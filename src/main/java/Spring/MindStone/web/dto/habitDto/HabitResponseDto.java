package Spring.MindStone.web.dto.habitDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class HabitResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HabitDTO {
        Long habitId;
        LocalDateTime createdAt;
    }

}
