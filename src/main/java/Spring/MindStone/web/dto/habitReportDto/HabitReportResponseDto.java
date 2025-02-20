package Spring.MindStone.web.dto.habitReportDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class HabitReportResponseDto {

    @Schema(description = "한 달 동안 기록한 습관 비율 (%)", example = "50.0")
    private double recordPercentage;

    @Schema(description = "습관 달성률 증가율 (%)", example = "30.0")
    private double achievementGrowth;

    @Schema(description = "가장 많이 기록한 습관", example = "운동하기")
    private String topHabit;

    @Schema(description = "주별 달성률")
    private List<WeeklyData> weeklyAchievementRates;

    @Schema(description = "주별 활동 시간 (분)")
    private List<WeeklyData> weeklyActiveTime;

    @Schema(description = "주별 활동 횟수")
    private List<WeeklyData> weeklyHabitCounts;

    @Getter
    @AllArgsConstructor
    public static class WeeklyData {
        private int week;
        private long habitId;
        private long value;
    }
}
