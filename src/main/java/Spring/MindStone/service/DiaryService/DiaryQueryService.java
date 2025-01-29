package Spring.MindStone.service.DiaryService;

import Spring.MindStone.web.dto.diaryDto.DiaryResponseDTO;
import Spring.MindStone.web.dto.diaryDto.SimpleDiaryDTO;

import java.time.LocalDate;

public interface DiaryQueryService {
    /** 특정 날짜 일기 호출 **/
    SimpleDiaryDTO getDiaryByDate(Long id, LocalDate date);
}
