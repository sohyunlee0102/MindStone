package Spring.MindStone.web.dto.habitDto;

import Spring.MindStone.domain.enums.HabitColor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class HabitRequestDto {

    @Getter
    @Setter
    public static class HabitDto {

        @NotBlank
        @Schema(description = "습관 이름", example = "운동하기")
        String title;
        @Schema(description = "습관을 설정한 요일, 일월화수목금토 기준 0000000에 표시 (예: '1010101' -> 일화목토)", example = "1010101")
        @NotNull
        String dayOfWeek;
        @NotNull
        @Schema(description = "알림 시간 (HH:mm 형식으로 저장됨)", example = "15:00/16:00")
        String alarmTime;
        @NotNull
        @Schema(description = "지속 시간 (분 단위)", example = "30")
        Integer targetTime;
        @NotNull
        @Schema(description = "습관 활성화 여부", example = "true")
        Boolean isActive;
        @NotNull
        @Schema(description = "습관 색상", example = "RED")
        HabitColor habitColor;

    }

    @Getter
    @Setter
    public static class HabitUpdateDto {

        @NotNull
        Long habitId;
        @NotBlank
        @Schema(description = "습관 이름", example = "운동하기")
        String title;
        @Schema(description = "습관을 설정한 요일, 일월화수목금토 기준 0000000에 표시 (예: '1010101' -> 일화목토)", example = "1010101")
        @NotNull
        String dayOfWeek;
        @NotNull
        @Schema(description = "알림 시간 (HH:mm 형식으로 저장됨)", example = "15:00/16:00")
        String alarmTime;
        @NotNull
        @Schema(description = "지속 시간 (분 단위)", example = "30")
        Integer targetTime;
        @NotNull
        @Schema(description = "습관 활성화 여부", example = "true")
        Boolean isActive;
        @NotNull
        @Schema(description = "습관 색상", example = "RED")
        HabitColor habitColor;

    }

    @Getter
    @Setter
    public static class CreateHabitHistoryDto {

        @NotBlank
        String comment;
        @NotNull
        LocalDate date;
        LocalDateTime startTime;
        LocalDateTime endTime;
        HabitColor habitColor;
        @NotNull
        Long habitId;

    }

    @Getter
    @Setter
    public static class UpdateHabitHistoryDto {

        Long habitHistoryId;
        Long executionId;
        LocalDateTime startTime;
        LocalDateTime endTime;
        HabitColor habitColor;
        String comment;
    }

    @Getter
    @Setter
    public static class CreateHabitExecutionDto {

        @NotNull
        Long habitHistoryId;
        LocalDateTime startTime;
        LocalDateTime endTime;
    }

}