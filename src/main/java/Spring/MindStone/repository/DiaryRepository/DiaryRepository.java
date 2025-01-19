package Spring.MindStone.repository.DiaryRepository;

import Spring.MindStone.domain.DailyDiary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<DailyDiary, Long> {
    DailyDiary findById(long id);
}
