package Spring.MindStone.service.onboardingService;

import Spring.MindStone.apiPayload.code.status.ErrorStatus;
import Spring.MindStone.apiPayload.code.status.SuccessStatus;
import Spring.MindStone.apiPayload.exception.handler.MemberInfoHandler;
import Spring.MindStone.domain.member.MemberInfo;
import Spring.MindStone.repository.memberRepository.MemberInfoRepository;
import Spring.MindStone.service.habitService.HabitService;
import Spring.MindStone.web.dto.onboardingDto.OnboardingRequestDto;
import Spring.MindStone.web.dto.onboardingDto.OnboardingResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OnboardingService {

    @Autowired
    private MemberInfoRepository memberInfoRepository;

    @Autowired
    private HabitService habitService;

    /**
     * 온보딩 처리 (회원 정보 저장)
     * @param onboardingRequestDto 온보딩 요청 DTO
     * @param email 회원 이메일
     * @return OnboardingResponseDto 온보딩 응답 DTO
     */
    public OnboardingResponseDto onboardMember(OnboardingRequestDto onboardingRequestDto, String email) { // 회원 정보 엔티티로 변환
        // 이메일 중복 체크
        /*
        if (memberInfoRepository.existsByEmail(email)){
            // 이메일이 중복될 경우 - ErrorStatus
            return new OnboardingResponseDto(
                    ErrorStatus.EMAIL_ALREADY_EXISTS.getCode(),
                    ErrorStatus.EMAIL_ALREADY_EXISTS.getMessage(),
                    false,
                    ErrorStatus.EMAIL_ALREADY_EXISTS.getHttpStatus().name()
            );
        }
         */
        /*
        // 회원 정보 생성 및 저장
        MemberInfo memberInfo = MemberInfo.builder()
                .email(email)
                .nickname(onboardingRequestDto.getNickname())
                .birthday(onboardingRequestDto.getBirthDate())
                .mbti(onboardingRequestDto.getMbti())
                .job(onboardingRequestDto.getJob())
                .build();

        memberInfoRepository.save(memberInfo);

         */

        MemberInfo memberInfo = memberInfoRepository.findByEmail(email)
                .orElseThrow(() -> new MemberInfoHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 기존 정보 업데이트
        memberInfo.setNickname(onboardingRequestDto.getNickname());
        memberInfo.setBirthday(onboardingRequestDto.getBirthDate());
        memberInfo.setMbti(onboardingRequestDto.getMbti());
        memberInfo.setJob(onboardingRequestDto.getJob());

        // 습관 저장 로직 (선택 사항)
        if (onboardingRequestDto.getHabits() != null && !onboardingRequestDto.getHabits().isEmpty()) {
            habitService.createHabits(onboardingRequestDto.getHabits(), memberInfo);
        }


        // 온보딩 성공 시 - SuccessStatus
        return new OnboardingResponseDto(
                SuccessStatus._OK.getCode(),
                SuccessStatus._OK.getMessage(),
                true,
                SuccessStatus._OK.getHttpStatus().name()
        );
    }
}
