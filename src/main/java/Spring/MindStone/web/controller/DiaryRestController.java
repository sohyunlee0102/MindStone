package Spring.MindStone.web.controller;


import Spring.MindStone.apiPayload.ApiResponse;
import Spring.MindStone.domain.member.MemberInfo;
import Spring.MindStone.config.jwt.JwtTokenUtil;
import Spring.MindStone.service.DiaryService.DiaryCommandService;
import Spring.MindStone.service.DiaryService.DiaryQueryService;
import Spring.MindStone.web.dto.diaryDto.DiaryRequestDTO;
import Spring.MindStone.web.dto.diaryDto.DiaryResponseDTO;
import Spring.MindStone.web.dto.diaryDto.DiarySaveDTO;
import Spring.MindStone.web.dto.diaryDto.SimpleDiaryDTO;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/diary")
@RequiredArgsConstructor
@Tag(name = "Diary", description = "Diary API")
public class DiaryRestController {
    private final DiaryCommandService diaryCommandService;
    private final DiaryQueryService diaryQueryService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/create")
    @Operation(summary = "일기 자동생성", description = "하루 마무리에서 일기 자동생성해주는 기능")
    public ApiResponse<DiaryResponseDTO.DiaryCreationResponseDTO> createAutoDiary
            (@Valid @RequestBody DiaryRequestDTO.DiaryCreationRequestDTO diaryRequest,
             @RequestHeader("Authorization") String authorization) {
        Long memberId = JwtTokenUtil.extractMemberId(authorization);
        System.out.println(memberId);
        return ApiResponse.onSuccess(diaryCommandService.createDiary(memberId, diaryRequest.getDate()));
    }

    @PostMapping("/recreate")
    @Operation(summary = "일기 재생성", description = "사용자가 일기를 재생성할때의 기능(지금은 테스트용)")
    public ApiResponse<DiaryResponseDTO.DiaryCreationResponseDTO> createAutoDiaryRe
            (@Valid @RequestBody DiaryRequestDTO.DiaryCreationRequestDTO diaryRequest,
             @RequestHeader("Authorization") String authorization) {
        System.out.println("GET api/diary/recreate");
        System.out.println(diaryRequest.getBodyPart());
        Long memberId = JwtTokenUtil.extractMemberId(authorization);
        return ApiResponse.onSuccess(diaryCommandService.createDiaryRe(memberId, diaryRequest.getBodyPart(), diaryRequest.getDate()));
    }


    @GetMapping("/{memberId}/{date}")
    @Operation(summary = "일기 요청", description = "특정날짜 기준 일기 요청")
    public ApiResponse<DiaryResponseDTO.DiaryGetResponseDTO> getDiary
            (@PathVariable("memberId") Long id,
             @PathVariable("date") LocalDate date){
        System.out.println("GET api/diary/{memberId}/{date}");
        return ApiResponse.onSuccess(diaryQueryService.getDiaryByDate(id,date));
    }

    @PostMapping("/save")
    @Operation(summary = "일기 저장", description = "일기 내용이 마음에 들때 저장요청")
    public ApiResponse<SimpleDiaryDTO> saveDiary(
            @Valid @RequestBody DiarySaveDTO diaryDTO,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestHeader("Authorization") String authorization){
        System.out.println("POST api/diary/save");
        Long memberId = JwtTokenUtil.extractMemberId(authorization);
        return ApiResponse.onSuccess(diaryCommandService.saveDiary(diaryDTO,memberId,image));
    }
}
