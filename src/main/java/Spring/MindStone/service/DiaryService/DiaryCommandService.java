package Spring.MindStone.service.DiaryService;

import Spring.MindStone.web.dto.DiaryResponseDTO;

public interface DiaryCommandService {
    //gpt를 호출하여 일기를 자동생성
    String createDiary(Long id, String bodyPart);

    //저장하고 싶은 일기를 호출하여 저장

}
