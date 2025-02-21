package Spring.MindStone.web.dto.dummyDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DummyEmotionStatistic {
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
}
