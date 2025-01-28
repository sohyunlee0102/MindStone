package Spring.MindStone.service.DiaryService;

import Spring.MindStone.apiPayload.code.status.ErrorStatus;
import Spring.MindStone.apiPayload.exception.handler.MemberInfoHandler;
import Spring.MindStone.domain.diary.DailyDiary;
import Spring.MindStone.repository.DiaryRepository.DiaryRepository;
import Spring.MindStone.web.dto.diaryDto.DiaryResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DiaryQueryServiceImpl implements DiaryQueryService {

    private final DiaryRepository diaryRepository;

    @Override
    public DiaryResponseDTO.DiaryGetResponseDTO getDiaryByDate(Long id, LocalDate date) {
        DailyDiary diary = diaryRepository.findDailyDiaryByDate(id,date)
                .orElseThrow(() ->new MemberInfoHandler(ErrorStatus.DIARY_NOT_FOUND));
        return DiaryResponseDTO.DiaryGetResponseDTO.builder()
                .id(diary.getId())
                .date(diary.getDate())
                .memberId(diary.getMemberInfo().getId()) // MemberInfo에서 ID 추출
                .title(diary.getTitle())
                .content(diary.getContent())
                .imagePath(diary.getImagePath())
                .build();
    }
}
