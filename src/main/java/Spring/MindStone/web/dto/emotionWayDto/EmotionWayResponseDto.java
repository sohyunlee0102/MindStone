package Spring.MindStone.web.dto.emotionWayDto;

import Spring.MindStone.domain.enums.Job;
import Spring.MindStone.domain.enums.MBTI;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmotionWayResponseDto {
    private MBTI mbti;
    private Job job;
    private String hobby;
    private String strengths;
    private String stressManagement;
}
