package Spring.MindStone.repository.habitRepository;

import Spring.MindStone.domain.habit.Habit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HabitRepository extends JpaRepository<Habit, Long> {

    List<Habit> findByMemberInfoId(Long memberId);

    // 특정 날짜까지 존재하는 전체 습관 개수 조회 (추가된 습관 반영)
    @Query("SELECT COUNT(h) FROM Habit h " +
            "WHERE h.memberInfo.id = :memberId " +
            "AND h.createdAt <= :date")
    int countTotalHabitsByDate(@Param("memberId") Long memberId,
                               @Param("date") LocalDate date);

    // 특정 월에 존재한 전체 습관 개수 조회
    @Query("SELECT COUNT(h) FROM Habit h " +
            "WHERE h.memberInfo.id = :memberId " +
            "AND FUNCTION('YEAR', h.createdAt) = :year " +
            "AND FUNCTION('MONTH', h.createdAt) = :month")
    int countTotalHabitsByMonth(@Param("memberId") Long memberId,
                                @Param("year") int year,
                                @Param("month") int month);
}
