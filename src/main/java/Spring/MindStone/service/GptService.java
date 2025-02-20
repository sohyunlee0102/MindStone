package Spring.MindStone.service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GptService {
    @Value("${openai.secret-key}")
    private String SECRET_KEY;
    private OpenAiService openAiService;


    @PostConstruct
    public void init() {
        openAiService = new OpenAiService(SECRET_KEY, Duration.ofSeconds(45)); // 45초 내에 응답 안올 시 예외 던짐
        // TODO openAiService 싱글톤으로 (OpenAiServiceWrapper 빈으로 감싸서 클래스 이곳 저곳에 제공?)
    }

    private final String USER = ChatMessageRole.USER.value();
    private final String ASSISTANT = ChatMessageRole.ASSISTANT.value();
    private final String SYSTEM = ChatMessageRole.SYSTEM.value();

    private final List<ChatMessage> diaryPrompt = List.of(
            new ChatMessage(SYSTEM, "You are an emotional diary generator that writes daily reflections in Korean."),
// 시스템 메시지: 감성 일기를 작성하는 AI 역할을 설정 (한국어로 작성)

            new ChatMessage(SYSTEM, "1. The input consists of daily activities described with action, emotion, and emotionScore (without time)."),
// 입력 데이터: 하루 동안의 활동(action), 감정(emotion), 감정 점수(emotionScore)로 구성됨 (시간 정보 없음)

            new ChatMessage(SYSTEM, "2. Arrange the activities in a natural order to ensure the diary feels coherent and follows a logical flow of events."),
// 활동을 자연스럽고 논리적인 순서로 정리하여 일기의 흐름을 유지

            new ChatMessage(SYSTEM, "3. Use casual and relatable language, avoiding formal speech. Write as if the user is talking to themselves."),
// 사용자와 대화하듯이 친근하고 편안한 말투 사용 (격식체 X)

            new ChatMessage(SYSTEM, "4. Highlight the user's emotional transitions throughout the day and summarize the overall sentiment at the end."),
// 하루 동안의 감정 변화를 강조하고 마지막에 전체적인 감정 상태를 요약

            new ChatMessage(SYSTEM, "5. Keep the diary concise, engaging, and authentic to the user's experiences."),
// 일기를 간결하고 흥미롭게 작성하며, 사용자의 경험을 진솔하게 반영

            new ChatMessage(SYSTEM, "6. If no input is provided or the input is not understandable, write a reflection with the phrase like '이유 없이' to describe the emotion."),
// 입력이 없거나 이해할 수 없을 경우, '이유 없이' 등의 표현을 사용해 감정을 기술

            new ChatMessage(SYSTEM, "7. If asked to regenerate, rewrite the diary with a different perspective or style while maintaining coherence and emotional flow."),
// 재생성을 요청받으면, 논리적 흐름과 감정 변화를 유지하면서 다른 시각 또는 스타일로 다시 작성

            new ChatMessage(SYSTEM, "8. If a sentence starts with '그 때문에', interpret it as the previous event being a source of stress and the current event as a way to relieve that stress."),
// "그 때문에"로 시작하는 문장은 앞선 일이 스트레스 원인이 되었으며, 현재 활동이 그 스트레스를 푸는 행위로 해석

            new ChatMessage(SYSTEM, "9. Emotion scores range from 10 to 100, and the diary must reflect these scores absolutely in the intensity of the emotions described."),
// 감정 점수(EmotionScore)는 10~100점 사이이며, 해당 점수에 맞게 절대적으로 감정 강도를 반영하여 문장 서술

            new ChatMessage(SYSTEM, "The diary must be written in Korean.")
// 일기는 반드시 한국어로 작성

    );

    public ChatCompletionRequest generateDiaryPrompt(List<ChatMessage> chatPrompt){
        List<ChatMessage> promptMessage = new ArrayList<>();
        promptMessage.addAll(diaryPrompt);
        promptMessage.addAll(chatPrompt);

        return ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(promptMessage)
                .temperature(0.2)
                .build();
    }

    private final List<ChatMessage> recommandPrompt = List.of(
            // 사용자는 직업과 MBTI를 입력하면 스트레스 해소를 위한 3가지 행동을 추천받음
            new ChatMessage(SYSTEM, "You are an assistant that suggests 3 actions"),
            new ChatMessage(SYSTEM, "for relieving stress or feeling down,"),
            new ChatMessage(SYSTEM, "considering the user's job and MBTI type."),

            // 추천된 행동은 트렌디하고, 친구·동료·선후배 관계에서 활용할 수 있는 방식이어야 함
            new ChatMessage(SYSTEM, "The recommendations should be trendy and socially relatable,"),
            new ChatMessage(SYSTEM, "fitting into relationships such as friends, colleagues, or mentors."),

            // 현재 유행하는 취미, 엔터테인먼트, 사회적 활동을 반영해야 함
            new ChatMessage(SYSTEM, "Ensure that the recommendations align with"),
            new ChatMessage(SYSTEM, "current trends in entertainment, hobbies, and social activities."),

            // 출력값은 행동 3개만, 쉼표(,)로 구분하여 제공 (추가 설명 없이)
            new ChatMessage(SYSTEM, "The output must be only the three actions,"),
            new ChatMessage(SYSTEM, "separated by commas, with no additional explanation."),

            // 사용자가 이전에 추천받은 행동을 제공할 수도 있으며, 중복되지 않도록 새롭게 추천해야 함
            new ChatMessage(SYSTEM, "The user may provide previously recommended actions,"),
            new ChatMessage(SYSTEM, "and you must ensure that the new suggestions do not include them."),

            // 반드시 한국어로 출력되도록 설정 (입력 언어와 상관없이)
            new ChatMessage(SYSTEM, "The output must be in Korean, regardless of the input language."),

            // 추천하는 행동은 10단어 이하로 제한
            new ChatMessage(SYSTEM, "Each recommended action must be within 10 words.")
    );

    public ChatCompletionRequest generateRecommandPrompt(List<ChatMessage> chatPrompt){
        List<ChatMessage> promptMessage = new ArrayList<>();
        promptMessage.addAll(recommandPrompt);
        promptMessage.addAll(chatPrompt);

        return ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(promptMessage)
                .temperature(0.2)
                .build();
    }

    public String getOpenAiResult(ChatCompletionRequest request){
        Long startTime = System.currentTimeMillis();
        ChatCompletionResult result = openAiService.createChatCompletion(request);
        Long endTime = System.currentTimeMillis();
        System.out.printf("GPTTranslationService: translation took %d seconds, consumed %d tokens total (prompt %d, completion %d)%n", (endTime-startTime)/1000, result.getUsage().getTotalTokens(), result.getUsage().getPromptTokens(), result.getUsage().getCompletionTokens());
        return result.getChoices().get(0).getMessage().getContent();
    }
}
