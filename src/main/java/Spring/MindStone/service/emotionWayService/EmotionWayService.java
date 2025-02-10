package Spring.MindStone.service.emotionWayService;

import Spring.MindStone.domain.member.MemberInfo;
import Spring.MindStone.domain.member.MemberInterest;
import Spring.MindStone.repository.memberRepository.MemberInfoRepository;
import Spring.MindStone.repository.memberRepository.MemberInterestRepository;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmotionWayService {

    @Value("${openai.secret-key}")
    private String SECRET_KEY;
    private OpenAiService openAiService;

    private final String USER = ChatMessageRole.USER.value();
    private final String ASSISTANT = ChatMessageRole.ASSISTANT.value();
    private final String SYSTEM = ChatMessageRole.SYSTEM.value();

    private final MemberInfoRepository memberInfoRepository;
    private final MemberInterestRepository memberInterestRepository;

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

    private final List<ChatMessage> systemPrompt = List.of(

    );

    @PostConstruct
    public void init() {
        openAiService = new OpenAiService(SECRET_KEY, Duration.ofSeconds(45)); // 45초 내에 응답 안올 시 예외 던짐
        // TODO openAiService 싱글톤으로 (OpenAiServiceWrapper 빈으로 감싸서 클래스 이곳 저곳에 제공?)
    }


    private ChatCompletionRequest generatePrompt(List<ChatMessage> systemPrompt, List<ChatMessage> chatPrompt){
        List<ChatMessage> promptMessage = new ArrayList<>();
        promptMessage.addAll(systemPrompt);
        promptMessage.addAll(chatPrompt);

        return ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(promptMessage)
                .temperature(0.2)
                .build();
    }

    private String getOpenAiResult(ChatCompletionRequest request){
        Long startTime = System.currentTimeMillis();
        ChatCompletionResult result = openAiService.createChatCompletion(request);
        Long endTime = System.currentTimeMillis();
        System.out.printf("GPTTranslationService: translation took %d seconds, consumed %d tokens total (prompt %d, completion %d)%n", (endTime-startTime)/1000, result.getUsage().getTotalTokens(), result.getUsage().getPromptTokens(), result.getUsage().getCompletionTokens());
        return result.getChoices().get(0).getMessage().getContent();
    }

}
