package Spring.MindStone.web.controller;

import Spring.MindStone.apiPayload.ApiResponse;
import Spring.MindStone.service.DiaryService.DiaryCommandService;
import Spring.MindStone.service.DiaryService.DiaryCommandServiceImpl;
import Spring.MindStone.web.dto.DiaryRequestDTO;
import Spring.MindStone.web.dto.DiaryResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryRestController {
    private final DiaryCommandServiceImpl diaryCommandService;

    @GetMapping("/create")
    @Tag(name = "Response Estimate", description = "Response Estimate API")
    @Operation(summary = "일기 자동생성", description = "하루 마무리에서 일기 자동생성해주는 기능")
    @Parameters({
            @Parameter(name = "date", description = "날짜", example = "2025-01-25")
    })
    public ApiResponse<DiaryResponseDTO.DiaryCreationResponseDTO> createAutoDiary(@RequestBody DiaryRequestDTO.DiaryCreationRequestDTO diaryRequest){
            return ApiResponse.onSuccess(diaryCommandService.createDiary(diaryRequest.getId(), diaryRequest.getDate()));
    }

    @PostMapping("/recreate")
    @Tag(name = "Response")
    @Operation(summary = "일기 자동생성", description = "하루 마무리에서 일기 자동생성해주는 기능")
    @Parameters({
            @Parameter(name = "date", description = "날짜", example = "2025-01-25"),
            @Parameter(name = "bodyPart", description = "일기 내용", example = "아침식사,기쁨,50\n비맞아서우울함,우울,100")
    })
    public ApiResponse<DiaryResponseDTO.DiaryCreationResponseDTO> createAutoDiaryRe(@RequestBody DiaryRequestDTO.DiaryCreationRequestDTO diaryRequest){
        return ApiResponse.onSuccess(diaryCommandService.createDiaryRe(diaryRequest.getId(), diaryRequest.getBodyPart(), diaryRequest.getDate()));
    }

}
