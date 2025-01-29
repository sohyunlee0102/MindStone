package Spring.MindStone.service.MemberInfoService;

import Spring.MindStone.apiPayload.code.status.ErrorStatus;
import Spring.MindStone.apiPayload.exception.handler.MemberInfoHandler;
import Spring.MindStone.domain.member.MemberInfo;
import Spring.MindStone.web.dto.memberDto.MemberInfoRequestDTO;
import Spring.MindStone.repository.memberRepository.MemberInfoRepository;
import Spring.MindStone.web.dto.memberDto.MemberInfoResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberInfoService {

    private final MemberInfoRepository memberInfoRepository;
    private final PasswordEncoder passwordEncoder;

    // 멤버 등록
    @Transactional
    public MemberInfoResponseDTO.JoinResultDTO joinMember(MemberInfoRequestDTO.JoinDto request) {
        if (memberInfoRepository.existsByEmail(request.getEmail())) {
            throw new MemberInfoHandler(ErrorStatus.EMAIL_ALREADY_EXISTS);
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
        MemberInfo member = memberInfoRepository.findById(memberId)
                .orElseThrow(() -> new MemberInfoHandler(ErrorStatus.MEMBER_NOT_FOUND));
        member.setNickname(request.getNickname());
        return memberInfoRepository.save(member).getId();
    }

    @Transactional
    public Long updatePassword(MemberInfoRequestDTO.PasswordDto request, Long memberId) {
        MemberInfo member = memberInfoRepository.findById(memberId)
                .orElseThrow(() -> new MemberInfoHandler(ErrorStatus.MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getOldPassword(), member.getPassword())) {
            throw new MemberInfoHandler(ErrorStatus.INVALID_PASSWORD);
        }
        member.setPassword(passwordEncoder.encode(request.getNewPassword()));
        return memberInfoRepository.save(member).getId();
    }

}