package Spring.MindStone.web.controller;

import Spring.MindStone.apiPayload.ApiResponse;
import Spring.MindStone.config.jwt.JwtTokenUtil;
import Spring.MindStone.service.DummyService;
import Spring.MindStone.web.dto.dummyDto.DummyEmotionStatistic;
import Spring.MindStone.web.dto.emotionNoteDto.EmotionNoteSaveDTO;
import Spring.MindStone.web.dto.emotionNoteDto.SimpleEmotionNoteDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dummy")
@RequiredArgsConstructor
@Tag(name = "DummyData", description = "날짜기준으로 더미데이터 넣기")
public class DummyDataController {


    private final DummyService dummyService;

    @PostMapping("/emotionStatistic")
    @Operation(summary = "감정 통계 날짜로 만들기")
    ApiResponse<Long> makeDummyEmotionStatistic(
            @Valid @RequestBody DummyEmotionStatistic dummyEmotionStatistic,
            @RequestHeader("Authorization") String authorization) {
        Long memberId = JwtTokenUtil.extractMemberId(authorization);
        dummyService.dummyStatistic(memberId,dummyEmotionStatistic);
        return ApiResponse.onSuccess(1L);
    }
}
