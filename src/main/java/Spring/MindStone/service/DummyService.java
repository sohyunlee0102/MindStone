package Spring.MindStone.service;

import Spring.MindStone.domain.diary.DailyEmotionStatistic;
import Spring.MindStone.domain.member.MemberInfo;
import Spring.MindStone.repository.diaryRepository.DailyEmotionStatisticRepository;
import Spring.MindStone.service.memberInfoService.MemberInfoService;
import Spring.MindStone.web.dto.dummyDto.DummyEmotionStatistic;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DummyService {

    private final MemberInfoService memberInfoService;
    private final DailyEmotionStatisticRepository dailyEmotionStatisticRepository;

    @Transactional
    public void dummyStatistic(Long memberId, DummyEmotionStatistic request) {
        MemberInfo memberInfo = memberInfoService.findMemberById(memberId);
        DailyEmotionStatistic dailyEmotionStatistic = DailyEmotionStatistic
                .builder().date(request.getDate())
                .memberInfo(memberInfo)
                .angerFigure(request.getAngerFigure())
                .depressionFigure(request.getDepressionFigure())
                .calmFigure(request.getCalmFigure())
                .joyFigure(request.getJoyFigure())
                .sadFigure(request.getSadFigure())
                .thrillFigure(request.getThrillFigure())
                .happinessFigure(request.getHappinessFigure())
                .actionRecommandCount(10)
                .diaryAutoCreationCount(10)
                .build();

        dailyEmotionStatisticRepository.save(dailyEmotionStatistic);
    }
}
