package Spring.MindStone.service.habitService;

import Spring.MindStone.domain.member.MemberInfo;
import Spring.MindStone.repository.habitRepository.HabitHistoryRepository;
import Spring.MindStone.repository.habitRepository.HabitRepository;
import Spring.MindStone.repository.memberRepository.MemberInfoRepository;
import Spring.MindStone.web.dto.habitCalendarDto.HabitCalendarResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitCalendarService {

    private final HabitHistoryRepository habitHistoryRepository;
    private final HabitRepository habitRepository;
    private final MemberInfoRepository memberInfoRepository;

    public HabitCalendarResponseDto getCalendarData(Long memberId, int year, int month) {
        MemberInfo memberInfo = memberInfoRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        int totalDaysInMonth = YearMonth.of(year, month).lengthOfMonth();
        int recordedDays = 0;
        int fullAchievementDays = 0;

        List<HabitCalendarResponseDto.DailyRecord> dailyRecords = new ArrayList<>();

        for (int day = 1; day <= totalDaysInMonth; day++) {
            LocalDate currentDate = LocalDate.of(year, month, day);

            int completedHabits = habitHistoryRepository.countCompletedHabitsByDate(memberId, year, month, day);
            int totalHabits = habitRepository.countTotalHabitsByDate(memberId, currentDate);

            if (completedHabits > 0) {
                recordedDays++;
            }

            if (totalHabits > 0 && completedHabits == totalHabits) {
                fullAchievementDays++;
            }

            dailyRecords.add(
                    HabitCalendarResponseDto.DailyRecord.builder()
                            .day(day)
                            .completedHabits(completedHabits)
                            .totalHabits(totalHabits)
                            .build()
            );
        }

        double recordPercentage = (totalDaysInMonth == 0) ? 0 : (recordedDays * 100.0) / totalDaysInMonth;

        return HabitCalendarResponseDto.builder()
                .recordPercentage(recordPercentage)
                .fullAchievementCount(fullAchievementDays)
                .dailyRecords(dailyRecords)
                .build();
    }
}
