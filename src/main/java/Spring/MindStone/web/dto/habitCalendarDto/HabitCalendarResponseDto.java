package Spring.MindStone.web.dto.habitCalendarDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class HabitCalendarResponseDto {

    @Schema(description = "한 달 동안 기록한 습관 비율 (%)", example = "50.0")
    private double recordPercentage;

    @Schema(description = "모든 습관을 100% 완료한 날의 개수", example = "3")
    private int fullAchievementCount;

    @Schema(description = "일별 습관 기록")
    private List<DailyRecord> dailyRecords;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class DailyRecord {
        @Schema(description = "날짜", example = "1")
        private int day;

        @Schema(description = "완료한 습관 개수", example = "2")
        private int completedHabits;

        @Schema(description = "해당 날짜의 전체 습관 개수", example = "3")
        private int totalHabits;
    }
}