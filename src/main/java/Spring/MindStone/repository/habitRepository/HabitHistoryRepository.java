package Spring.MindStone.repository.habitRepository;

import Spring.MindStone.domain.habit.HabitHistory;
import Spring.MindStone.domain.member.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface HabitHistoryRepository extends JpaRepository<HabitHistory, Long> {
    Optional<HabitHistory> findFirstByMemberInfoOrderByStartTimeDesc(MemberInfo memberInfo);
    List<HabitHistory> findByMemberInfoAndStartTimeBetween(MemberInfo memberInfo, LocalDateTime startDate, LocalDateTime endDate);
}
