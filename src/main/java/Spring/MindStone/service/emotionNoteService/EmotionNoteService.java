package Spring.MindStone.service.emotionNoteService;

import Spring.MindStone.apiPayload.code.status.ErrorStatus;
import Spring.MindStone.apiPayload.exception.handler.MemberInfoHandler;
import Spring.MindStone.domain.emotion.EmotionNote;
import Spring.MindStone.domain.enums.EmotionList;
import Spring.MindStone.domain.member.MemberInfo;
import Spring.MindStone.repository.diaryRepository.DailyEmotionStatisticRepository;
import Spring.MindStone.repository.emotionNoteRepository.EmotionNoteRepository;
import Spring.MindStone.service.diaryService.DailyEmotionStatisticService;
import Spring.MindStone.service.memberInfoService.MemberInfoService;
import Spring.MindStone.web.dto.emotionNoteDto.EmotionNoteSaveDTO;
import Spring.MindStone.web.dto.emotionNoteDto.SimpleEmotionNoteDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class EmotionNoteService {
    private final EmotionNoteRepository emotionNoteRepository;
    private final MemberInfoService memberInfoService;
    private final DailyEmotionStatisticRepository dailyEmotionStatisticRepository;
    private final DailyEmotionStatisticService dailyEmotionStatisticService;

    public SimpleEmotionNoteDTO saveEmotionNote(EmotionNoteSaveDTO note, Long memberId) {
        MemberInfo memberInfo = memberInfoService.findMemberById(memberId);
        EmotionNote emotionNote = EmotionNote.builder()
                .memberInfo(memberInfo)
                .emotionFigure(note.getEmotionFigure())
                .emotion(EmotionList.fromString(note.getEmotion()))
                .content(note.getContent()).build();

        dailyEmotionStatisticService.saveStatistics(memberInfo, LocalDate.now()
                ,EmotionList.fromString(note.getEmotion()),note.getEmotionFigure());

        emotionNoteRepository.save(emotionNote);
        return new SimpleEmotionNoteDTO(emotionNote);
    }
}
