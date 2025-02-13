package Spring.MindStone.web.dto.habitDto;

import Spring.MindStone.domain.enums.HabitColor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetHabitDTO {
        Long habitId;
        String title;
        String dayOfWeek;
        String alarmTime;
        Integer targetTime;
        Boolean isActive;
        HabitColor habitColor;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetHabitForNowDTO {
        Long habitId;
        String title;
        Integer targetTime;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetLastHabitHistoryDTO {
        Long daysSinceLastHabit;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HabitHistoryDTO {
        Long habitHistoryId;
        String comment;
        LocalDateTime startTime;
        LocalDateTime endTime;
        HabitColor habitColor;

    }

}
