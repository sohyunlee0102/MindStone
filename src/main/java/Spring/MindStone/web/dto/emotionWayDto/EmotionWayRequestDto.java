package Spring.MindStone.web.dto.emotionWayDto;

import Spring.MindStone.domain.enums.Job;
import Spring.MindStone.domain.enums.MBTI;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmotionWayRequestDto {

    @NotNull
    @Schema(description = "회원 ID", example = "1")
    private Long memberId;

    @NotNull
    @Schema(description = "MBTI", example = "INFJ")
    private MBTI mbti;

    @NotNull
    @Schema(description = "직업", example = "ENGINEER")
    private Job job;

    @NotBlank
    @Schema(description = "취미", example = "독서")
    private String hobby;

    @NotBlank
    @Schema(description = "특기", example = "베이킹")
    private String strengths;

    @NotBlank
    @Schema(description = "스트레스 해소 방법", example = "운동")
    private String stressManagement;
}
