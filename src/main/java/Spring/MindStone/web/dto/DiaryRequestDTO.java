package Spring.MindStone.web.dto;

import lombok.*;

import java.time.LocalDate;


public class DiaryRequestDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiaryCreationRequestDTO {
        private Long id;          // Optional: 사용자 ID
        private String bodyPart;
        private LocalDate date;
    }
}

