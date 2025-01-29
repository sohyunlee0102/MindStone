package Spring.MindStone.web.dto.diaryDto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DiaryUpdateDTO {
    private LocalDate date; // 날짜

    private String emotion;//일기에 관한 감정

    private String content; // 일기 내용
}
