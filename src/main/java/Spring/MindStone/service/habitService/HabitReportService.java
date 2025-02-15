package Spring.MindStone.service.habitService;

import Spring.MindStone.repository.habitRepository.HabitHistoryRepository;
import Spring.MindStone.web.dto.habitReportDto.HabitReportResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HabitReportService {

    private final HabitHistoryRepository habitHistoryRepository;

    @Transactional
    public HabitReportResponseDto getHabitReport(Long memberId, int year, int month) {
        // 기록 비율 계산
        int totalRecordedDays = habitHistoryRepository.countCompletedHabitsByMonth(memberId, year, month);
        int totalDaysInMonth = 30; // TODO: 실제 월별 일수 반영 가능

        double recordPercentage = (totalDaysInMonth > 0) ? ((double) totalRecordedDays / totalDaysInMonth) * 100 : 0.0;

        // 달성률 증가율 계산
        Double achievementGrowth = habitHistoryRepository.calculateAchievementGrowth(memberId, year, month);
        if (achievementGrowth == null) achievementGrowth = 0.0;

        // 가장 많이 기록된 습관 조회
        Long topHabitId = habitHistoryRepository.findMostCompletedHabit(memberId, year, month);
        String topHabit = (topHabitId != null) ? "습관 ID: " + topHabitId : "없음";

        // 주별 달성률
        List<Object[]> weeklyAchievementData = habitHistoryRepository.getWeeklyHabitCounts(memberId, year, month);
        List<HabitReportResponseDto.WeeklyData> weeklyAchievementRates = weeklyAchievementData.stream()
                .map(data -> new HabitReportResponseDto.WeeklyData((int) data[0], (long) data[1], (long) data[2]))
                .collect(Collectors.toList());

        // 주별 활동 시간
        List<Object[]> weeklyActiveTimeData = habitHistoryRepository.getWeeklyActiveTime(memberId, year, month);
        List<HabitReportResponseDto.WeeklyData> weeklyActiveTime = weeklyActiveTimeData.stream()
                .map(data -> new HabitReportResponseDto.WeeklyData((int) data[0], (long) data[1], (long) data[2]))
                .collect(Collectors.toList());

        return new HabitReportResponseDto(recordPercentage, achievementGrowth, topHabit, weeklyAchievementRates, weeklyActiveTime);
    }
}
