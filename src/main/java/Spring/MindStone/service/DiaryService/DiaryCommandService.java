package Spring.MindStone.service.DiaryService;

import Spring.MindStone.web.dto.DiaryResponseDTO;

import java.time.LocalDate;

public interface DiaryCommandService {
    //gpt를 호출하여 일기를 자동생성
    DiaryResponseDTO.DiaryCreationResponseDTO createDiary(Long id, LocalDate date);

    //String createDiaryHard(Long id,String bodyPart, LocalDate date);

    DiaryResponseDTO.DiaryCreationResponseDTO createDiaryRe(Long id, String bodyPart, LocalDate date);

    //저장하고 싶은 일기를 호출하여 저장

}
