package Spring.MindStone.web.controller;

import Spring.MindStone.service.habitService.HabitReportService;
import Spring.MindStone.web.dto.habitReportDto.HabitReportResponseDto;
import Spring.MindStone.config.jwt.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/habit-report")
@RequiredArgsConstructor
@Tag(name = "HabitReport", description = "사용자의 습관 리포트 정보 조회 API")
public class HabitReportController {

    private final HabitReportService habitReportService;

    @Operation(summary = "습관 리포트 조회 API", description = "사용자의 특정 년/월 습관 리포트를 조회합니다.")
    @GetMapping
    public ResponseEntity<HabitReportResponseDto> getHabitReport(
            @RequestHeader("Authorization") String authorization,
            @RequestParam int year,
            @RequestParam int month) {

        Long memberId = JwtTokenUtil.extractMemberId(authorization);
        return ResponseEntity.ok(habitReportService.getHabitReport(memberId, year, month));
    }
}
