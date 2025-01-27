package Spring.MindStone.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


public class DiaryRequestDTO {

    @Getter
    @Setter
    public static class DiaryCreationRequestDTO {
        private Long id;          // Optional: 사용자 ID
        @NotBlank
        private String bodyPart;
        @NotNull
        private LocalDate date;
    }

    @Getter
    @Setter
    public static class DiaryGetRequestDTO {
        private Long id;          // Optional: 사용자 ID
        @NotNull
        private LocalDate date;
    }
}

