package Spring.MindStone.web.dto.habitDto;

import Spring.MindStone.domain.enums.HabitColor;
import Spring.MindStone.domain.habit.HabitExecution;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
    public static class HabitHistoryWithExecutionDTO {
        Long habitId;
        Long habitHistoryId;
        String comment;
        HabitColor habitColor;
        List<HabitExecutionDTO> executions;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HabitExecutionDTO {
        private Long id;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
    }


}
