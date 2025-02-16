package Spring.MindStone.service.emotionNoteService;

import Spring.MindStone.domain.diary.DailyEmotionStatistic;
import Spring.MindStone.domain.emotion.EmotionNote;
import Spring.MindStone.domain.emotion.StressEmotionNote;
import Spring.MindStone.domain.enums.EmotionList;
import Spring.MindStone.domain.member.MemberInfo;
import Spring.MindStone.repository.diaryRepository.DailyEmotionStatisticRepository;
import Spring.MindStone.repository.emotionNoteRepository.EmotionNoteRepository;
import Spring.MindStone.repository.emotionNoteRepository.StressEmotionNoteRepository;
import Spring.MindStone.service.diaryService.DailyEmotionStatisticService;
import Spring.MindStone.service.memberInfoService.MemberInfoService;
import Spring.MindStone.web.dto.emotionNoteDto.EmotionNoteSaveDTO;
import Spring.MindStone.web.dto.emotionNoteDto.EmotionNoteStressSaveDTO;
import Spring.MindStone.web.dto.emotionNoteDto.SimpleEmotionNoteDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmotionNoteService {
    private final EmotionNoteRepository emotionNoteRepository;
    private final MemberInfoService memberInfoService;
    private final DailyEmotionStatisticRepository dailyEmotionStatisticRepository;
    private final DailyEmotionStatisticService dailyEmotionStatisticService;
    private final StressEmotionNoteRepository stressEmotionNoteRepository;

    @Transactional
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

    @Transactional
    public SimpleEmotionNoteDTO saveStressEmotionNote(EmotionNoteStressSaveDTO request, Long memberId){
        MemberInfo memberInfo = memberInfoService.findMemberById(memberId);
        EmotionNote emotionNote = emotionNoteRepository.findById(request.getSteressReson_id()).orElseThrow(() -> new EntityNotFoundException("기록했던 슬픈 일이 존재하지 않습니다."));

        StressEmotionNote stressEmotionNote = StressEmotionNote.builder().memberInfo(memberInfo)
                .emotionFigure(request.getEmotionFigure())
                .emotion(EmotionList.fromString(request.getEmotion()))
                .content(request.getContent())
                .stressNote(emotionNote)
                .build();

        dailyEmotionStatisticService.saveStatistics(memberInfo, LocalDate.now()
                ,EmotionList.fromString(request.getEmotion()),request.getEmotionFigure());

        stressEmotionNoteRepository.save(stressEmotionNote);
        return new SimpleEmotionNoteDTO(stressEmotionNote);
    }

    public SimpleEmotionNoteDTO getEmotionCallendarReport(Long id, int year, int month){
        MemberInfo memberInfo = memberInfoService.findMemberById(id);
        List<DailyEmotionStatistic> statisticList = dailyEmotionStatisticRepository.findStatisticByDate(id, year, month);
        int totalDaysInMonth = YearMonth.of(year, month).lengthOfMonth();
        int recordedDays = statisticList.size();

        for(int i=1; i<= totalDaysInMonth; i++){
            DailyEmotionStatistic statistic = statisticList.get(i);

        }
        return null;
    }
}
