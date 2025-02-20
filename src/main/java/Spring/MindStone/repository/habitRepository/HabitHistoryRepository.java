package Spring.MindStone.repository.habitRepository;

import Spring.MindStone.domain.habit.HabitHistory;
import Spring.MindStone.domain.member.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HabitHistoryRepository extends JpaRepository<HabitHistory, Long> {

    List<HabitHistory> findByMemberInfoAndDate(MemberInfo memberInfo, LocalDate date);

    /*
    // 특정 날짜에 사용자가 기록한(달성한) 습관 개수 조회
    @Query("SELECT COUNT(hh) FROM HabitHistory hh " +
            "WHERE hh.memberInfo.id = :memberId " +
            "AND DATE(hh.startTime) = :date")
    int countCompletedHabitsByDate(@Param("memberId") Long memberId,
                                   @Param("date") LocalDate date);


    // 특정 월에 사용자가 기록한(달성한) 습관 개수 조회
    @Query("SELECT COUNT(hh) FROM HabitHistory hh " +
            "WHERE hh.memberInfo.id = :memberId " +
            "AND YEAR(hh.startTime) = :year " +
            "AND MONTH(hh.startTime) = :month")
    int countCompletedHabitsByMonth(@Param("memberId") Long memberId,
                                    @Param("year") int year,
                                    @Param("month") int month);

    // 특정 주별 습관 달성 횟수 조회 (리포트)
    @Query(value = "SELECT WEEK(hh.start_time), hh.habit_id, COUNT(*) " +
            "FROM habit_history hh " +
            "WHERE hh.member_id = :memberId " +
            "AND YEAR(hh.start_time) = :year " +
            "AND MONTH(hh.start_time) = :month " +
            "GROUP BY WEEK(hh.start_time), hh.habit_id",
            nativeQuery = true)
    List<Object[]> getWeeklyHabitCounts(@Param("memberId") Long memberId,
                                        @Param("year") int year,
                                        @Param("month") int month);


    // 특정 주별 활동 시간 조회 (리포트)
    @Query(value = "SELECT WEEK(hh.start_time), hh.habit_id, SUM(TIMESTAMPDIFF(MINUTE, hh.start_time, COALESCE(hh.end_time, hh.start_time))) " +
            "FROM habit_history hh " +
            "WHERE hh.member_id = :memberId " +
            "AND YEAR(hh.start_time) = :year " +
            "AND MONTH(hh.start_time) = :month " +
            "GROUP BY WEEK(hh.start_time), hh.habit_id",
            nativeQuery = true)
    List<Object[]> getWeeklyActiveTime(@Param("memberId") Long memberId,
                                       @Param("year") int year,
                                       @Param("month") int month);


    // 달성률 증가율 계산 (리포트)
    @Query(value = "SELECT (endRate - startRate) * 100.0 / NULLIF(startRate, 0) " +
            "FROM (SELECT " +
            "(SELECT COUNT(DISTINCT DATE(hh1.start_time)) FROM habit_history hh1 " +
            "WHERE hh1.member_id = :memberId AND YEAR(hh1.start_time) = :year AND MONTH(hh1.start_time) = :month AND DAY(hh1.start_time) <= 10) AS startRate, " +
            "(SELECT COUNT(DISTINCT DATE(hh2.start_time)) FROM habit_history hh2 " +
            "WHERE hh2.member_id = :memberId AND YEAR(hh2.start_time) = :year AND MONTH(hh2.start_time) = :month AND DAY(hh2.start_time) >= 20) AS endRate) AS growth",
            nativeQuery = true)
    Double calculateAchievementGrowth(@Param("memberId") Long memberId,
                                      @Param("year") int year,
                                      @Param("month") int month);

    // 가장 많이 기록한(달성한) 습관 조회 (리포트)
    @Query(value = "SELECT hh.habit_id FROM habit_history hh " +
            "WHERE hh.member_id = :memberId " +
            "AND YEAR(hh.start_time) = :year " +
            "AND MONTH(hh.start_time) = :month " +
            "GROUP BY hh.habit_id " +
            "ORDER BY COUNT(*) DESC LIMIT 1",
            nativeQuery = true)
    Long findMostCompletedHabit(@Param("memberId") Long memberId,
                                @Param("year") int year,
                                @Param("month") int month);
     */

    // 특정 날짜에 사용자가 기록한(달성한) 습관 개수 조회
    @Query("SELECT COUNT(hh) FROM HabitHistory hh " +
            "WHERE hh.memberInfo.id = :memberId " +
            "AND DATE(hh.createdAt) = :date")
    int countCompletedHabitsByDate(@Param("memberId") Long memberId,
                                   @Param("date") LocalDate date);

    // 특정 월에 사용자가 기록한(달성한) 습관 개수 조회
    @Query("SELECT COUNT(hh) FROM HabitHistory hh " +
            "WHERE hh.memberInfo.id = :memberId " +
            "AND YEAR(hh.createdAt) = :year " +
            "AND MONTH(hh.createdAt) = :month")
    int countCompletedHabitsByMonth(@Param("memberId") Long memberId,
                                    @Param("year") int year,
                                    @Param("month") int month);

    // 특정 주별 습관 달성 횟수 조회 (리포트)
    @Query(value = "SELECT WEEK(hh.created_at), hh.habit_id, COUNT(*) " +
            "FROM habit_history hh " +
            "WHERE hh.member_id = :memberId " +
            "AND YEAR(hh.created_at) = :year " +
            "AND MONTH(hh.created_at) = :month " +
            "GROUP BY WEEK(hh.created_at), hh.habit_id",
            nativeQuery = true)
    List<Object[]> getWeeklyHabitCounts(@Param("memberId") Long memberId,
                                        @Param("year") int year,
                                        @Param("month") int month);

    // 특정 주별 활동 시간 조회 (리포트)
    @Query(value = "SELECT WEEK(he.start_time), hh.habit_id, SUM(TIMESTAMPDIFF(MINUTE, he.start_time, COALESCE(he.end_time, he.start_time))) " +
            "FROM habit_history hh " +
            "JOIN habit_execution he ON hh.id = he.habit_history_id " +
            "WHERE hh.member_id = :memberId " +
            "AND YEAR(he.start_time) = :year " +
            "AND MONTH(he.start_time) = :month " +
            "GROUP BY WEEK(he.start_time), hh.habit_id",
            nativeQuery = true)
    List<Object[]> getWeeklyActiveTime(@Param("memberId") Long memberId,
                                       @Param("year") int year,
                                       @Param("month") int month);

    // 달성률 증가율 계산 (리포트)
    @Query(value = "SELECT (endRate - startRate) * 100.0 / NULLIF(startRate, 0) " +
            "FROM (SELECT " +
            "(SELECT COUNT(DISTINCT DATE(hh1.created_at)) FROM habit_history hh1 " +
            "WHERE hh1.member_id = :memberId AND YEAR(hh1.created_at) = :year AND MONTH(hh1.created_at) = :month AND DAY(hh1.created_at) <= 10) AS startRate, " +
            "(SELECT COUNT(DISTINCT DATE(hh2.created_at)) FROM habit_history hh2 " +
            "WHERE hh2.member_id = :memberId AND YEAR(hh2.created_at) = :year AND MONTH(hh2.created_at) = :month AND DAY(hh2.created_at) >= 20) AS endRate) AS growth",
            nativeQuery = true)
    Double calculateAchievementGrowth(@Param("memberId") Long memberId,
                                      @Param("year") int year,
                                      @Param("month") int month);

    // 가장 많이 기록한(달성한) 습관 조회 (리포트)
    @Query(value = "SELECT hh.habit_id FROM habit_history hh " +
            "WHERE hh.member_id = :memberId " +
            "AND YEAR(hh.created_at) = :year " +
            "AND MONTH(hh.created_at) = :month " +
            "GROUP BY hh.habit_id " +
            "ORDER BY COUNT(*) DESC LIMIT 1",
            nativeQuery = true)
    Long findMostCompletedHabit(@Param("memberId") Long memberId,
                                @Param("year") int year,
                                @Param("month") int month);

}
