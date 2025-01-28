package Spring.MindStone.web.dto.diaryDto;

import Spring.MindStone.domain.diary.DailyDiary;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SimpleDiaryDTO {

    private Long id;

    private LocalDate date; // 날짜

    private String title; // 일기 제목

    private String content; // 일기 내용

    private String imagePath; // 이미지 경로

    public SimpleDiaryDTO(DailyDiary diary){
        id = diary.getId();
        date = diary.getDate();
        title = diary.getTitle();
        content = diary.getContent();
        imagePath = diary.getImagePath();
    }
}
