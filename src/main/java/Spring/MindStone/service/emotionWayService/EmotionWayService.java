package Spring.MindStone.service.emotionWayService;

import Spring.MindStone.apiPayload.code.status.ErrorStatus;
import Spring.MindStone.apiPayload.exception.handler.MemberInfoHandler;
import Spring.MindStone.domain.diary.DailyEmotionStatistic;
import Spring.MindStone.domain.member.MemberInfo;
import Spring.MindStone.domain.member.MemberInterest;
import Spring.MindStone.repository.memberRepository.MemberInfoRepository;
import Spring.MindStone.repository.memberRepository.MemberInterestRepository;
import Spring.MindStone.service.GptService;
import Spring.MindStone.service.diaryService.DailyEmotionStatisticService;
import Spring.MindStone.service.memberInfoService.MemberInfoService;
import Spring.MindStone.web.dto.emotionWayDto.AiRecommandResponseDto;
import Spring.MindStone.web.dto.emotionWayDto.EmotionWayRequestDto;
import Spring.MindStone.web.dto.emotionWayDto.EmotionWayResponseDto;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmotionWayService {

    private final MemberInfoService memberInfoService;
    private final GptService gptService;
    @Value("${openai.secret-key}")
    private String SECRET_KEY;
    private OpenAiService openAiService;

    private final String USER = ChatMessageRole.USER.value();
    private final String ASSISTANT = ChatMessageRole.ASSISTANT.value();
    private final String SYSTEM = ChatMessageRole.SYSTEM.value();

    private final MemberInfoRepository memberInfoRepository;
    private final MemberInterestRepository memberInterestRepository;
    private final DailyEmotionStatisticService dailyEmotionStatisticService;
    /**
     * 감정 관리 방법 조회
     */
    public EmotionWayResponseDto getEmotionWay(Long memberId) {
        MemberInfo member = memberInfoRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원 정보를 찾을 수 없습니다."));

        MemberInterest memberInterest = memberInterestRepository.findByMemberInfo(member)
                .orElseThrow(() -> new EntityNotFoundException("회원의 감정 관리 정보를 찾을 수 없습니다."));

        return EmotionWayResponseDto.builder()
                .mbti(member.getMbti())
                .job(member.getJob())
                .hobby(memberInterest.getHobbyActions())
                .strengths(memberInterest.getSpecialSkillActions())
                .stressManagement(memberInterest.getStressActions())
                .build();
    }

    /**
     * 감정 관리 방법 수정
     */
    @Transactional
    public void updateEmotionWay(Long memberId, EmotionWayRequestDto dto) {
        MemberInfo member = memberInfoRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원 정보를 찾을 수 없습니다."));

        MemberInterest memberInterest = memberInterestRepository.findByMemberInfo(member)
                .orElseThrow(() -> new EntityNotFoundException("회원의 감정 관리 정보를 찾을 수 없습니다."));

        // 정보 업데이트
        member.setMbti(dto.getMbti());
        member.setJob(dto.getJob());
        memberInterest.setHobbyActions(dto.getHobby());
        memberInterest.setSpecialSkillActions(dto.getStrengths());
        memberInterest.setStressActions(dto.getStressManagement());

        memberInfoRepository.save(member);
        memberInterestRepository.save(memberInterest);
    }


    private void checkGPTCount(Long memberId){
        DailyEmotionStatistic statisticEntity = dailyEmotionStatisticService.getStatisticEntity(memberId);
        if(statisticEntity.getActionRecommandCount()> 0){
            statisticEntity.setActionRecommandCount(statisticEntity.getActionRecommandCount() - 1);
            dailyEmotionStatisticService.updateStatistic(statisticEntity);
        }else{
            throw new MemberInfoHandler(ErrorStatus.NO_MORE_AI_COUNT);
        }
    }

    public AiRecommandResponseDto getAiRecommand(Long memberId, String previousRecommand){
        checkGPTCount(memberId);

        MemberInfo memberInfo = memberInfoService.findMemberById(memberId);
        String job = memberInfo.getJob().toString();
        String mbti = memberInfo.getMbti().toString();
        String content = job + " " + mbti + ", 추천한 행동 :" + previousRecommand;
        List<ChatMessage> chatPrompt = List.of(
                new ChatMessage(USER, "대학생 INFP, 추천한 행동: 게임하기,서점에서 책 구매하기,옛날영화보기"),
                new ChatMessage(ASSISTANT, "카페에서 감성 사진 찍기,전시회 구경하기,플레이리스트 정리하기"),
                new ChatMessage(USER, "직장인 ESTJ, 추천한 행동: "),
                new ChatMessage(ASSISTANT, "헬스장 가기,맛집 동료들과 방문하기,주말 드라이브"),
                new ChatMessage(USER, content)
        );

        String recommand = gptService.getOpenAiResult(gptService.generateRecommandPrompt(chatPrompt));
        return AiRecommandResponseDto.builder().recommand(recommand).
                previousRecommand(previousRecommand+","+recommand).build();
    }

    public String getRecommand(Long memberId){
        MemberInfo member = memberInfoService.findMemberById(memberId);

        MemberInterest memberInterest = memberInterestRepository.findByMemberInfo(member)
                .orElseThrow(() -> new EntityNotFoundException("회원의 감정 관리 정보를 찾을 수 없습니다."));

        String interestString = memberInterest.getHobbyActions() + "," +
                memberInterest.getStressActions() + "," + memberInterest.getSpecialSkillActions();

        //죄송해요 그냥 시간없어서 셔플 시켜서 3개 던져주기로 했어요..
        //시간나면은 이쪽 보완하는걸로..
        List<String> interestList = new ArrayList<>(Arrays.stream(interestString.split(",")).toList());

        Collections.shuffle(interestList);

        int size = Math.min(3, interestList.size());
        List<String> randomSelection = interestList.subList(0, size);

        //list1,list2,list3해서 3개 랜덤하게 보낼예정.. ㅎ.....
        return String.join(",", randomSelection);

    }


}
