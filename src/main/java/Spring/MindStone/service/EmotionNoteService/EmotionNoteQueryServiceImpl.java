package Spring.MindStone.service.EmotionNoteService;

import Spring.MindStone.domain.emotion.EmotionNote;
import Spring.MindStone.repository.EmotionNoteRepository.EmotionNoteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmotionNoteQueryServiceImpl implements EmotionNoteQueryService {
    private final EmotionNoteRepository emotionNoteRepository;


    //자동일기 생성할 때, 필요한 하루 일들을 갖고 오는 함수, DiaryCommandService에서 사용
    public List<EmotionNote> getNotesByIdAndDate(Long id, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay(); // 날짜 시작 시간
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay(); // 날짜 다음 날 시작 시간
        Sort sort = Sort.by("createdAt").ascending();// 날짜기준으로 오름차순 정렬
        List<EmotionNote> result = emotionNoteRepository.findByIdAndCreatedAtBetween(id, startOfDay, endOfDay, sort)
                .orElseThrow(() -> new EntityNotFoundException("해당 날짜의 일과가 기록되지 않았습니다."));
        return result;

    }
}
