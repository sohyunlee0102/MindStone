package Spring.MindStone.repository.diaryRepository;

import Spring.MindStone.domain.diary.DailyEmotionStatistic;
import Spring.MindStone.domain.diary.DiaryImage;
import Spring.MindStone.domain.member.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyEmotionStatisticRepository extends JpaRepository<DailyEmotionStatistic, Long> {
    Optional<DailyEmotionStatistic> findFirstByDateAndMemberInfo(LocalDate date, MemberInfo memberInfo);



    @Query("SELECT hh FROM DailyEmotionStatistic hh " +
            "WHERE hh.memberInfo.id = :memberId " +
            "AND YEAR(hh.createdAt) = :year " +
            "AND MONTH(hh.createdAt) = :month")
    List<DailyEmotionStatistic> findStatisticByDate(@Param("memberId") Long memberId,
                                @Param("year") int year,
                                @Param("month") int month);
}
