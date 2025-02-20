package Spring.MindStone.service.emotionNoteService;

import Spring.MindStone.domain.emotion.EmotionNote;
import Spring.MindStone.domain.emotion.StressEmotionNote;
import Spring.MindStone.domain.member.MemberInfo;

import java.time.LocalDate;
import java.util.List;

public interface EmotionNoteQueryService {
    public List<EmotionNote> getNotesByIdAndDate(MemberInfo memberInfo, LocalDate date);
    public List<StressEmotionNote> getStressNotesByIdAndDate(MemberInfo memberInfo, LocalDate date);
}
