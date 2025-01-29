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

    private String emotion;//일기에 관한 감정

    private String content; // 일기 내용

    private String impressiveThing; // 인상 깊은 일
}
