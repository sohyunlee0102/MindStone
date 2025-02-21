package Spring.MindStone.web.controller;

import Spring.MindStone.apiPayload.ApiResponse;
import Spring.MindStone.service.habitService.HabitCalendarService;
import Spring.MindStone.web.dto.habitCalendarDto.HabitCalendarResponseDto;
import Spring.MindStone.config.jwt.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/habit-calendar")
@RequiredArgsConstructor
@Tag(name = "HabitCalendar", description = "습관 달력 조회 API")
public class HabitCalendarController {

    private final HabitCalendarService habitCalendarService;

    @Operation(summary = "습관 달력 조회 API", description = "사용자의 특정 년/월 습관 기록 데이터를 조회합니다.")
    @GetMapping
    public ApiResponse<HabitCalendarResponseDto> getCalendarData(
            @RequestHeader("Authorization") String authorization,
            @RequestParam int year,
            @RequestParam int month) {

        Long memberId = JwtTokenUtil.extractMemberId(authorization);
        return ApiResponse.onSuccess(habitCalendarService.getCalendarData(memberId, year, month));
    }
}
