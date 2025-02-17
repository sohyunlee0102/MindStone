package Spring.MindStone.web.dto.emotionNoteDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmotionNoteStressSaveDTO {
    @NotNull(message = "무슨 감정을 느꼈는지 적어야합니다.")
    @Schema(description = "감정", example = "ANGER, DEPRESSION, SAD, CALM, JOY, THRILL, HAPPINESS")
    private String emotion;

    @NotNull(message = "그 감정을 느끼게 한 일의 엔티티 id를 적어주세요")
    @Schema(description = "슬픈감정기록 ID", example = "바로 직전에 적었던 슬픈 감정의 ID를 적어주세요")
    private Long steressReson_id;

    @NotNull(message = "감정을 얼마나 느꼈는지 적어야합니다.")
    @Schema(description = "감정수치", example = "10~100")
    private Integer emotionFigure;

    @NotNull(message = "어떤 스트레스 푸는 행동을 했는지 적어야합니다.")
    @Schema(description = "스트레스 푼 행동", example = "게임하기")
    private String content;

    @Schema(description = "자신의 스트레스 푸는 행동을 사용했는지의 유무")
    private boolean isRecommend;

    @Schema(description = "얼마나 했는지 시간 작성", example = "1시간 30분")
    private String time;
}
