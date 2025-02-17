package Spring.MindStone.web.dto.diaryDto;

import Spring.MindStone.domain.diary.DailyDiary;
import Spring.MindStone.domain.diary.DiaryImage;
import Spring.MindStone.domain.enums.EmotionList;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class SimpleDiaryDTO {

    private Long id;

    private LocalDate date; // 날짜

    private EmotionList emotion;

    private String title; // 일기 제목

    private String content; // 일기 내용

    private List<String> imagePath; // 이미지 경로

    public SimpleDiaryDTO(DailyDiary diary){
        id = diary.getId();
        emotion = diary.getEmotion();
        date = diary.getDate();
        title = diary.getTitle();
        content = diary.getContent();
        imagePath = diary.getDiaryImageList().stream()
                .map(DiaryImage::getImagePath).collect(Collectors.toList());
    }
}
