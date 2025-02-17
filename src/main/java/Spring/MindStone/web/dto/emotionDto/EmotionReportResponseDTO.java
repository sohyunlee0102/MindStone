package Spring.MindStone.web.dto.emotionDto;

import Spring.MindStone.web.dto.habitReportDto.HabitReportResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmotionReportResponseDTO {
    @Schema(description = "한 달 동안 기록한 감정 비율 (%)(첫째장)", example = "50.0")
    private double recordPercentage;

    @Schema(description = "하루 가장 많았던 감정(첫째장)", example = "ANGER, DEPRESSION, SAD, CALM, JOY, THRILL, HAPPINESS;")
    private String totalBestEmtoion;

    @Schema(description = "감정분포수치(둘째장)", example = "index 1이 1주차, index2가 2주차")
    private List<SimpleEmotionStatisticDto> totalEmtoionStatistic;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class WeeklyRecord {
        @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
        private Integer angerFigure = 0; // 분노 수치

        @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
        private Integer depressionFigure = 0; // 우울 수치

        @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
        private Integer sadFigure = 0; // 슬픔 수치

        @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
        private Integer calmFigure = 0; // 슬픔 수치

        @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
        private Integer joyFigure = 0; // 기쁨 수치

        @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
        private Integer thrillFigure = 0; // 전율 수치

        @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
        private Integer happinessFigure = 0; // 행복 수치
    }
}
