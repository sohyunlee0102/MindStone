package Spring.MindStone.repository.diaryRepository;

import Spring.MindStone.domain.diary.DailyEmotionStatistic;
import Spring.MindStone.domain.diary.DiaryImage;
import Spring.MindStone.domain.member.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyEmotionStatisticRepository extends JpaRepository<DailyEmotionStatistic, Long> {
    Optional<Object> findByDateAndMemberInfo(LocalDate date, MemberInfo memberInfo);
}
