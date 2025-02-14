package Spring.MindStone.web.dto.habitCalendarDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HabitCalendarResponseDto {

    @Schema(description = "해당 월 기록한 일 수 / 전체 일 수", example = "50.0")
    private double recordPercentage;

    @Schema(description = "100% 달성한 일 수", example = "3")
    private int fullAchievementCount;

    @Schema(description = "일별 기록 리스트")
    private List<DailyRecord> dailyRecords;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyRecord {
        @Schema(description = "날짜", example = "1")
        private int day;

        @Schema(description = "완료된 습관 개수", example = "2")
        private int completedHabits;

        @Schema(description = "총 습관 개수", example = "3")
        private int totalHabits;
    }
}