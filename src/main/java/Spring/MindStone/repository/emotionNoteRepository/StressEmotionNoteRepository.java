package Spring.MindStone.repository.emotionNoteRepository;

import Spring.MindStone.domain.emotion.EmotionNote;
import Spring.MindStone.domain.emotion.StressEmotionNote;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StressEmotionNoteRepository extends JpaRepository<StressEmotionNote, Long> {
    List<StressEmotionNote> findByIdAndCreatedAtBetween(Long id, LocalDateTime startOfDay, LocalDateTime endOfDay, Sort sort);
}
