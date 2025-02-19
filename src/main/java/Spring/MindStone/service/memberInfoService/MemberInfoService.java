package Spring.MindStone.service.memberInfoService;

import Spring.MindStone.apiPayload.code.status.ErrorStatus;
import Spring.MindStone.apiPayload.exception.handler.MemberInfoHandler;
import Spring.MindStone.domain.enums.Status;
import Spring.MindStone.domain.listener.ListenerUtil;
import Spring.MindStone.domain.member.MemberInfo;
import Spring.MindStone.service.tokenBlacklistService.TokenBlacklistService;
import Spring.MindStone.web.dto.memberDto.MemberInfoRequestDTO;
import Spring.MindStone.repository.memberRepository.MemberInfoRepository;
import Spring.MindStone.web.dto.memberDto.MemberInfoResponseDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberInfoService {

    @PersistenceContext
    private EntityManager entityManager;

    private final MemberInfoRepository memberInfoRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenBlacklistService tokenBlacklistService;

    // 멤버 등록
    @Transactional
    public MemberInfoResponseDTO.JoinResultDTO joinMember(MemberInfoRequestDTO.JoinDto request) {
        ListenerUtil.disableListener();

        Optional<MemberInfo> existingMemberOpt = memberInfoRepository.findByEmail(request.getEmail());

        if (existingMemberOpt.isPresent()) {
            MemberInfo exisingMember = existingMemberOpt.get();

            if (exisingMember.getStatus() == Status.INACTIVE) {
                exisingMember.setStatus(Status.ACTIVE);
                exisingMember.setInactiveDate(null);
                memberInfoRepository.save(exisingMember);
                ListenerUtil.enableListener();
                return new MemberInfoResponseDTO.JoinResultDTO(exisingMember.getId(), LocalDateTime.now());
            } else {
                throw new MemberInfoHandler(ErrorStatus.EMAIL_ALREADY_EXISTS);
            }
        }

        // 온보딩 이전 Not Null 필드의 디폴트 값 설정
        String nickname = request.getNickname();
        if (nickname == null || nickname.trim().isEmpty()) {
            nickname = "default_nickname";
        }

        LocalDate birthday = request.getBirthday();
        if (birthday == null) {
            birthday = LocalDate.now();
        }

        Boolean shareScope = request.getShareScope();
        if (shareScope == null) {
            shareScope = true;
        }

        Boolean marketingAgree = request.getMarketingAgree();
        if (marketingAgree == null) {
            marketingAgree = true;
        }

        MemberInfo newMemberInfo = MemberInfo.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .nickname(nickname)
                .mbti(request.getMbti())
                .birthday(birthday)
                .job(request.getJob())
                .shareScope(shareScope)
                .marketingAgree(marketingAgree)
                .role(request.getRole())
                .build();

        newMemberInfo.encodePassword(passwordEncoder.encode(request.getPassword()));

        memberInfoRepository.save(newMemberInfo);

        ListenerUtil.enableListener();

        return new MemberInfoResponseDTO.JoinResultDTO(newMemberInfo.getId(), LocalDateTime.now());
    }

    // 이메일 찾기
    @Transactional
    public MemberInfoResponseDTO.EmailDto findEmail(MemberInfoRequestDTO.EmailDto request) {
        MemberInfo memberInfo = memberInfoRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new MemberInfoHandler(ErrorStatus.MEMBER_NOT_FOUND));
        return new MemberInfoResponseDTO.EmailDto(memberInfo.getEmail(), LocalDateTime.now());
    }

    // 아이디 찾기
    @Transactional
    public MemberInfo findMemberByEmail(String email) {
        return memberInfoRepository.findByEmail(email)
                .orElseThrow(() -> new MemberInfoHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }

    @Transactional
    public MemberInfo findMemberById(Long memberId) {
        return memberInfoRepository.findById(memberId)
                .orElseThrow(() -> new MemberInfoHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }

    @Transactional
    public Long updateNickname(MemberInfoRequestDTO.NicknameDto request, Long memberId) {
        MemberInfo member = findMemberById(memberId);
        member.setNickname(request.getNickname());
        return memberInfoRepository.save(member).getId();
    }

    @Transactional
    public Long updatePassword(MemberInfoRequestDTO.PasswordDto request, Long memberId) {
        MemberInfo member = findMemberById(memberId);

        if (!passwordEncoder.matches(request.getOldPassword(), member.getPassword())) {
            throw new MemberInfoHandler(ErrorStatus.INVALID_PASSWORD);
        }
        member.setPassword(passwordEncoder.encode(request.getNewPassword()));
        return memberInfoRepository.save(member).getId();
    }

    @Transactional
    public void deactivateMember(Long memberId, String refreshToken) {
        MemberInfo member = findMemberById(memberId);

        member.setStatus(Status.INACTIVE);
        member.setInactiveDate(LocalDate.now());

        tokenBlacklistService.addToBlackList(refreshToken);

        memberInfoRepository.save(member);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void deleteInactiveMembers() {
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        List<MemberInfo> membersToDelete = memberInfoRepository.findByStatusAndInactiveDateBefore(Status.INACTIVE, thirtyDaysAgo);

        memberInfoRepository.deleteAll(membersToDelete);
    }

}