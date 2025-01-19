package Spring.MindStone.service.DiaryService;

import Spring.MindStone.apiPayload.exception.GeneralException;

import Spring.MindStone.domain.DailyDiary;
import Spring.MindStone.domain.EmotionNote;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DiaryCommandServiceImpl implements DiaryCommandService {

    @Value("$openai.secret-key")
    private String SECRET_KEY;
    private OpenAiService openAiService;

    private final String USER = ChatMessageRole.USER.value();
    private final String ASSISTANT = ChatMessageRole.ASSISTANT.value();
    private final String SYSTEM = ChatMessageRole.SYSTEM.value();

    @PostConstruct
    public void init() {
        openAiService = new OpenAiService(SECRET_KEY, Duration.ofSeconds(45)); // 45초 내에 응답 안올 시 예외 던짐
        // TODO openAiService 싱글톤으로 (OpenAiServiceWrapper 빈으로 감싸서 클래스 이곳 저곳에 제공?)
    }

    @Override
    public String createDiary(Long id, String bodyPart) {
        List<ChatMessage> systemPrompt = List.of(
                new ChatMessage(SYSTEM, "You are an emotional diary generator that writes daily reflections in Korean."),
                new ChatMessage(SYSTEM, "1. The input consists of daily activities described with time, action, emotion, and emotionScore."),
                new ChatMessage(SYSTEM, "2. The emotionScore ranges from 50 to 500, in increments of 50. Instead of showing the numeric score, describe the intensity of emotions qualitatively, ranging from 'very little' to 'very much.'"),
                new ChatMessage(SYSTEM, "3. Arrange the activities in chronological order to ensure the diary follows the order of events during the day."),
                new ChatMessage(SYSTEM, "4. Use natural and empathetic language to make the diary feel personal and relatable."),
                new ChatMessage(SYSTEM, "5. Highlight the user's emotional transitions throughout the day and summarize the overall sentiment at the end."),
                new ChatMessage(SYSTEM, "6. Keep the diary concise and engaging, ensuring it feels authentic to the user's experiences."),
                new ChatMessage(SYSTEM, "The diary must be written in Korean.")
        );
        List<ChatMessage> chatPrompt = List.of(
                new ChatMessage(USER, "08:00 AM - Action: 아침 운동, Emotion: 기쁨, Emotion Score: 50"),
                new ChatMessage(ASSISTANT, "아침 8시에 일어나서 아침 운동을 하니 살짝 기뻤다."),
                new ChatMessage(USER, "10:00 AM - Action: 회의 참석, Emotion: 스트레스, Emotion Score: 300"),
                new ChatMessage(ASSISTANT, "10시에는 회의 참석을 하니 스트레스가 좀 컸던거 같다."),
                new ChatMessage(USER, "01:00 PM - Action: 점심 식사, Emotion: 만족, Emotion Score: 150"),
                new ChatMessage(ASSISTANT, "1시에 점심 식사를 하였는데 만족스러웠다."),
                new ChatMessage(USER, "03:00 PM - Action: 업무 마감, Emotion: 성취감, Emotion Score: 450"),
                new ChatMessage(ASSISTANT, "3시에 업무 마감을 하니 매우 성취감이 컸다."),
                new ChatMessage(USER, bodyPart)
        );
        return getOpenAiResult(generatePrompt(systemPrompt, chatPrompt));
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




//    public String generatePrompt(List<EmotionNote> emotionNoteList){
//        return null;
//    }
//
//    public String callOpenAI(String prompt, int maxTokens) throws JsonProcessingException {
//        // 1. 헤더 설정
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBearerAuth(gptConfig.getSecretKey());
//
//        // 2. 메시지 템플릿 정의
//        String systemMessage = "You are a helpful assistant. " + // 시스템 역할 메시지
//                "Please generate a diary entry based on the user input below.";
//        String userMessage = prompt;
//
//        // 3. 요청 본문 작성
//        Map<String, Object> requestBody = new HashMap<>();
//        requestBody.put("model", gptConfig.getModel());
//        requestBody.put("messages", Arrays.asList(
//                Map.of("role", "system", "content", systemMessage),
//                Map.of("role", "user", "content", userMessage)
//        ));
//        requestBody.put("temperature", 0.3);
//        requestBody.put("max_tokens", maxTokens);
//
//        // 4. HTTP 요청 생성
//        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
//
//        try {
//            // 5. REST API 호출
//            ResponseEntity<String> response = restTemplate.exchange(
//                    API_URL, HttpMethod.POST, entity, String.class);
//            return response.getBody();
//        } catch (Exception e) {
//            // 예외 메시지 상세화
//            throw new RuntimeException("Failed to call OpenAI API: " + e.getMessage(), e);
//        }
//    }



}
