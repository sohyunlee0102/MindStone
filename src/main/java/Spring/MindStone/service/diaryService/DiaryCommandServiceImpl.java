package Spring.MindStone.service.diaryService;

import Spring.MindStone.apiPayload.code.status.ErrorStatus;
import Spring.MindStone.apiPayload.exception.handler.MemberInfoHandler;
import Spring.MindStone.domain.diary.DailyDiary;
import Spring.MindStone.domain.diary.DailyEmotionStatistic;
import Spring.MindStone.domain.diary.DiaryImage;
import Spring.MindStone.domain.emotion.EmotionNote;
import Spring.MindStone.domain.emotion.StressEmotionNote;
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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private void checkGPTCount(Long memberId){
        DailyEmotionStatistic statisticEntity = dailyEmotionStatisticService.getStatisticEntity(memberId);
        if(statisticEntity.getDiaryAutoCreationCount() > 0){
            statisticEntity.setDiaryAutoCreationCount(statisticEntity.getDiaryAutoCreationCount() - 1);
            dailyEmotionStatisticService.updateStatistic(statisticEntity);
        }else{
            throw new MemberInfoHandler(ErrorStatus.NO_MORE_AI_COUNT);
        }
    }

    @Override
    @Transactional
    public DiaryResponseDTO.DiaryCreationResponseDTO createDiary(Long id, LocalDate date) {
        //0. 사용자가 이미 ai생성 횟수를 다 썼는지 체크
        checkGPTCount(id);

        //1. EmotionNoteService에서 멤버의 하루 일들을 갖고옴.

        MemberInfo memberInfo = memberInfoService.findMemberById(id);
        List<EmotionNote> emotionNoteList = emotionNoteService.getNotesByIdAndDate(memberInfo, date);
        List<StressEmotionNote> stressEmotionNoteList = emotionNoteService.getStressNotesByIdAndDate(memberInfo, date);


        //두 노트를 합침
        List<Object> sortedList = Stream.concat(emotionNoteList.stream(), stressEmotionNoteList.stream())
                .sorted(Comparator.comparing(note -> note.getCreatedAt())) // createdAt 기준 정렬
                .collect(Collectors.toList());

        //2. 받아온 리스트를 합침.
        String result = sortedList.stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n"));

        System.out.println("일기 자동생성 호출 - 들어간 내용"+result);

        //3.result를 프롬프트와 함께 제작
        List<ChatMessage> chatPrompt = List.of(
                new ChatMessage(USER, "아침 운동, JOY, 60"),
                new ChatMessage(ASSISTANT, "아침에 상쾌하게 운동을 하고 나니 기분이 한결 좋아졌다."),

                new ChatMessage(USER, "출근길, SAD, 30"),
                new ChatMessage(ASSISTANT, "출근길에 사람들도 많고 지치면서 기분이 조금 가라앉았다."),

                new ChatMessage(USER, "회의 참석, ANGER, 80"),
                new ChatMessage(ASSISTANT, "회의 내내 답답한 말만 오가서 점점 화가 치밀었다."),

                new ChatMessage(USER, "커피 한 잔, CALM, 50"),
                new ChatMessage(ASSISTANT, "그 때문에 커피 한 잔을 마시면서 마음을 조금 가라앉혔다."),

                new ChatMessage(USER, "업무 마감, HAPPINESS, 90"),
                new ChatMessage(ASSISTANT, "오늘 하루 일을 끝내고 나니 정말 뿌듯하고 행복했다."),

                new ChatMessage(USER, "ㅇㅁㄴㅇ, ANGER, 20"),
                new ChatMessage(ASSISTANT, "이유 없이 조금 짜증이 났다."),
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
                new ChatMessage(USER, "아침 운동, JOY, 60"),
                new ChatMessage(ASSISTANT, "아침에 상쾌하게 운동을 하고 나니 기분이 한결 좋아졌다."),

                new ChatMessage(USER, "출근길, SAD, 30"),
                new ChatMessage(ASSISTANT, "출근길에 사람들도 많고 지치면서 기분이 조금 가라앉았다."),

                new ChatMessage(USER, "회의 참석, ANGER, 80"),
                new ChatMessage(ASSISTANT, "회의 내내 답답한 말만 오가서 점점 화가 치밀었다."),

                new ChatMessage(USER, "커피 한 잔, CALM, 50"),
                new ChatMessage(ASSISTANT, "그 때문에 커피 한 잔을 마시면서 마음을 조금 가라앉혔다."),

                new ChatMessage(USER, "업무 마감, HAPPINESS, 90"),
                new ChatMessage(ASSISTANT, "오늘 하루 일을 끝내고 나니 정말 뿌듯하고 행복했다."),

                new ChatMessage(USER, "ㅇㅁㄴㅇ, ANGER, 20"),
                new ChatMessage(ASSISTANT, "이유 없이 조금 짜증이 났다."),

                new ChatMessage(USER, "재생성 해줘"),
                new ChatMessage(SYSTEM, "Rewrite the diary with a different perspective or tone while keeping it coherent."),

                new ChatMessage(USER, bodyPart)

        );

        //4. 프롬프트와 생성하는 함수와 GPT API를 실행
        String content = gptService.getOpenAiResult(gptService.generateDiaryPrompt(chatPrompt));
        return DiaryResponseDTO.DiaryCreationResponseDTO
                .builder().content(content).bodyPart(bodyPart).build();
    }



    public SimpleDiaryDTO updateDiary(DiaryUpdateDTO updateDTO, Long memberId, List<MultipartFile> images){
        //날짜
        DailyDiary diary =diaryRepository.findDailyDiaryByDate(memberId, updateDTO.getDate())
                .orElseThrow(() ->new MemberInfoHandler(ErrorStatus.DIARY_NOT_FOUND));

        if(!memberId.equals(diary.getMemberInfo().getId())){
            throw new MemberInfoHandler(ErrorStatus.DIARY_ISNT_MINE);
        }

        diary.update(updateDTO);//내용이나 돌 모양이 변형

        // 3. 이미지 업데이트
        if (images != null && !images.isEmpty()) {
            List<DiaryImage> diaryImageList = diary.getDiaryImageList();

            // 3-1. 기존 이미지 AWS에서 삭제 후 레포에서도 제거
            for (Iterator<DiaryImage> iterator = diaryImageList.iterator(); iterator.hasNext();) {
                DiaryImage diaryImage = iterator.next();
                fileService.deleteFile(diaryImage.getImagePath());
                iterator.remove();  // 리스트에서 안전하게 제거 (JPA orphanRemoval과 충돌 방지)
            }

            // 3-2. 새로운 이미지 리스트 추가
            List<DiaryImage> newImages = setAwsStore(diary, images);
            diaryImageList.addAll(newImages); // 기존 리스트에 추가
        }

        //바뀐 내용 반영
        diaryRepository.save(diary);

        return new SimpleDiaryDTO(diary);
    }

    @Override
    public SimpleDiaryDTO saveDiary(DiarySaveDTO saveDTO, Long memberId, List<MultipartFile> images) {
        // 회원 정보 찾기
        MemberInfo memberInfo = memberInfoService.findMemberById(memberId);

        DailyDiary diary = diaryRepository.findDailyDiaryByDate(memberId, saveDTO.getDate())
                .orElse(null);

        if (diary == null) {
            // 객체가 존재하지 않으면 새로 생성
            diary = DailyDiary.builder()
                    .date(saveDTO.getDate())
                    .emotion(EmotionList.fromString(saveDTO.getEmotion()))
                    .memberInfo(memberInfo)
                    .impressiveThing(saveDTO.getImpressiveThing())
                    .title(saveDTO.getTitle())
                    .content(saveDTO.getContent())
                    .diaryImageList(new ArrayList<>())  // 빈 리스트로 초기화
                    .build();
        } else {
            // 존재하면 업데이트
            diary.setEmotion(EmotionList.fromString(saveDTO.getEmotion()));
            diary.setImpressiveThing(saveDTO.getImpressiveThing());
            diary.setTitle(saveDTO.getTitle());
            diary.setContent(saveDTO.getContent());
        }

        // 일기 레포에 일기 저장 (이 때 diary와 연결된 DiaryImage도 함께 저장)
        diaryRepository.save(diary);  // 일기 객체와 이미지들 모두 저장됨

        // 이미지가 있다면 이미지 리스트 설정
        if (images != null && !images.isEmpty()) {
            List<DiaryImage> diaryImageList = setAwsStore(diary, images); // 이미지 저장
            diary.setDiaryImageList(diaryImageList);  // diary에 이미지 설정
        }

        // 일기 DTO 반환
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
