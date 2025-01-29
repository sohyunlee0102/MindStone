package Spring.MindStone.service.DiaryService;

import Spring.MindStone.web.dto.diaryDto.*;
import Spring.MindStone.domain.member.MemberInfo;
import Spring.MindStone.web.dto.diaryDto.DiaryResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface DiaryCommandService {
    /** 일기 자동생성 **/
    DiaryResponseDTO.DiaryCreationResponseDTO createDiary(Long id, LocalDate date);

    /** 일기 재생성 **/
    DiaryResponseDTO.DiaryCreationResponseDTO createDiaryRe(Long id, String bodyPart, LocalDate date);

    /** 일기 수정 **/
    SimpleDiaryDTO updateDiary(DiaryUpdateDTO updateDTO, Long memberId, List<MultipartFile> image);

    /** 일기 저장 **/
    SimpleDiaryDTO saveDiary(DiarySaveDTO saveDTO, Long memberId, List<MultipartFile> imageList);

    /** 일기 삭제 **/
    SimpleDiaryDTO deleteDiary(Long id, Long memberId);
}
