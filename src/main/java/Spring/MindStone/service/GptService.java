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
            new ChatMessage(SYSTEM, "1. The input consists of daily activities described with action, emotion, and emotionScore (without time)."),
            new ChatMessage(SYSTEM, "2. Arrange the activities in a natural order to ensure the diary feels coherent and follows a logical flow of events."),
            new ChatMessage(SYSTEM, "3. Use casual and relatable language, avoiding formal speech. Write as if the user is talking to themselves"),
            new ChatMessage(SYSTEM, "4. Highlight the user's emotional transitions throughout the day and summarize the overall sentiment at the end."),
            new ChatMessage(SYSTEM, "5. Keep the diary concise, engaging, and authentic to the user's experiences."),
            new ChatMessage(SYSTEM, "6. If no input is provided or the input is not understandable, write a reflection with the phrase like '이유 없이' to describe the emotion."),
            new ChatMessage(SYSTEM, "7. If asked to regenerate, rewrite the diary with a different perspective or style while maintaining coherence and emotional flow."),
            new ChatMessage(SYSTEM, "The diary must be written in Korean.")
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
            new ChatMessage(SYSTEM, "You are an emotional diary generator that writes daily reflections in Korean."),
            new ChatMessage(SYSTEM, "1. The input consists of daily activities described with action, emotion, and emotionScore (without time)."),
            new ChatMessage(SYSTEM, "2. Arrange the activities in a natural order to ensure the diary feels coherent and follows a logical flow of events."),
            new ChatMessage(SYSTEM, "3. Use casual and relatable language, avoiding formal speech. Write as if the user is talking to themselves"),
            new ChatMessage(SYSTEM, "4. Highlight the user's emotional transitions throughout the day and summarize the overall sentiment at the end."),
            new ChatMessage(SYSTEM, "5. Keep the diary concise, engaging, and authentic to the user's experiences."),
            new ChatMessage(SYSTEM, "6. If no input is provided or the input is not understandable, write a reflection with the phrase like '이유 없이' to describe the emotion."),
            new ChatMessage(SYSTEM, "7. If asked to regenerate, rewrite the diary with a different perspective or style while maintaining coherence and emotional flow."),
            new ChatMessage(SYSTEM, "The diary must be written in Korean.")
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
