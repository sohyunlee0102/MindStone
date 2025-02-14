
package Spring.MindStone.web.controller;

import Spring.MindStone.apiPayload.ApiResponse;
import Spring.MindStone.config.jwt.JwtTokenUtil;
import Spring.MindStone.service.emotionWayService.EmotionWayService;
import Spring.MindStone.web.dto.emotionWayDto.AiRecommandResponseDto;
import Spring.MindStone.web.dto.emotionWayDto.EmotionWayRequestDto;
import Spring.MindStone.web.dto.emotionWayDto.EmotionWayResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/emotionWay")
@RequiredArgsConstructor
@Tag(name = "EmotionWay", description = "사용자의 감정 관리 방법 조회 및 수정 API")
public class EmotionWayController {

    private final EmotionWayService emotionWayService;

    /**
     * 감정 관리 방법 조회
     */

    @GetMapping
    @Operation(summary = "감정 관리 방법 조회 API", description = "회원 ID를 이용해 감정 관리 방법을 조회합니다.")
    public ApiResponse<EmotionWayResponseDto> getEmotionWay(
            @RequestHeader("Authorization") String authorization) {
        Long memberId = JwtTokenUtil.extractMemberId(authorization);
        return ApiResponse.onSuccess(emotionWayService.getEmotionWay(memberId));
    }

    /**
     * 감정 관리 방법 수정
     */

    @PutMapping
    @Operation(summary = "감정 관리 방법 수정 API", description = "회원 ID를 이용해 감정 관리 방법을 수정합니다.")
    public ApiResponse<String> updateEmotionWay(
            @RequestHeader("Authorization") String authorization,
            @RequestBody EmotionWayRequestDto request) {

        Long memberId = JwtTokenUtil.extractMemberId(authorization);
        emotionWayService.updateEmotionWay(memberId, request);
        return ApiResponse.onSuccess("감정 관리 방법이 성공적으로 수정되었습니다.");
    }

    @GetMapping("/stress/gpt")
    @Operation(summary = "GPT에게 행동 추천받기", description = "슬픈감정일때 행동추천을 받는걸 gpt로 받습니다.")
    @Parameters({
            @Parameter(name = "previousRecommand", description = "이미 받았던 추천 없으면 공백으로")
    })
    public ApiResponse<AiRecommandResponseDto> getAIRecommand(
            @RequestHeader("Authorization") String authorization,
            @RequestParam("previousRecommand") String previousRecommand
    ){
        Long memberId = JwtTokenUtil.extractMemberId(authorization);
        return ApiResponse.onSuccess(emotionWayService.getAiRecommand(memberId, previousRecommand));
    }

    @GetMapping("/stress")
    @Operation(summary = "자신이 적은 행동 랜덤으로 3개 추천받기",
            description = "슬픈감정일때 행동추천을 받는걸 자신의 껄로 3개 받습니다.")
    public ApiResponse<String> getRecommand(
            @RequestHeader("Authorization") String authorization
    ){
        Long memberId = JwtTokenUtil.extractMemberId(authorization);
        return ApiResponse.onSuccess(emotionWayService.getRecommand(memberId));
    }
    

}
