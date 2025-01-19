package Spring.MindStone.service.EmotionNoteService;

import Spring.MindStone.domain.EmotionNote;

import java.time.LocalDate;
import java.util.List;

public interface EmotionNoteQueryService {
    public List<EmotionNote> getNotesByIdAndDate(Long id, LocalDate date);
}
