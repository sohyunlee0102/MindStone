package Spring.MindStone.web.dto.diaryDto;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DiarySaveDTO {
    private LocalDate date; // 날짜

    private String title; // 일기 제목

    private String content; // 일기 내용

    private String impressiveThing; // 인상 깊은 일

    private Boolean isPublic; // 공개 여부

    private String imagePath; // 이미지 경로
}
