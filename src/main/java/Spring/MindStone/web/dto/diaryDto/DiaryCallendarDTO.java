package Spring.MindStone.web.dto.diaryDto;

import Spring.MindStone.domain.diary.DailyDiary;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

//달력 요청 했을떄 ResponseDTO
@Getter
@Setter
@AllArgsConstructor
public class DiaryCallendarDTO {
    @Schema(description = "인덱스로 쓰기 편하게 일만 추출해서 보내용", example = "50.0")
    int dateIndex;

    Long diaryId;

    String emotion;

    public DiaryCallendarDTO(DailyDiary dailyDiary) {
        dateIndex = dailyDiary.getDate().getDayOfMonth();
        diaryId = dailyDiary.getId();
        emotion = dailyDiary.getEmotion().toString();
    }
}
