package Spring.MindStone.service.DiaryService;

import Spring.MindStone.domain.diary.DailyDiary;
import Spring.MindStone.domain.emotion.EmotionNote;
import Spring.MindStone.domain.member.MemberInfo;
import Spring.MindStone.repository.DiaryRepository.DiaryRepository;
import Spring.MindStone.service.EmotionNoteService.EmotionNoteQueryService;
import Spring.MindStone.web.dto.diaryDto.DiaryResponseDTO;
import Spring.MindStone.web.dto.diaryDto.DiarySaveDTO;
import Spring.MindStone.web.dto.diaryDto.SimpleDiaryDTO;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryCommandServiceImpl implements DiaryCommandService {


    private final EmotionNoteQueryService emotionNoteService;
    private final DiaryRepository diaryRepository;

    @Value("${openai.secret-key}")
    private String SECRET_KEY;
    private OpenAiService openAiService;

    private final String USER = ChatMessageRole.USER.value();
    private final String ASSISTANT = ChatMessageRole.ASSISTANT.value();
    private final String SYSTEM = ChatMessageRole.SYSTEM.value();

    private final List<ChatMessage> systemPrompt = List.of(
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

    @PostConstruct
    public void init() {
        openAiService = new OpenAiService(SECRET_KEY, Duration.ofSeconds(45)); // 45초 내에 응답 안올 시 예외 던짐
        // TODO openAiService 싱글톤으로 (OpenAiServiceWrapper 빈으로 감싸서 클래스 이곳 저곳에 제공?)
    }

    @Override
    public DiaryResponseDTO.DiaryCreationResponseDTO createDiary(Long id, LocalDate date) {
        //1. EmotionNoteService에서 멤버의 하루 일들을 갖고옴.

        List<EmotionNote> emotionNoteList = emotionNoteService.getNotesByIdAndDate(id, date);


        //2. 받아온 리스트를 합침.
        String result = emotionNoteList.stream()
                .map(EmotionNote::toString)
                .collect(Collectors.joining("\n"));

        System.out.println("일기 자동생성 호출 - 들어간 내용"+result);

        //3.result를 프롬프트와 함께 제작
        List<ChatMessage> chatPrompt = List.of(
                new ChatMessage(USER, "아침 운동, 기쁨, 50"),
                new ChatMessage(ASSISTANT, "아침에 일어나서 운동을 하니깐 그래도 상쾌한 기분이었다."),
                new ChatMessage(USER, "회의 참석, 분노, 300"),
                new ChatMessage(ASSISTANT, "회의 참석을 하니 아침 운동의 기쁨이 사라질정도로 화났다."),
                new ChatMessage(USER, "점심 식사, 만족, 150"),
                new ChatMessage(ASSISTANT, "그래도 점심 식사를 하니 꽤나 만족스러웠다."),
                new ChatMessage(USER, "업무 마감, 성취감, 450"),
                new ChatMessage(ASSISTANT, "업무 마감까지 하니 매우 성취감이 컸다."),
                new ChatMessage(USER, "ㅇㅁㄴㅇ, 분노, 50"),
                new ChatMessage(ASSISTANT, "이유 없이 분노가 좀 차올랐다."),
                new ChatMessage(USER, result)
        );



        //4. 프롬프트와 생성하는 함수와 GPT API를 실행
        String content = getOpenAiResult(generatePrompt(systemPrompt, chatPrompt));
        return DiaryResponseDTO.DiaryCreationResponseDTO
                .builder().content(content).bodyPart(result).build();
    }

    @Override
    public DiaryResponseDTO.DiaryCreationResponseDTO createDiaryRe(Long id, String bodyPart, LocalDate date){
        List<ChatMessage> chatPrompt = List.of(
                new ChatMessage(USER, "아침 운동, 기쁨, 50"),
                new ChatMessage(ASSISTANT, "아침에 일어나서 운동을 하니깐 그래도 상쾌한 기분이었다."),
                new ChatMessage(USER, "회의 참석, 분노, 300"),
                new ChatMessage(ASSISTANT, "회의 참석을 하니 아침 운동의 기쁨이 사라질정도로 화났다."),
                new ChatMessage(USER, "점심 식사, 만족, 150"),
                new ChatMessage(ASSISTANT, "그래도 점심 식사를 하니 꽤나 만족스러웠다."),
                new ChatMessage(USER, "업무 마감, 성취감, 450"),
                new ChatMessage(ASSISTANT, "업무 마감까지 하니 매우 성취감이 컸다."),
                new ChatMessage(USER, "ㅇㅁㄴㅇ, 분노, 50"),
                new ChatMessage(ASSISTANT, "이유 없이 분노가 좀 차올랐다."),
                new ChatMessage(USER, "재생성 해줘"),
                new ChatMessage(SYSTEM, "Rewrite the diary with a different perspective or tone while keeping it coherent."),
                new ChatMessage(USER, bodyPart)
        );

        //4. 프롬프트와 생성하는 함수와 GPT API를 실행
        String content = getOpenAiResult(generatePrompt(systemPrompt, chatPrompt));
        return DiaryResponseDTO.DiaryCreationResponseDTO
                .builder().content(content).bodyPart(bodyPart).build();
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

    @Override
    public SimpleDiaryDTO saveDiary(DiarySaveDTO saveDTO, MemberInfo memberInfo, MultipartFile image){
        //이미지 받아오는거 설정해야함!
        DailyDiary diary = DailyDiary.builder()
                .date(saveDTO.getDate())
                .memberInfo(memberInfo)
                .impressiveThing(saveDTO.getImpressiveThing())
                .title(saveDTO.getTitle())
                .content(saveDTO.getContent())
                .imagePath(image.toString()).build();

        diaryRepository.save(diary);

        return new SimpleDiaryDTO(diary);
    }

}
