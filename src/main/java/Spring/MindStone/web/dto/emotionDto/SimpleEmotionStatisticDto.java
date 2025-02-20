package Spring.MindStone.web.dto.emotionDto;

import Spring.MindStone.domain.diary.DailyEmotionStatistic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleEmotionStatisticDto {
    @Schema(description = "통계가 저장된 날짜", example = "2024-02-01")
    private LocalDate date;

    @Schema(description = "분노 수치", example = "5")
    private Integer angerFigure;

    @Schema(description = "우울 수치", example = "3")
    private Integer depressionFigure;

    @Schema(description = "슬픔 수치", example = "4")
    private Integer sadFigure;

    @Schema(description = "평온 수치", example = "7")
    private Integer calmFigure;

    @Schema(description = "기쁨 수치", example = "6")
    private Integer joyFigure;

    @Schema(description = "전율 수치", example = "2")
    private Integer thrillFigure;

    @Schema(description = "행복 수치", example = "8")
    private Integer happinessFigure;

    public SimpleEmotionStatisticDto(DailyEmotionStatistic entity) {
        this.date = entity.getDate();
        this.angerFigure = entity.getAngerFigure();
        this.depressionFigure = entity.getDepressionFigure();
        this.sadFigure = entity.getSadFigure();
        this.calmFigure = entity.getCalmFigure();
        this.joyFigure = entity.getJoyFigure();
        this.thrillFigure = entity.getThrillFigure();
        this.happinessFigure = entity.getHappinessFigure();
    }
}
