package Spring.MindStone.repository.habitRepository;

import Spring.MindStone.domain.habit.HabitExecution;
import Spring.MindStone.domain.habit.HabitHistory;
import Spring.MindStone.domain.member.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HabitExecutionRepository extends JpaRepository<HabitExecution, Long> {

    Optional<HabitExecution> findFirstByMemberInfoOrderByStartTimeDesc(MemberInfo memberInfo);

    List<HabitExecution> findByHabitHistory(HabitHistory habitHistory);

}
