package Spring.MindStone.web.dto.emotionWayDto;

import lombok.Builder;

@Builder
public class AiRecommandResponseDto {
    String recommand;
    String previousRecommand;
}
