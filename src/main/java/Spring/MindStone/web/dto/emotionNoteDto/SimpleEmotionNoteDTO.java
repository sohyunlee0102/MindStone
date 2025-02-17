package Spring.MindStone.web.dto.emotionNoteDto;

import Spring.MindStone.domain.emotion.EmotionNote;
import Spring.MindStone.domain.emotion.StressEmotionNote;
import Spring.MindStone.web.dto.emotionDto.SimpleEmotionStatisticDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SimpleEmotionNoteDTO {

    private Long id;

    @NotNull(message = "무슨 감정을 느꼈는지 적어야합니다.")
    @Schema(description = "감정", example = "ANGER, DEPRESSION, SAD, CALM, JOY, THRILL, HAPPINESS")
    private String emotion;

    @NotNull(message = "감정을 얼마나 느꼈는지 적어야합니다.")
    @Schema(description = "감정수치", example = "10~100")
    private Integer emotionFigure;

    @Schema(description = "감정이유 작성", example = "외출을 다짐했는데 비가 와서 슬펐다.")
    private String content;

    public SimpleEmotionNoteDTO(EmotionNote emotionNote) {
        id = emotionNote.getId();
        emotion =emotionNote.getEmotion().toString();
        emotionFigure = emotionNote.getEmotionFigure();
        content = emotionNote.getContent();
    }

    public SimpleEmotionNoteDTO(StressEmotionNote emotionNote){
        id = emotionNote.getId();
        emotion =emotionNote.getEmotion().toString();
        emotionFigure = emotionNote.getEmotionFigure();
        content = emotionNote.getContent();
    }

}
