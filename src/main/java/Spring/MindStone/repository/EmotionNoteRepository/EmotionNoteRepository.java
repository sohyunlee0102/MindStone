package Spring.MindStone.repository.EmotionNoteRepository;

import Spring.MindStone.domain.emotion.EmotionNote;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmotionNoteRepository extends JpaRepository<EmotionNote, Long> {
    Optional<List<EmotionNote>> findByIdAndCreatedAtBetween(Long id, LocalDateTime startOfDay, LocalDateTime endOfDay, Sort sort);
}
