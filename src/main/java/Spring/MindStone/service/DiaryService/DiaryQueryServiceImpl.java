package Spring.MindStone.service.DiaryService;

import Spring.MindStone.apiPayload.code.status.ErrorStatus;
import Spring.MindStone.apiPayload.exception.handler.MemberInfoHandler;
import Spring.MindStone.domain.diary.DailyDiary;
import Spring.MindStone.repository.DiaryRepository.DiaryRepository;
import Spring.MindStone.web.dto.diaryDto.DiaryResponseDTO;
import Spring.MindStone.web.dto.diaryDto.SimpleDiaryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DiaryQueryServiceImpl implements DiaryQueryService {

    private final DiaryRepository diaryRepository;

    @Override
    public SimpleDiaryDTO getDiaryByDate(Long id, LocalDate date) {
        DailyDiary diary = diaryRepository.findDailyDiaryByDate(id,date)
                .orElseThrow(() ->new MemberInfoHandler(ErrorStatus.DIARY_NOT_FOUND));
        return new SimpleDiaryDTO(diary);
    }
}
