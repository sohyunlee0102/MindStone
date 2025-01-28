package Spring.MindStone.service.DiaryService;

import Spring.MindStone.web.dto.diaryDto.DiaryResponseDTO;

import java.time.LocalDate;

public interface DiaryQueryService {
    /** 특정 날짜 일기 호출 **/
    DiaryResponseDTO.DiaryGetResponseDTO getDiaryByDate(Long id, LocalDate date);
}
