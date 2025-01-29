package Spring.MindStone.repository.habitRepository;

import Spring.MindStone.domain.habit.Habit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HabitRepository extends JpaRepository<Habit, Long> {

    List<Habit> findByMemberInfoId(Long memberId);

}
