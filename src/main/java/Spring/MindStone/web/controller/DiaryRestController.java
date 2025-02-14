package Spring.MindStone.web.controller;


import Spring.MindStone.apiPayload.ApiResponse;
import Spring.MindStone.config.jwt.JwtTokenUtil;
import Spring.MindStone.service.diaryService.DiaryCommandService;
import Spring.MindStone.service.diaryService.DiaryQueryService;
import Spring.MindStone.web.dto.diaryDto.*;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

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


    @GetMapping("/{date}")
    @Operation(summary = "일기 날짜기준 요청 요청", description = "특정날짜 기준 일기 요청")
    public ApiResponse<SimpleDiaryDTO> getDiary
            (@PathVariable("date") LocalDate date,
             @RequestHeader("Authorization") String authorization){
        System.out.println("GET api/diary/{date}");
        Long memberId = JwtTokenUtil.extractMemberId(authorization);
        return ApiResponse.onSuccess(diaryQueryService.getDiaryByDate(memberId,date));
    }

    @GetMapping("//{diaryId}")
    @Operation(summary = "일기 Id 기준 요청", description = "일기에 저장된 ID기준으로 요청(달력 처음에 출력할때 그걸 리스트로 넣어놓을 거임))")
    public ApiResponse<SimpleDiaryDTO> getDiary
            (@PathVariable("diaryId") Long diaryId,
             @RequestHeader("Authorization") String authorization){
        System.out.println("GET api/diary/{diaryId}");
        Long memberId = JwtTokenUtil.extractMemberId(authorization);
        return ApiResponse.onSuccess(diaryQueryService.getDiaryById(memberId,diaryId));
    }

    @PostMapping("/save")
    @Operation(summary = "일기 저장", description = "일기 내용이 마음에 들때 저장요청")
    public ApiResponse<SimpleDiaryDTO> saveDiary(
            @Valid @RequestBody DiarySaveDTO diaryDTO,
            @RequestPart(value = "image", required = false) List<MultipartFile> image,
            @RequestHeader("Authorization") String authorization){
        System.out.println("POST api/diary/save");
        Long memberId = JwtTokenUtil.extractMemberId(authorization);
        return ApiResponse.onSuccess(diaryCommandService.saveDiary(diaryDTO,memberId,image));
    }

    @DeleteMapping("/{diaryId}")
    @Operation(summary = "일기삭제")
    public ApiResponse<SimpleDiaryDTO> deleteDiary(
            @PathVariable("diaryId") Long diaryId,
            @RequestHeader("Authorization") String authorization) {
        System.out.println("DELETE api/diary/{diaryId}");
        Long memberId = JwtTokenUtil.extractMemberId(authorization);
        return ApiResponse.onSuccess(diaryCommandService.deleteDiary(diaryId,memberId));
    }

    @PatchMapping("")
    @Operation(summary = "일기수정")
    public ApiResponse<SimpleDiaryDTO> updateDiary(
            @Valid @RequestBody DiaryUpdateDTO diaryDTO,
            @RequestPart(value = "image", required = false) List<MultipartFile> image,
            @RequestHeader("Authorization") String authorization
    ){
        System.out.println("PATCH api/diary");
        Long memberId = JwtTokenUtil.extractMemberId(authorization);
        return ApiResponse.onSuccess(diaryCommandService.updateDiary(diaryDTO,memberId,image));
    }

    @GetMapping("/calendar")
    @Operation(summary = "특정 년월 달력 요청", description = "이거 list로 보내주는거에요")
    @Parameters({
            @Parameter(name = "year", description = "년도"),
            @Parameter(name = "month", description = "월")
    })
    public ApiResponse<List<DiaryCallendarDTO>> getDiaryCalendar(
            @RequestParam("year") @NotNull int year,
            @RequestParam("month") int month,
            @RequestHeader("Authorization") String authorization
    ){
        System.out.println("GET api/diary/calendar");
        Long memberId = JwtTokenUtil.extractMemberId(authorization);
        return ApiResponse.onSuccess(diaryQueryService.getDiaryCalendar(year,month,memberId));
    }

    /*@GetMapping("/calendar/repot")
    @Operation(summary = "특정 년월 달력 요청")
    @Parameters({
            @Parameter(name = "year", description = "년도"),
            @Parameter(name = "month", description = "월")
    })
    public ApiResponse<List<DiaryCallendarDTO>> getCalendarReport(
            @RequestParam("year") @NotNull int year,
            @RequestParam("month") int month,
            @RequestHeader("Authorization") String authorization
    ){
        System.out.println("GET api/diary/calendar/report");
        Long memberId = JwtTokenUtil.extractMemberId(authorization);
        return ApiResponse.onSuccess(diaryQueryService.getDiaryCalendar(year,month,memberId));
    }*/

}
