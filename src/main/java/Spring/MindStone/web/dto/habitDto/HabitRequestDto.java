package Spring.MindStone.web.dto.habitDto;

import Spring.MindStone.domain.enums.HabitColor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class HabitRequestDto {

    @Getter
    @Setter
    public static class HabitDto {

        @NotNull
        Long habitId;
        @NotBlank
        String title;
        @NotNull
        String dayOfWeek;
        @NotNull
        String alarmTime;
        @NotNull
        Integer targetTime;
        @NotNull
        Boolean isActive;
        @NotNull
        HabitColor habitColor;

    }
}