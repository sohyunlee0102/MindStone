package Spring.MindStone.service.diaryService;

import Spring.MindStone.apiPayload.code.status.ErrorStatus;
import Spring.MindStone.apiPayload.exception.handler.MemberInfoHandler;
import Spring.MindStone.domain.diary.DailyDiary;
import Spring.MindStone.domain.diary.DailyEmotionStatistic;
import Spring.MindStone.domain.diary.DiaryImage;
import Spring.MindStone.domain.emotion.EmotionNote;
import Spring.MindStone.domain.enums.EmotionList;
import Spring.MindStone.domain.member.MemberInfo;
import Spring.MindStone.repository.diaryRepository.DiaryImageRepository;
import Spring.MindStone.repository.diaryRepository.DiaryRepository;
import Spring.MindStone.repository.memberRepository.MemberInfoRepository;
import Spring.MindStone.service.GptService;
import Spring.MindStone.service.emotionNoteService.EmotionNoteQueryService;
import Spring.MindStone.service.FileService;
import Spring.MindStone.service.memberInfoService.MemberInfoService;
import Spring.MindStone.web.dto.diaryDto.DiaryResponseDTO;
import Spring.MindStone.web.dto.diaryDto.DiarySaveDTO;
import Spring.MindStone.web.dto.diaryDto.DiaryUpdateDTO;
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
    private final DiaryQueryService diaryQueryService;
    private final DiaryRepository diaryRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final MemberInfoService memberInfoService;
    private final FileService fileService;
    private final DiaryImageRepository diaryImageRepository;
    private final DiaryQueryServiceImpl diaryQueryServiceImpl;
    private final GptService gptService;

    private final String USER = ChatMessageRole.USER.value();
    private final String ASSISTANT = ChatMessageRole.ASSISTANT.value();
    private final String SYSTEM = ChatMessageRole.SYSTEM.value();
    private final DailyEmotionStatisticService dailyEmotionStatisticService;

    public void checkGPTCount(Long memberId){
        DailyEmotionStatistic statisticEntity = dailyEmotionStatisticService.getStatisticEntity(memberId);
        if(statisticEntity.getDiaryAutoCreationCount() > 0){
            statisticEntity.setDiaryAutoCreationCount(statisticEntity.getDiaryAutoCreationCount() - 1);
            dailyEmotionStatisticService.updateStatistic(statisticEntity);
        }else{
            throw new MemberInfoHandler(ErrorStatus.NO_MORE_AI_COUNT);
        }
    }

    @Override
    public DiaryResponseDTO.DiaryCreationResponseDTO createDiary(Long id, LocalDate date) {
        //0. 사용자가 이미 ai생성 횟수를 다 썼는지 체크
        checkGPTCount(id);

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
        String content = gptService.getOpenAiResult(gptService.generateDiaryPrompt(chatPrompt));
        return DiaryResponseDTO.DiaryCreationResponseDTO
                .builder().content(content).bodyPart(result).build();
    }

    @Override
    public DiaryResponseDTO.DiaryCreationResponseDTO createDiaryRe(Long id, String bodyPart, LocalDate date){
        //0. 사용자가 이미 ai생성 횟수를 다 썼는지 체크
        checkGPTCount(id);

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
        String content = gptService.getOpenAiResult(gptService.generateDiaryPrompt(chatPrompt));
        return DiaryResponseDTO.DiaryCreationResponseDTO
                .builder().content(content).bodyPart(bodyPart).build();
    }



    public SimpleDiaryDTO updateDiary(DiaryUpdateDTO updateDTO, Long memberId, List<MultipartFile> image){
        //날짜
        DailyDiary diary =diaryRepository.findDailyDiaryByDate(memberId, updateDTO.getDate())
                .orElseThrow(() ->new MemberInfoHandler(ErrorStatus.DIARY_NOT_FOUND));

        if(!memberId.equals(diary.getMemberInfo().getId())){
            throw new MemberInfoHandler(ErrorStatus.DIARY_ISNT_MINE);
        }

        diary.update(updateDTO);//내용이나 돌 모양이 변형

        //이미지의 수정 사항이 있다면 이미지 변경 진행
        if(!image.isEmpty()){
            List<DiaryImage> diaryImageList = diary.getDiaryImageList();

            //1. 이미지 리스트의 aws와 레포에서 삭제
            for (DiaryImage diaryImage : diaryImageList) {
                fileService.deleteFile(diaryImage.getImagePath());
                //diaryImageRepository.delete(diaryImage);
            }
            //diary repo에서도 밀어버리기, orphan true라 claer하면 다 지워짐
            diary.getDiaryImageList().clear();

            //새로 바뀐 이미지 리스트 저장
            diary.setDiaryImageList(setAwsStore(diary, image));
        }

        //바뀐 내용 반영
        diaryRepository.save(diary);

        return new SimpleDiaryDTO(diary);
    }

    @Override
    public SimpleDiaryDTO saveDiary(DiarySaveDTO saveDTO, Long memberId,List<MultipartFile> image){
        MemberInfo memberInfo = memberInfoService.findMemberById(memberId);

        //이미지만 설정안했음!
        DailyDiary diary = DailyDiary.builder()
                .date(saveDTO.getDate())
                .emotion(EmotionList.fromString(saveDTO.getEmotion()))
                .memberInfo(memberInfo)
                .impressiveThing(saveDTO.getImpressiveThing())
                .title(saveDTO.getTitle())
                .content(saveDTO.getContent())
                .build();


        //diary에 이미지 리스트들 저장
        diary.setDiaryImageList(setAwsStore(diary, image));

        //일기 레포에 일기 저장
        diaryRepository.save(diary);

        return new SimpleDiaryDTO(diary);
    }

    public SimpleDiaryDTO deleteDiary(Long id, Long memberId){
        MemberInfo memberInfo = memberInfoService.findMemberById(memberId);
        DailyDiary diary =diaryRepository.findDailyDiaryByDate(memberId, LocalDate.now())
                .orElseThrow(() ->new MemberInfoHandler(ErrorStatus.DIARY_NOT_FOUND));

        if(!memberId.equals(diary.getMemberInfo().getId())){
            throw new MemberInfoHandler(ErrorStatus.DIARY_ISNT_MINE);
        }

        List<DiaryImage> diaryImageList = diary.getDiaryImageList();

        //1. 이미지 리스트의 aws와 레포에서 삭제
        for (DiaryImage diaryImage : diaryImageList) {
            fileService.deleteFile(diaryImage.getImagePath());
            //diaryImageRepository.delete(diaryImage);
        }
        diary.getDiaryImageList().clear();
        memberInfo.removeDiary(diary);//멤버에서도 지워줌

        diaryRepository.delete(diary);

        return new SimpleDiaryDTO(diary);
    }

    public List<DiaryImage> setAwsStore(DailyDiary diary,List<MultipartFile> image){

        //diary에 이미지들 매핑시키기 위한 list
        List<DiaryImage> diaryImageList = new ArrayList<>();

        for(int i = 0;i<image.size();i++){
            DiaryImage diaryImage = DiaryImage.builder()
                    .imagePath(fileService.uploadFile(image.get(i)))//이 부분이 aws
                    .imageOrder(i)
                    .diary(diary).build();
            //diaryImageRepo에 먼저 저장
            diaryImageList.add(diaryImage);
            diaryImageRepository.save(diaryImage);
        }

        return diaryImageList;
    }

}
