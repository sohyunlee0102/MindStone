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
import Spring.MindStone.web.dto.emotionNoteDto.EmotionNoteStressSaveDTO;
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

        //호출해서 감정이 저장될때마다 감정통계 엔티티를 추가로 업데이트 시킴.
        dailyEmotionStatisticService.saveStatistics(memberInfo, LocalDate.now()
                ,EmotionList.fromString(note.getEmotion()),note.getEmotionFigure());

        emotionNoteRepository.save(emotionNote);
        return new SimpleEmotionNoteDTO(emotionNote);
    }

    public SimpleEmotionNoteDTO saveStressEmotionNote(EmotionNoteStressSaveDTO request, Long memberId){
        EmotionNoteSaveDTO emotionNoteDTO;
        if(request.getTime().equals("0시간 0분")||request.getTime().isBlank()){
            emotionNoteDTO = EmotionNoteSaveDTO.builder().emotionFigure(request.getEmotionFigure())
                    .emotion(request.getEmotion()).content("스트레스를 받아서 : "+request.getContent() + "을 했다.").build();
        }else{
            emotionNoteDTO = EmotionNoteSaveDTO.builder().emotionFigure(request.getEmotionFigure())
                    .emotion(request.getEmotion()).content("스트레스를 받아서 : "+request.getContent() + "을 했고, 얼마나 했는지: " + request.getTime()).build();
        }

        return saveEmotionNote(emotionNoteDTO, memberId);

    }
}
