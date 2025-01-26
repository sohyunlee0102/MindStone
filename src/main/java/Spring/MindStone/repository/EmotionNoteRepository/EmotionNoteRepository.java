package Spring.MindStone.repository.EmotionNoteRepository;

import Spring.MindStone.domain.EmotionNote;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface EmotionNoteRepository extends JpaRepository<EmotionNote, Long> {
    List<EmotionNote> findByIdAndCreatedAtBetween(Long id, LocalDateTime startOfDay, LocalDateTime endOfDay, Sort sort);
}
