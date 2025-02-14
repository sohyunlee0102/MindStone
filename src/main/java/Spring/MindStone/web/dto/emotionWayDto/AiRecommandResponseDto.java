package Spring.MindStone.web.dto.emotionWayDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiRecommandResponseDto {
    String recommand;
    String previousRecommand;
}
