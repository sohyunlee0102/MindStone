package Spring.MindStone.service.habitService;

import Spring.MindStone.apiPayload.code.status.ErrorStatus;
import Spring.MindStone.apiPayload.exception.handler.HabitHandler;
import Spring.MindStone.domain.habit.Habit;
import Spring.MindStone.domain.habit.HabitHistory;
import Spring.MindStone.domain.member.MemberInfo;
import Spring.MindStone.repository.habitRepository.HabitHistoryRepository;
import Spring.MindStone.service.memberInfoService.MemberInfoService;
import Spring.MindStone.web.dto.habitDto.HabitRequestDto;
import Spring.MindStone.web.dto.habitDto.HabitResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HabitHistoryService {

    private final HabitHistoryRepository habitHistoryRepository;
    private final MemberInfoService memberInfoService;
    private final HabitService habitService;

    public HabitResponseDto.GetLastHabitHistoryDTO getLastHabitHistory(Long memberId) {
        MemberInfo memberInfo = memberInfoService.findMemberById(memberId);

        HabitHistory habitHistory = habitHistoryRepository.findFirstByMemberInfoOrderByStartTimeDesc(memberInfo)
                .orElseThrow(() -> new HabitHandler(ErrorStatus.HABIT_HISTORY_NOT_FOUND));

        LocalDateTime lastHabitDate = habitHistory.getStartTime();
        LocalDate lastHabitLocalDate = lastHabitDate.toLocalDate();
        LocalDate today = LocalDate.now();

        if (lastHabitLocalDate.isEqual(today)) {
            return null;
        }

        long daysSinceLastHabit = ChronoUnit.DAYS.between(lastHabitLocalDate, today);

        return new HabitResponseDto.GetLastHabitHistoryDTO(daysSinceLastHabit);
    }

    public List<HabitResponseDto.HabitHistoryDTO> getHabitsForDate(Long memberId, LocalDate date) {
        MemberInfo memberInfo = memberInfoService.findMemberById(memberId);

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        List<HabitHistory> habitHistories = habitHistoryRepository.findByMemberInfoAndStartTimeBetween(memberInfo, startOfDay, endOfDay);

        return habitHistories.stream()
                .map(habitHistory -> new HabitResponseDto.HabitHistoryDTO(
                        habitHistory.getComment(),
                        habitHistory.getStartTime(),
                        habitHistory.getEndTime(),
                        habitHistory.getHabitHistoryColor()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public Long addHabitHistory(Long memberId, HabitRequestDto.CreateHabitHistoryDto request) {
        MemberInfo memberInfo = memberInfoService.findMemberById(memberId);
        Habit habit = habitService.findHabitById(request.getHabitId());

        HabitHistory habitHistory = HabitHistory.builder()
                .memberInfo(memberInfo)
                .habit(habit)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .comment(request.getComment())
                .habitHistoryColor(request.getHabitColor())
                .build();

        return habitHistoryRepository.save(habitHistory).getId();
    }

    @Transactional
    public Long updateHabitHistory(HabitRequestDto.UpdateHabitHistoryDto request) {
        HabitHistory habitHistory = habitHistoryRepository.findById(request.getHabitHistoryId())
                .orElseThrow(() -> new HabitHandler(ErrorStatus.HABIT_HISTORY_NOT_FOUND));

        if (request.getComment() != null) {
            habitHistory.setComment(request.getComment());
        }

        if (request.getStartTime() != null) {
            habitHistory.setStartTime(request.getStartTime());
        }

        if (request.getEndTime() != null) {
            habitHistory.setEndTime(request.getEndTime());
        }

        if (request.getHabitColor() != null) {
            habitHistory.setHabitHistoryColor(request.getHabitColor());
        }

        return habitHistoryRepository.save(habitHistory).getId();
    }
}
