package Spring.MindStone.service.habitService;

import Spring.MindStone.apiPayload.code.status.ErrorStatus;
import Spring.MindStone.apiPayload.exception.handler.HabitHandler;
import Spring.MindStone.domain.habit.Habit;
import Spring.MindStone.domain.habit.HabitExecution;
import Spring.MindStone.domain.habit.HabitHistory;
import Spring.MindStone.domain.member.MemberInfo;
import Spring.MindStone.repository.habitRepository.HabitExecutionRepository;
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
    private final HabitExecutionRepository habitExecutionRepository;

    public HabitResponseDto.GetLastHabitHistoryDTO getLastHabitHistory(Long memberId) {
        MemberInfo memberInfo = memberInfoService.findMemberById(memberId);

        HabitExecution lastExecution = habitExecutionRepository.findFirstByMemberInfoOrderByStartTimeDesc(memberInfo)
                .orElseThrow(() -> new HabitHandler(ErrorStatus.HABIT_HISTORY_NOT_FOUND));

        LocalDateTime lastExecutionTime = lastExecution.getStartTime();
        LocalDate laseExecutionDate = lastExecutionTime.toLocalDate();
        LocalDate today = LocalDate.now();

        if (laseExecutionDate.isEqual(today)) {
            return null;
        }

        long daysSinceLastExecution = ChronoUnit.DAYS.between(laseExecutionDate, today);

        return new HabitResponseDto.GetLastHabitHistoryDTO(daysSinceLastExecution);
    }

    public List<HabitResponseDto.HabitHistoryWithExecutionDTO> getHabitsForDate(Long memberId, LocalDate date) {
        MemberInfo memberInfo = memberInfoService.findMemberById(memberId);

        List<HabitHistory> habitHistories = habitHistoryRepository.findByMemberInfoAndDate(memberInfo, date);

        return habitHistories.stream()
                .map(habitHistory -> new HabitResponseDto.HabitHistoryWithExecutionDTO(
                        habitHistory.getHabit().getId(),
                        habitHistory.getId(),
                        habitHistory.getComment(),
                        habitHistory.getHabitHistoryColor(),
                        habitHistory.getExecutions().stream().map(execution -> new HabitResponseDto.HabitExecutionDTO(
                                execution.getId(),
                                execution.getStartTime(),
                                execution.getEndTime()
                        )).collect(Collectors.toList())
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
                .comment(request.getComment())
                .habitHistoryColor(request.getHabitColor())
                .date(request.getDate())
                .build();

        habitHistoryRepository.save(habitHistory);

        if (request.getStartTime() != null && request.getEndTime() != null) {
            HabitExecution habitExecution = HabitExecution.builder()
                    .habitHistory(habitHistory)
                    .startTime(request.getStartTime())
                    .endTime(request.getEndTime())
                    .memberInfo(memberInfo)
                    .build();

            habitExecutionRepository.save(habitExecution);
        }

        return habitHistory.getId();
    }

    @Transactional
    public void updateHabitHistory(HabitRequestDto.UpdateHabitHistoryDto request) {

        if (request.getHabitHistoryId() != null) {
            HabitHistory habitHistory = habitHistoryRepository.findById(request.getHabitHistoryId())
                    .orElseThrow(() -> new HabitHandler(ErrorStatus.HABIT_HISTORY_NOT_FOUND));

            if (request.getComment() != null) {
                habitHistory.setComment(request.getComment());
            }

            if (request.getHabitColor() != null) {
                habitHistory.setHabitHistoryColor(request.getHabitColor());
            }

            habitHistoryRepository.save(habitHistory);
        }

        if (request.getExecutionId() != null) {
            HabitExecution execution = habitExecutionRepository.findById(request.getExecutionId())
                    .orElseThrow(() -> new HabitHandler(ErrorStatus.HABIT_EXECUTION_NOT_FOUND));

            if (request.getStartTime() != null && request.getEndTime() != null) {
                execution.setStartTime(request.getStartTime());
                execution.setEndTime(request.getEndTime());
            }

            habitExecutionRepository.save(execution);
        }

    }

    @Transactional
    public Long addHabitExecution(Long memberId, HabitRequestDto.CreateHabitExecutionDto request) {
        MemberInfo memberInfo = memberInfoService.findMemberById(memberId);

        HabitHistory habitHistory = habitHistoryRepository.findById(request.getHabitHistoryId())
                .orElseThrow(() -> new HabitHandler(ErrorStatus.HABIT_HISTORY_NOT_FOUND));

        List<HabitExecution> existingExecutions = habitExecutionRepository.findByHabitHistory(habitHistory);
        for (HabitExecution execution : existingExecutions) {
            boolean isOverlapping = request.getStartTime().isBefore(execution.getEndTime())
                    && request.getEndTime().isAfter(execution.getStartTime());

            if (isOverlapping) {
                throw new HabitHandler(ErrorStatus.TIME_RANGE_CONFLICT);
            }
        }

        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new HabitHandler(ErrorStatus.INVALID_TIME_RANGE);
        }

        HabitExecution habitExecution = HabitExecution.builder()
                .habitHistory(habitHistory)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .memberInfo(memberInfo)
                .build();

        habitHistory.getExecutions().add(habitExecution);
        return habitHistoryRepository.save(habitHistory).getId();
    }

}
