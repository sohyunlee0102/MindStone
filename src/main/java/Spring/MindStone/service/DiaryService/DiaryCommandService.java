package Spring.MindStone.service.DiaryService;

import Spring.MindStone.web.dto.DiaryResponseDTO;

import java.time.LocalDate;

public interface DiaryCommandService {
    /** 일기 자동생성 **/
    DiaryResponseDTO.DiaryCreationResponseDTO createDiary(Long id, LocalDate date);

    /** 일기 재생성 **/
    DiaryResponseDTO.DiaryCreationResponseDTO createDiaryRe(Long id, String bodyPart, LocalDate date);


}
