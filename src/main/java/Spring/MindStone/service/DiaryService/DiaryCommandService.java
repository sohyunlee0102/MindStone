package Spring.MindStone.service.DiaryService;

import Spring.MindStone.web.dto.diaryDto.DiaryResponseDTO;
import Spring.MindStone.domain.member.MemberInfo;
import Spring.MindStone.web.dto.diaryDto.DiaryResponseDTO;
import Spring.MindStone.web.dto.diaryDto.DiarySaveDTO;
import Spring.MindStone.web.dto.diaryDto.SimpleDiaryDTO;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public interface DiaryCommandService {
    /** 일기 자동생성 **/
    DiaryResponseDTO.DiaryCreationResponseDTO createDiary(Long id, LocalDate date);

    /** 일기 재생성 **/
    DiaryResponseDTO.DiaryCreationResponseDTO createDiaryRe(Long id, String bodyPart, LocalDate date);

    /** 일기 수정 **/


    /** 일기 저장 **/
    SimpleDiaryDTO saveDiary(DiarySaveDTO saveDTO, Long memberId, MultipartFile image);

}
