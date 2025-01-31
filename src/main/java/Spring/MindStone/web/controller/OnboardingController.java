package Spring.MindStone.web.controller;


import Spring.MindStone.domain.member.MemberInfo;
import Spring.MindStone.service.onboardingService.OnboardingService;
import Spring.MindStone.web.dto.onboardingDto.OnboardingRequestDto;
import Spring.MindStone.web.dto.onboardingDto.OnboardingResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Onboarding", description = "회원 온보딩 관련 API")
@RestController
@RequestMapping("/api/members")
public class OnboardingController {
    @Autowired
    private OnboardingService onboardingService;

    /**
     * 온보딩 요청 처리 및 응답 반환
     * @param requestDto 온보딩 요청 DTO
     * @return OnboardingResponseDto 온보딩 결과 응답 DTO
     */
    @Operation(
            summary = "회원 온보딩 설문(기본 정보 체크) 작성",
            description = "회원이 온보딩 정보를 작성하면 유효성 검사를 거쳐 회원 정보를 저장합니다."
    )
    @PostMapping("/survey")
    public OnboardingResponseDto onboardSurvey(@Valid @RequestBody OnboardingRequestDto requestDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = ((MemberInfo) auth.getPrincipal()).getEmail();
        return onboardingService.onboardMember(requestDto, userEmail);
    }
}
