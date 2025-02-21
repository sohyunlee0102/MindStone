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
import java.time.ZoneId;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class  DailyEmotionStatisticService {
    private final DailyEmotionStatisticRepository dailyEmotionStatisticRepository;
    private final MemberInfoService memberInfoService;


    public DailyEmotionStatistic saveStatistics(MemberInfo memberInfo, LocalDate date, EmotionList emotion, int figure) {
        DailyEmotionStatistic statistics = dailyEmotionStatisticRepository.findFirstByDateAndMemberInfo(LocalDate.now(ZoneId.of("Asia/Seoul")),memberInfo)
                .orElseGet(() -> new DailyEmotionStatistic(memberInfo, LocalDate.now(ZoneId.of("Asia/Seoul")))); // 없으면 생성

        //여기서 감정들에 추가되는 수치만큼 더해줌.
        statistics.updateEmotion(emotion, figure);

        return dailyEmotionStatisticRepository.save(statistics); // 저장 후 반환
    }

    public SimpleEmotionStatisticDto getStatistic(Long memberId) {
        MemberInfo memberInfo = memberInfoService.findMemberById(memberId);
        DailyEmotionStatistic statistics = dailyEmotionStatisticRepository.findFirstByDateAndMemberInfo(LocalDate.now(ZoneId.of("Asia/Seoul")),memberInfo)
                .orElseGet(() ->dailyEmotionStatisticRepository.save(new DailyEmotionStatistic(memberInfo, LocalDate.now(ZoneId.of("Asia/Seoul")))) );

        return new SimpleEmotionStatisticDto(statistics);
    }

    public DailyEmotionStatistic getStatisticEntity(Long memberId) {
        MemberInfo memberInfo = memberInfoService.findMemberById(memberId);
        DailyEmotionStatistic statistics = dailyEmotionStatisticRepository.findFirstByDateAndMemberInfo(LocalDate.now(ZoneId.of("Asia/Seoul")),memberInfo)
                .orElseGet(() ->dailyEmotionStatisticRepository.save(new DailyEmotionStatistic(memberInfo, LocalDate.now(ZoneId.of("Asia/Seoul")))) );
        return statistics;
    }

    public void updateStatistic(DailyEmotionStatistic statistics) {
        dailyEmotionStatisticRepository.save(statistics);
    }
}
