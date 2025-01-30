package Spring.MindStone.repository.diaryRepository;

import Spring.MindStone.domain.diary.DiaryImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryImageRepository extends JpaRepository<DiaryImage, Long> {
}
