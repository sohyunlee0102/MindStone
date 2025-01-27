package Spring.MindStone.repository.DiaryRepository;

import Spring.MindStone.domain.diary.DailyDiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiaryRepository extends JpaRepository<DailyDiary, Long> {
    @Query(value = "SELECT * FROM daily_diary " +
            "WHERE date = :date " +
            "AND member_id != :memberId",
            nativeQuery = true)
    Optional<DailyDiary> findDailyDiaryByDate(@Param("memberId") Long memberId,
                                              @Param("date") LocalDate date);

}
