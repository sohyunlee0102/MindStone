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
import Spring.MindStone.web.dto.emotionDto.EmotionReportResponseDTO;
import Spring.MindStone.web.dto.emotionNoteDto.EmotionNoteSaveDTO;
import Spring.MindStone.web.dto.emotionNoteDto.EmotionNoteStressSaveDTO;
import Spring.MindStone.web.dto.emotionNoteDto.SimpleEmotionNoteDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmotionNoteService {
    private final EmotionNoteRepository emotionNoteRepository;
    private final MemberInfoService memberInfoService;
    private final DailyEmotionStatisticRepository dailyEmotionStatisticRepository;
    private final DailyEmotionStatisticService dailyEmotionStatisticService;
    private final StressEmotionNoteRepository stressEmotionNoteRepository;
    private final EmotionNoteQueryServiceImpl emotionNoteQueryServiceImpl;

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

    public EmotionReportResponseDTO getEmotionCallendarReport(Long id, int year, int month){
        MemberInfo memberInfo = memberInfoService.findMemberById(id);
        List<DailyEmotionStatistic> statisticList = dailyEmotionStatisticRepository.findStatisticByDate(id, year, month);
        int totalDaysInMonth = YearMonth.of(year, month).lengthOfMonth();
        int recordedDays = statisticList.size();

        EmotionReportResponseDTO result = new EmotionReportResponseDTO();

        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        int weeks = Math.min(4, (totalDaysInMonth + startOfMonth.getDayOfWeek().getValue()) / 7); // 최대 4주까지만 허용

        // dto에 저장해야할 내용
        List<EmotionReportResponseDTO.WeeklyRecord> weeklyStatisticList = new ArrayList<>();
        for (int i = 0; i < weeks; i++) {
            weeklyStatisticList.add(new EmotionReportResponseDTO.WeeklyRecord()); // 기본값 0으로 초기화된 객체 추가
        }

// statisticList의 날짜를 기준으로 주차 계산
        for (DailyEmotionStatistic statistic : statisticList) {
            LocalDate currentDate = statistic.getDate(); // 해당 감정 데이터의 날짜
            int daysFromStart = (int) ChronoUnit.DAYS.between(startOfMonth, currentDate);
            int weekIndex = Math.min(daysFromStart / 7, 3); // 최대 index 3 (4주 제한)

            EmotionReportResponseDTO.WeeklyRecord weekRecord = weeklyStatisticList.get(weekIndex);
            EmotionReportResponseDTO.WeeklyRecord dayRecord = new EmotionReportResponseDTO.WeeklyRecord(statistic);

            weekRecord.add(dayRecord);
        }

        //2페이지 내용 저장 완료
        result.setTotalEmtoionStatistic(weeklyStatisticList);

        //1페이지 계산
        EmotionReportResponseDTO.WeeklyRecord monthlyList = new EmotionReportResponseDTO.WeeklyRecord();
        for(int i = 0; i < weeks; i++){
            monthlyList.add(weeklyStatisticList.get(i));
        }

        String bestEmotion = monthlyList.getHighestEmotion();

        //1페이지에 이미지 변경에 필요한 감정 삽입
        result.setBestEmotion(bestEmotion);

        String koreanEmotion = switch (bestEmotion) {
            case "ANGER" -> "화남";
            case "DEPRESSION" -> "우울";
            case "SAD" -> "슬픔";
            case "CALM" -> "평온";
            case "JOY" -> "기쁨";
            case "THRILL" -> "설렘";
            case "HAPPINESS" -> "행복";
            default -> "무감정"; // 예외 처리
        };

        double percentage = ((double) totalDaysInMonth/recordedDays) * 100;
        String formattedPercentage = String.format("%.1f", percentage);

        String totalReport;
        if(koreanEmotion.equals("무감정")){
            totalReport = "이번 달에는 기록한 감정이 없네요.";
        }else{
            totalReport = month + "월에는 " + formattedPercentage +"% 기록했고,\n"
                    + koreanEmotion + " 감정이 가장 많았어요.";
        }

        //1페이지 마무리
        result.setTotalReport(totalReport);

        //3페이지
        result.setTotalSummary(emotionSummary(id, year, month, percentage, monthlyList));



        return result;
    }

    String emotionSummary(Long id,int year, int month, double percentage,
                          EmotionReportResponseDTO.WeeklyRecord monthlyRecord){
        int previousMonth;
        int previousYear;
        if(month == 1){
            previousMonth = 12;
            previousYear = year - 1;
        }else{
            previousMonth = month - 1;
            previousYear = year;
        }
        List<DailyEmotionStatistic> statisticList = dailyEmotionStatisticRepository.findStatisticByDate(id, previousYear, previousMonth);
        int totalDaysInMonth = YearMonth.of(previousYear, previousMonth).lengthOfMonth();
        int recordedDays = statisticList.size();
        double previousPercentage = ((double) totalDaysInMonth/recordedDays) * 100;


        String firstSummary;
        if(percentage >= previousPercentage){
            String formattedPercentage = String.format("%.1f", percentage - previousPercentage);
            firstSummary = "저번 달 보다 기록률이" + formattedPercentage + "% 증가했어요!\n";
        }else{
            String formattedPercentage = String.format("%.1f", (previousPercentage - percentage));
            firstSummary = "저번 달 보다 기록률이" + formattedPercentage + "% 감소했어요.\n";
        }

        int positiveEmotion = monthlyRecord.getJoyFigure() + monthlyRecord.getHappinessFigure() + monthlyRecord.getThrillFigure();
        int negativeEmotion = monthlyRecord.getAngerFigure() + monthlyRecord.getDepressionFigure() + monthlyRecord.getSadFigure();

        String secondSummary = summarizeEmotionState(positiveEmotion, negativeEmotion);


        String thirdSummary = summarizeStressSolution(id,year,month);



        return firstSummary + secondSummary + thirdSummary;
    }

    public String summarizeEmotionState(int positiveEmotion, int negativeEmotion) {
        int totalEmotion = positiveEmotion + negativeEmotion;
        if (totalEmotion == 0) {
            return "이번 달은 아직 감정의 기록이 없어요\n";
        }

        double negativeRatio = (double) negativeEmotion / totalEmotion;
        double positiveRatio = (double) positiveEmotion / totalEmotion;

        if (positiveRatio >= 0.55 && negativeRatio >= 0.40) {
            // 시나리오 1: 행복이 높지만, 슬픈 감정이 여전히 40% 이상
            return "기쁜 순간이 많지만, 아직은 감정을 다스리는 과정이 필요해요. 조금씩 더 나아질 거예요!\n\n";
        } else if (negativeRatio >= 0.55 && positiveRatio >= 0.40) {
            // 시나리오 2: 슬픔이 높지만, 행복한 감정이 여전히 40% 이상
            return "어려운 시기를 겪고 있지만, 희망의 불씨는 여전히 남아 있어요. 긍정적인 변화가 다가올 거예요!\n\n";
        } else if (positiveRatio >= 0.65) {
            // 시나리오 3: 행복이 압도적으로 높음 (65% 이상)
            return "행복한 감정이 가득한 한 달이였어요! 소중한 순간들을 오래 기억하며 즐겨보세요.\n\n";
        } else if (negativeRatio >= 0.65) {
            // 시나리오 4: 슬픔이 압도적으로 높음 (65% 이상)
            return "힘든 한 달을 보냈을 수도 있지만, 내일은 오늘보다 더 나아질 거예요. 자신을 소중히 여겨주세요!\n\n";
        } else {
            // 중립적인 감정 상태 (50:50이거나, 어느 한쪽도 압도적이지 않음)
            return "이번 달은 감정의 균형을 이루며 지나갔어요. 차분하게 앞으로 나아가 봅시다!\n\n";
        }
    }

    public String summarizeStressSolution(Long id, int year, int month){
        List<EmotionList> negativeEmotions = List.of(EmotionList.ANGER, EmotionList.DEPRESSION, EmotionList.SAD);
        int count = emotionNoteRepository.countNegativeEmotionNoteByMonth(id, year, month, negativeEmotions);
        int solutionCount = stressEmotionNoteRepository.countStressEmotionNoteByMonth(id, year, month);

        double resolutionRate = (double) solutionCount / count * 100;

        String formattedRate = String.format("%.1f", resolutionRate);
        String result = "스트레스 대응 비율은 " + formattedRate + "%에요\n";
        if (resolutionRate < 20) {
            return result + "스트레스 받는 일이 있어도, 바로 해결하기가 쉽지 않네요. 천천히 해결해 나가봐요.";
        } else if (resolutionRate < 60) {
            return result+  "스트레스 받는 일이 있지만, 일부를 바로바로 잘 대응하고 있어요!";
        } else {
            return result+ "스트레스 받는 일이 있어도, 대부분 극복하는 앞으로도 이렇게 계속 나아가요!";
        }

    }
}
