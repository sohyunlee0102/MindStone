package Spring.MindStone.repository.habitRepository;

import Spring.MindStone.domain.habit.HabitHistory;
import Spring.MindStone.domain.member.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface HabitHistoryRepository extends JpaRepository<HabitHistory, Long> {
    Optional<HabitHistory> findFirstByMemberInfoOrderByStartTimeDesc(MemberInfo memberInfo);
    List<HabitHistory> findByMemberInfoAndStartTimeBetween(MemberInfo memberInfo, LocalDateTime startDate, LocalDateTime endDate);

    // ✅ 특정 날짜에 사용자가 기록한(달성한) 습관 개수 조회
    @Query("SELECT COUNT(hh) FROM HabitHistory hh " +
            "WHERE hh.memberInfo.id = :memberId " +
            "AND FUNCTION('YEAR', hh.startTime) = :year " +
            "AND FUNCTION('MONTH', hh.startTime) = :month " +
            "AND FUNCTION('DAY', hh.startTime) = :day")
    int countCompletedHabitsByDate(@Param("memberId") Long memberId,
                                   @Param("year") int year,
                                   @Param("month") int month,
                                   @Param("day") int day);
}
