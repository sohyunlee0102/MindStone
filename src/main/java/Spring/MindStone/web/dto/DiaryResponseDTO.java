package Spring.MindStone.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class DiaryResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiaryCreationResponseDTO {
        private String content;
        private String bodyPart;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiaryGetResponseDTO {
        private Long id; // 기본 키
        private LocalDate date;
        private Long memberId; // MemberInfo의 ID
        private String title; // 일기 제목
        private String content; // 일기 내용
        private String imagePath; // 이미지 경로
    }
}
