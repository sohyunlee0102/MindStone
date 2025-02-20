package Spring.MindStone.web.controller;

import Spring.MindStone.apiPayload.ApiResponse;
import Spring.MindStone.config.jwt.JwtTokenUtil;
import Spring.MindStone.service.emotionNoteService.EmotionNoteService;
import Spring.MindStone.web.dto.emotionDto.EmotionReportResponseDTO;
import Spring.MindStone.web.dto.habitReportDto.HabitReportResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/emotion-report")
@RequiredArgsConstructor
@Tag(name = "EmotionReport", description = "사용자의 달력 리포트 정보 조회 API")
public class EmotionReportController {
    private final EmotionNoteService emotionNoteService;

    @Operation(summary = "감정 리포트 조회 API", description = "사용자의 특정 년/월 감정 리포트를 조회합니다.")
    @GetMapping
    public ApiResponse<EmotionReportResponseDTO> getEmotionReport(
            @RequestHeader("Authorization") String authorization,
            @RequestParam int year,
            @RequestParam int month) {

        Long memberId = JwtTokenUtil.extractMemberId(authorization);
        return ApiResponse.onSuccess(emotionNoteService.getEmotionCallendarReport(memberId, year, month));
    }
}
