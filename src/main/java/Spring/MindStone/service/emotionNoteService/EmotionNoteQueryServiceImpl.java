package Spring.MindStone.service.emotionNoteService;

import Spring.MindStone.apiPayload.code.status.ErrorStatus;
import Spring.MindStone.apiPayload.exception.handler.MemberInfoHandler;
import Spring.MindStone.domain.emotion.EmotionNote;
import Spring.MindStone.domain.emotion.StressEmotionNote;
import Spring.MindStone.domain.member.MemberInfo;
import Spring.MindStone.repository.emotionNoteRepository.EmotionNoteRepository;
import Spring.MindStone.repository.emotionNoteRepository.StressEmotionNoteRepository;
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
    private final StressEmotionNoteRepository stressEmotionNoteRepository;


    @Override
    //자동일기 생성할 때, 필요한 하루 일들을 갖고 오는 함수, DiaryCommandService에서 사용
    public List<EmotionNote> getNotesByIdAndDate(MemberInfo memberInfo, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay(); // 날짜 시작 시간
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay(); // 날짜 다음 날 시작 시간
        Sort sort = Sort.by("createdAt").ascending();// 날짜기준으로 오름차순 정렬
        List<EmotionNote> result = emotionNoteRepository.findByMemberInfoAndCreatedAtBetween(memberInfo, startOfDay, endOfDay, sort);

        if(result.isEmpty()){
            throw new MemberInfoHandler(ErrorStatus.NOTE_NOT_FOUND);
        }

        return result;
    }

    @Override
    public List<StressEmotionNote> getStressNotesByIdAndDate(MemberInfo memberInfo, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay(); // 날짜 다음 날 시작 시간
        Sort sort = Sort.by("createdAt").ascending();// 날짜기준으로 오름차순 정렬

        List<StressEmotionNote> result = stressEmotionNoteRepository.findByIdAndCreatedAtBetween(memberInfo, startOfDay, endOfDay, sort);

        return result;
    }
}
