package Spring.MindStone.service.EmotionNoteService;

import Spring.MindStone.domain.emotion.EmotionNote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmotionNoteQueryService {
    public List<EmotionNote> getNotesByIdAndDate(Long id, LocalDate date);
}
