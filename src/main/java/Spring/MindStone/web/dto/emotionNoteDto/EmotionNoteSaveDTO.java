package Spring.MindStone.web.dto.emotionNoteDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmotionNoteSaveDTO {
    @NotNull(message = "무슨 감정을 느꼈는지 적어야합니다.")
    @Schema(description = "감정", example = "ANGER, DEPRESSION, SAD, CALM, JOY, THRILL, HAPPINESS")
    private String emotion;

    @NotNull(message = "감정을 얼마나 느꼈는지 적어야합니다.")
    @Schema(description = "감정수치", example = "10~100")
    private Integer emotionFigure;

    @Schema(description = "감정이유 작성", example = "외출을 다짐했는데 비가 와서 슬펐다.")
    private String content;
}
