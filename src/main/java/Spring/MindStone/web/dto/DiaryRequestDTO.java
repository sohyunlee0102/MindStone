package Spring.MindStone.web.dto;

import lombok.*;


public class DiaryRequestDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiaryCreationRequestDTO {
        private Long id;          // Optional: 사용자 ID
        private String bodyPart;
    }
}
