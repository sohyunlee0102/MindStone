package Spring.MindStone.service.DiaryService;

import Spring.MindStone.web.dto.diaryDto.DiaryCallendarDTO;
import Spring.MindStone.web.dto.diaryDto.DiaryResponseDTO;
import Spring.MindStone.web.dto.diaryDto.SimpleDiaryDTO;

import java.time.LocalDate;
import java.util.List;

public interface DiaryQueryService {
    /** 특정 날짜 일기 호출 **/
    SimpleDiaryDTO getDiaryByDate(Long id, LocalDate date);

    /** 일기 아이디 기준 호출 **/
    SimpleDiaryDTO getDiaryById(Long memberId, Long diaryId);

    /** 특정 월 달력 호출**/
    List<DiaryCallendarDTO> getDiaryCalendar(int year, int month, Long memberId);
}
