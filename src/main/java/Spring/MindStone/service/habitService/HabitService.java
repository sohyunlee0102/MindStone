package Spring.MindStone.service.habitService;

import Spring.MindStone.apiPayload.code.status.ErrorStatus;
import Spring.MindStone.apiPayload.exception.handler.HabitHandler;
import Spring.MindStone.apiPayload.exception.handler.MemberInfoHandler;
import Spring.MindStone.domain.habit.Habit;
import Spring.MindStone.domain.member.MemberInfo;
import Spring.MindStone.repository.habitRepository.HabitRepository;
import Spring.MindStone.repository.memberRepository.MemberInfoRepository;
import Spring.MindStone.web.dto.habitDto.HabitRequestDto;
import Spring.MindStone.web.dto.habitDto.HabitResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HabitService {

    private final HabitRepository habitRepository;
    private final MemberInfoRepository memberInfoRepository;

    @Transactional
    public Habit findHabitById(Long habitId) {
        return habitRepository.findById(habitId)
                .orElseThrow(() -> new HabitHandler(ErrorStatus.HABIT_NOT_FOUND));
    }

    @Transactional
    public List<HabitResponseDto.GetHabitDTO> getAllHabits(Long memberId) {
        List<Habit> habits = habitRepository.findByMemberInfoId(memberId);

        return habits.stream().map(habit ->
                new HabitResponseDto.GetHabitDTO(
                        habit.getId(),
                        habit.getTitle(),
                        habit.getDayOfWeek(),
                        habit.getAlarmTime(),
                        habit.getTargetTime(),
                        habit.getIsActive(),
                        habit.getHabitColor()
                )).collect(Collectors.toList());
    }

    @Transactional
    public HabitResponseDto.HabitDTO createHabit(HabitRequestDto.HabitDto request, Long memberId) {
        MemberInfo memberInfo = memberInfoRepository.findById(memberId).
                orElseThrow(() -> new MemberInfoHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Habit habit = Habit.builder()
                .memberInfo(memberInfo)
                .title(request.getTitle())
                .dayOfWeek(request.getDayOfWeek())
                .alarmTime(request.getAlarmTime())
                .targetTime(request.getTargetTime())
                .habitColor(request.getHabitColor())
                .build();

        habitRepository.save(habit);

        return new HabitResponseDto.HabitDTO(habit.getId(), LocalDateTime.now());
    }

    @Transactional
    public HabitResponseDto.HabitDTO updateHabit(HabitRequestDto.HabitUpdateDto request, Long memberId) {
        Habit habit = habitRepository.findById(request.getHabitId())
                .orElseThrow(() -> new HabitHandler(ErrorStatus.HABIT_NOT_FOUND));

        if (!habit.getMemberInfo().getId().equals(memberId)) {
            throw new MemberInfoHandler(ErrorStatus._FORBIDDEN); // 권한 없음
        }

        if (request.getTitle() != null) habit.setTitle(request.getTitle());
        if (request.getDayOfWeek() != null) habit.setDayOfWeek(request.getDayOfWeek());
        if (request.getAlarmTime() != null) habit.setAlarmTime(request.getAlarmTime());
        if (request.getTargetTime() != null) habit.setTargetTime(request.getTargetTime());
        if (request.getIsActive() != null) habit.setIsActive(request.getIsActive());
        if (request.getHabitColor() != null) habit.setHabitColor(request.getHabitColor());

        return new HabitResponseDto.HabitDTO(habit.getId(), LocalDateTime.now());
    }

    @Transactional
    public List<HabitResponseDto.GetHabitForNowDTO> getHabitsForNow(Long memberId) {
        LocalDateTime now = LocalDateTime.now();
        int todayIndex = now.getDayOfWeek().getValue() % 7;
        LocalTime currentTime = now.toLocalTime();

        List<Habit> habits = habitRepository.findByMemberInfoId(memberId);

        return habits.stream()
                .filter(habit -> isTodayHabit(habit.getDayOfWeek(), todayIndex) &&
                        isAlarmTimeValid(habit.getAlarmTime(), habit.getTargetTime(), currentTime))
                .map(habit -> new HabitResponseDto.GetHabitForNowDTO(
                        habit.getId(),
                        habit.getTitle(),
                        habit.getTargetTime()))
                .collect(Collectors.toList());
    }

    private boolean isTodayHabit(String dayOfWeek, int todayIndex) {
        return dayOfWeek.length() == 7 && dayOfWeek.charAt(todayIndex) == '1';
    }

    private boolean isAlarmTimeValid(String alarmTime, int targetTime, LocalTime currentTime) {
        if (alarmTime == null || alarmTime.isEmpty()) return false;

        String[] times = alarmTime.split("/");
        for (String time : times) {
            LocalTime habitTime = LocalTime.parse(time);
            LocalTime habitEndTime = habitTime.plusMinutes(targetTime);

            if (!currentTime.isBefore(habitTime) && currentTime.isBefore(habitEndTime)) {
                return true;
            }
        }
        return false;
    }


    // 리스트 형태로 여러 개의 습관 생성
    @Transactional
    public void createHabits(List<HabitRequestDto.HabitDto> habitDtos, MemberInfo memberInfo) {
        for (HabitRequestDto.HabitDto habitDto : habitDtos) {
            Habit habit = Habit.builder()
                    .memberInfo(memberInfo)
                    .title(habitDto.getTitle())
                    .dayOfWeek(habitDto.getDayOfWeek())
                    .alarmTime(habitDto.getAlarmTime())
                    .targetTime(habitDto.getTargetTime())
                    .isActive(habitDto.getIsActive())
                    .habitColor(habitDto.getHabitColor())
                    .build();

            habitRepository.save(habit);
        }
    }

}