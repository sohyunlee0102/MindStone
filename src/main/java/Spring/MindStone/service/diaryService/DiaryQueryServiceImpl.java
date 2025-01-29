package Spring.MindStone.service.diaryService;

import Spring.MindStone.apiPayload.code.status.ErrorStatus;
import Spring.MindStone.apiPayload.exception.handler.MemberInfoHandler;
import Spring.MindStone.domain.diary.DailyDiary;
import Spring.MindStone.repository.diaryRepository.DiaryRepository;
import Spring.MindStone.repository.memberInfoRepository.MemberInfoRepository;
import Spring.MindStone.web.dto.diaryDto.DiaryCallendarDTO;
import Spring.MindStone.web.dto.diaryDto.SimpleDiaryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryQueryServiceImpl implements DiaryQueryService {
    private final MemberInfoRepository memberInfoRepository;
    private final DiaryRepository diaryRepository;

    @Override
    public SimpleDiaryDTO getDiaryByDate(Long memberId, LocalDate date) {
        DailyDiary diary = diaryRepository.findDailyDiaryByDate(memberId,date)
                .orElseThrow(() ->new MemberInfoHandler(ErrorStatus.DIARY_NOT_FOUND));
        return new SimpleDiaryDTO(diary);
    }

    public SimpleDiaryDTO getDiaryById(Long memberId, Long diaryId) {
        DailyDiary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() ->new MemberInfoHandler(ErrorStatus.DIARY_NOT_FOUND));
        if(!diary.getMemberInfo().getId().equals(memberId)){
            throw new MemberInfoHandler(ErrorStatus.DIARY_ISNT_MINE);
        }
        return new SimpleDiaryDTO(diary);
    }

    @Override
    public List<DiaryCallendarDTO> getDiaryCalendar(int year, int month, Long memberId) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<DailyDiary> diaryList = diaryRepository.findByMemberIdAndDateBetween(memberId, startDate, endDate);
        return diaryList.stream()
                .map(diary -> new DiaryCallendarDTO(diary))
                .collect(Collectors.toList());
    }

}
