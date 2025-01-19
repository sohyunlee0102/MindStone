package Spring.MindStone.web.controller;

import Spring.MindStone.apiPayload.ApiResponse;
import Spring.MindStone.service.DiaryService.DiaryCommandService;
import Spring.MindStone.service.DiaryService.DiaryCommandServiceImpl;
import Spring.MindStone.web.dto.DiaryRequestDTO;
import Spring.MindStone.web.dto.DiaryResponseDTO;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryRestController {
    private final DiaryCommandServiceImpl diaryCommandService;

    @GetMapping("/create")
    public ApiResponse<String> createAutoDiary(@RequestBody DiaryRequestDTO.DiaryCreationRequestDTO diaryRequest){
        try {
            String result = diaryCommandService.createDiary(diaryRequest.getId(), diaryRequest.getBodyPart());
            return ApiResponse.onSuccess(result);
        } catch (Exception e) {
            return ApiResponse.onFailure("GPT5001", "GPT에러 오류: " + e.getMessage(), null);
        }
    }

    @GetMapping("/create/hard")
    public ApiResponse<String> createAutoDiaryHard(){
        try {
            String bodyPart = "08:00 AM - Action: 아침 운동, Emotion: 기쁨, Emotion Score: 100\n10:00 AM - Action: 회의 참석, Emotion: 우울, Emotion Score: 300\n12:30 PM - Action: 점심 식사, Emotion: 활발, Emotion Score: 200";
            String result = diaryCommandService.createDiary(1L, bodyPart);
            return ApiResponse.onSuccess(result);
        } catch (Exception e) {
            return ApiResponse.onFailure("GPT5001", "GPT에러 오류: " + e.getMessage(), null);
        }
    }
}
