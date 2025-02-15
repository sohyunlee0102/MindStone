package Spring.MindStone.web.dto.habitCalendarDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HabitCalendarRequestDto {

    @NotNull
    @Schema(description = "조회할 연도", example = "2024")
    private int year;

    @NotNull
    @Schema(description = "조회할 월", example = "12")
    private int month;
}
