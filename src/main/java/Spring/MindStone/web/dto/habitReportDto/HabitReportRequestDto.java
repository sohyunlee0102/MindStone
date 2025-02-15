package Spring.MindStone.web.dto.habitReportDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HabitReportRequestDto {

    @Schema(description = "조회할 연도", example = "2024")
    private int year;

    @Schema(description = "조회할 월", example = "12")
    private int month;
}
