package Spring.MindStone.web.controller;

import Spring.MindStone.service.memberInfoService.MemberInterestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/emotionManagement")
@RequiredArgsConstructor
@Tag(name = "EmotionManagerment", description = "감정에 대한 일과 기록 및 해결")
public class MemberInterestController {
    private final MemberInterestService memberInterestService;

    /*@GetMapping("/")
    ApiResponse<String> getStressAcitonMine(@RequestHeader("Authorization") String authorization){
        Long memberId = JwtTokenUtil.extractMemberId(authorization);
        return ApiResponse.onSuccess(memberInterestService.getStressActionMine(memberId));
    }

    @GetMapping("/mbti")
    ApiResponse<String> getStressActionOtherByMBTI(@RequestPart(value = "mbti", required = false) String mbti
            ,@RequestHeader("Authorization") String authorization){
        Long memberId = JwtTokenUtil.extractMemberId(authorization);
        return null;
    }

    @GetMapping("/job")
    ApiResponse<String> getStressActionOtherByJob(){
        return null;
    }*/
}
