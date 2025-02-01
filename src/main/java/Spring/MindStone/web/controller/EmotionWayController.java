package Spring.MindStone.web.controller;

import Spring.MindStone.apiPayload.ApiResponse;
import Spring.MindStone.config.jwt.JwtTokenUtil;
import Spring.MindStone.service.emotionWayService.EmotionWayService;
import Spring.MindStone.web.dto.emotionWayDto.EmotionWayRequestDto;
import Spring.MindStone.web.dto.emotionWayDto.EmotionWayResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/emotionWay")
@RequiredArgsConstructor
@Tag(name = "감정 관리 방법", description = "사용자의 감정 관리 방법 조회 및 수정 API")
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
}
