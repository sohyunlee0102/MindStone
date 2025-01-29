package Spring.MindStone.repository.DiaryRepository;

import Spring.MindStone.domain.diary.DailyDiary;
import Spring.MindStone.domain.diary.DiaryImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryImageRepository extends JpaRepository<DiaryImage, Long> {
}
