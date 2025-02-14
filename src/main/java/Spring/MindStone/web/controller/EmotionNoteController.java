package Spring.MindStone.web.controller;

import Spring.MindStone.apiPayload.ApiResponse;
import Spring.MindStone.config.jwt.JwtTokenUtil;
import Spring.MindStone.domain.emotion.EmotionNote;
import Spring.MindStone.repository.emotionNoteRepository.EmotionNoteRepository;
import Spring.MindStone.service.diaryService.DailyEmotionStatisticService;
import Spring.MindStone.service.emotionNoteService.EmotionNoteQueryService;
import Spring.MindStone.service.emotionNoteService.EmotionNoteService;
import Spring.MindStone.web.dto.emotionDto.SimpleEmotionStatisticDto;
import Spring.MindStone.web.dto.emotionNoteDto.EmotionNoteSaveDTO;
import Spring.MindStone.web.dto.emotionNoteDto.EmotionNoteStressSaveDTO;
import Spring.MindStone.web.dto.emotionNoteDto.SimpleEmotionNoteDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/emotionnote")
@RequiredArgsConstructor
@Tag(name = "EmotionManagerment", description = "감정에 대한 일과 기록 및 해결")
public class EmotionNoteController {

    private final EmotionNoteQueryService emotionNoteQueryService;
    private final EmotionNoteService emotionNoteService;
    private final DailyEmotionStatisticService dailyEmotionStatisticService;

    @PostMapping
    @Operation(summary = "감정 일과 기록", description = "감정에 대한 일과 저장")
    ApiResponse<SimpleEmotionNoteDTO> saveEmotionNote(
            @Valid @RequestBody EmotionNoteSaveDTO emotionNoteDTO,
            @RequestHeader("Authorization") String authorization) {
        Long memberId = JwtTokenUtil.extractMemberId(authorization);

        return ApiResponse.onSuccess(emotionNoteService.saveEmotionNote(emotionNoteDTO,memberId));
    }

    @PostMapping("/stress")
    @Operation(summary = "슬픈 감정을 골랐을때, 스트레스를 푸는 행동을 한 후 기록 저장", description = "감정에 대한 일과 저장")
    ApiResponse<SimpleEmotionNoteDTO> saveStressEmotionNote(
            @Valid @RequestBody EmotionNoteStressSaveDTO request,
            @RequestHeader("Authorization") String authorization) {
        Long memberId = JwtTokenUtil.extractMemberId(authorization);

        return ApiResponse.onSuccess(emotionNoteService.saveStressEmotionNote(request ,memberId));
    }

    @GetMapping("/statistic")
    @Operation(summary = "하루 사용자의 감정 통계 내줌(홈화면 그래프)")
    ApiResponse<SimpleEmotionStatisticDto> statisticEmotionNote(@RequestHeader("Authorization") String authorization){
        Long memberId = JwtTokenUtil.extractMemberId(authorization);
        return ApiResponse.onSuccess(dailyEmotionStatisticService.getStatistic(memberId));
    }


}