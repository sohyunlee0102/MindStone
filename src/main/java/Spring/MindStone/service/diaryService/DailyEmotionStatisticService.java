package Spring.MindStone.service.diaryService;

import Spring.MindStone.domain.diary.DailyEmotionStatistic;
import Spring.MindStone.domain.enums.EmotionList;
import Spring.MindStone.domain.member.MemberInfo;
import Spring.MindStone.repository.diaryRepository.DailyEmotionStatisticRepository;
import Spring.MindStone.service.memberInfoService.MemberInfoService;
import Spring.MindStone.web.dto.emotionDto.SimpleEmotionStatisticDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class DailyEmotionStatisticService {
    private final DailyEmotionStatisticRepository dailyEmotionStatisticRepository;
    private final MemberInfoService memberInfoService;


    public DailyEmotionStatistic saveStatistics(MemberInfo memberInfo, LocalDate date, EmotionList emotion, int figure) {
        DailyEmotionStatistic statistics = (DailyEmotionStatistic) dailyEmotionStatisticRepository.findByDateAndMemberInfo(date,memberInfo)
                .orElseGet(() -> new DailyEmotionStatistic(memberInfo, date)); // 없으면 생성

        statistics.updateEmotion(emotion, figure);

        return dailyEmotionStatisticRepository.save(statistics); // 저장 후 반환
    }

    public SimpleEmotionStatisticDto getStatistic(Long memberId) {
        MemberInfo memberInfo = memberInfoService.findMemberById(memberId);
        DailyEmotionStatistic statistics = (DailyEmotionStatistic) dailyEmotionStatisticRepository.findByDateAndMemberInfo(LocalDate.now(),memberInfo)
                .orElseGet(() -> new DailyEmotionStatistic(memberInfo, LocalDate.now()));

        return new SimpleEmotionStatisticDto(statistics);
    }
}
