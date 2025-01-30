package Spring.MindStone.service.emotionNoteService;

import Spring.MindStone.domain.emotion.EmotionNote;

import java.time.LocalDate;
import java.util.List;

public interface EmotionNoteQueryService {
    public List<EmotionNote> getNotesByIdAndDate(Long id, LocalDate date);
}
