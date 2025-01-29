package Spring.MindStone.web.dto.diaryDto;

import Spring.MindStone.domain.diary.DailyDiary;

import java.time.LocalDate;

//달력 요청 했을떄 ResponseDTO
public class DiaryCallendarDTO {
    LocalDate date;

    Long diaryId;

    String emotion;

    public DiaryCallendarDTO(DailyDiary dailyDiary) {
        date = dailyDiary.getDate();
        diaryId = dailyDiary.getId();
        emotion = dailyDiary.getEmotion().toString();
    }
}
