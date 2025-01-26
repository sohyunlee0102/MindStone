package Spring.MindStone.repository.DiaryRepository;

import Spring.MindStone.domain.DailyDiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryRepository extends JpaRepository<DailyDiary, Long> {

}
