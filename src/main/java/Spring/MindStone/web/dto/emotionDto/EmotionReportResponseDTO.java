package Spring.MindStone.web.dto.emotionDto;

import Spring.MindStone.domain.diary.DailyEmotionStatistic;
import Spring.MindStone.web.dto.habitReportDto.HabitReportResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class EmotionReportResponseDTO {
    @Schema(description = "한 달 요약글(첫째장)", example = "${month}월에는 ${percentage}%기록했고 행복감정이 가장 많았어요")
    private String totalReport;

    @Schema(description = "이미지 선택에 필요한 감정",example = "ANGER, DEPRESSION, SAD, CALM, JOY, THRILL, HAPPINESS 중 하나")
    private String bestEmotion;

    @Schema(description = "감정분포수치(둘째장)")
    private List<WeeklyRecord> totalEmtoionStatistic;

    @Schema(description = "총평(셋째장)")
    private String totalSummary;

    @Getter
    @Builder
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WeeklyRecord {
        @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
        private Integer angerFigure = 0; // 분노 수치

        @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
        private Integer depressionFigure = 0; // 우울 수치

        @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
        private Integer sadFigure = 0; // 슬픔 수치

        @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
        private Integer calmFigure = 0; // 평온 수치

        @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
        private Integer joyFigure = 0; // 기쁨 수치

        @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
        private Integer thrillFigure = 0; // 설렘 수치

        @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
        private Integer happinessFigure = 0; // 행복 수치

        public void add(WeeklyRecord other) {
            this.angerFigure += other.angerFigure;
            this.depressionFigure += other.depressionFigure;
            this.sadFigure += other.sadFigure;
            this.calmFigure += other.calmFigure;
            this.joyFigure += other.joyFigure;
            this.thrillFigure += other.thrillFigure;
            this.happinessFigure += other.happinessFigure;
        }

        public WeeklyRecord(DailyEmotionStatistic statistic){
            this.angerFigure = statistic.getAngerFigure();
            this.depressionFigure = statistic.getDepressionFigure();
            this.sadFigure = statistic.getSadFigure();
            this.calmFigure = statistic.getCalmFigure();
            this.joyFigure = statistic.getJoyFigure();
            this.thrillFigure = statistic.getThrillFigure();
            this.happinessFigure = statistic.getHappinessFigure();
        }


        @com.fasterxml.jackson.annotation.JsonIgnore
        public String getHighestEmotion() {
            Map<String, Integer> emotions = Map.of(
                    "ANGER", angerFigure,
                    "DEPRESSION", depressionFigure,
                    "SAD", sadFigure,
                    "CALM", calmFigure,
                    "JOY", joyFigure,
                    "THRILL", thrillFigure,
                    "HAPPINESS", happinessFigure
            );

            return emotions.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(null);
        }
    }
}
