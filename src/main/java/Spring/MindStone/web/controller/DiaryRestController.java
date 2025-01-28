package Spring.MindStone.web.controller;


import Spring.MindStone.apiPayload.ApiResponse;
import Spring.MindStone.service.DiaryService.DiaryCommandService;
import Spring.MindStone.service.DiaryService.DiaryQueryService;
import Spring.MindStone.web.dto.diaryDto.DiaryRequestDTO;
import Spring.MindStone.web.dto.diaryDto.DiaryResponseDTO;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/diary")
@RequiredArgsConstructor
@Tag(name = "Diary", description = "Diary API")
public class DiaryRestController {
    private final DiaryCommandService diaryCommandService;
    private final DiaryQueryService diaryQueryService;


    @PostMapping("/create")
    @Operation(summary = "일기 자동생성", description = "하루 마무리에서 일기 자동생성해주는 기능")
    public ApiResponse<DiaryResponseDTO.DiaryCreationResponseDTO> createAutoDiary
            (@Valid @RequestBody DiaryRequestDTO.DiaryCreationRequestDTO diaryRequest){
            return ApiResponse.onSuccess(diaryCommandService.createDiary(diaryRequest.getId(), diaryRequest.getDate()));
    }

    @PostMapping("/recreate")
    @Operation(summary = "일기 재생성", description = "사용자가 일기를 재생성할때의 기능(지금은 테스트용)")
    public ApiResponse<DiaryResponseDTO.DiaryCreationResponseDTO> createAutoDiaryRe
            (@Valid @RequestBody DiaryRequestDTO.DiaryCreationRequestDTO diaryRequest){
        System.out.println("GET api/diary/recreate");
        System.out.println(diaryRequest.getBodyPart());
        return ApiResponse.onSuccess(diaryCommandService.createDiaryRe(diaryRequest.getId(), diaryRequest.getBodyPart(), diaryRequest.getDate()));
    }

    @GetMapping("/{memberId}/{date}")
    @Operation(summary = "일기 요청", description = "특정날짜 기준 일기 요청")
    public ApiResponse<DiaryResponseDTO.DiaryGetResponseDTO> getDiary
            (@PathVariable("memberId") Long id,
             @PathVariable("date") LocalDate date){
        System.out.println("GET api/diary/{memberId}/{date}");
        return ApiResponse.onSuccess(diaryQueryService.getDiaryByDate(id,date));
    }


}
