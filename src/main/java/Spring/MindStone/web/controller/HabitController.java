package Spring.MindStone.web.controller;

import Spring.MindStone.apiPayload.ApiResponse;
import Spring.MindStone.config.jwt.JwtTokenUtil;
import Spring.MindStone.service.habitService.HabitHistoryService;
import Spring.MindStone.service.habitService.HabitService;
import Spring.MindStone.web.dto.habitDto.HabitRequestDto;
import Spring.MindStone.web.dto.habitDto.HabitResponseDto;
import Spring.MindStone.web.dto.memberDto.MemberInfoRequestDTO;
import Spring.MindStone.web.dto.memberDto.MemberInfoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/habits")
public class HabitController {

    private final HabitService habitService;
    private final HabitHistoryService habitHistoryService;

    @GetMapping
    @Operation(summary = "사용자의 모든 습관 조회 API", description = "사용자의 전체 습관 목록을 조회하는 API 입니다.")
    public ApiResponse<List<HabitResponseDto.GetHabitDTO>> getAllHabits(
            @RequestHeader("Authorization") String authorization) {

        Long memberId = JwtTokenUtil.extractMemberId(authorization);
        return ApiResponse.onSuccess(habitService.getAllHabits(memberId));
    }

    @PostMapping
    @Operation(summary = "습관 추가 API", description = "새로운 습관을 생성하는 API 입니다." +
            "HabitColor의 경우 PURPLE, ORANGE, BLUE, GRAY, GREEN, YELLOW, PINK 중 하나로 입력해주시면 됩니다." +
            "각 필드의 대한 설명은 아래 HabitDto를 참고해주세요.")
    public ApiResponse<HabitResponseDto.HabitDTO> createHabit(@Valid @RequestBody HabitRequestDto.HabitDto request,
                                                              @RequestHeader("Authorization") String authorization) {
        Long memberId = JwtTokenUtil.extractMemberId(authorization);
        return ApiResponse.onSuccess(habitService.createHabit(request, memberId));
    }

    @PatchMapping
    @Operation(summary = "습관 수정 API", description = "습관을 수정하는 API 입니다.")
    public ApiResponse<HabitResponseDto.HabitDTO> updateHabit(@RequestBody HabitRequestDto.HabitUpdateDto request,
                                                              @RequestHeader("Authorization") String authorization) {
        Long memberId = JwtTokenUtil.extractMemberId(authorization);
        return ApiResponse.onSuccess(habitService.updateHabit(request, memberId));
    }

    @GetMapping("/home")
    @Operation(summary = "홈 화면에 습관 띄우기 API", description = "유저가 설정한 시간이 되었을 때 해당 습관을 홈 화면에 띄우는 API 입니다.")
    public ApiResponse<List<HabitResponseDto.GetHabitForNowDTO>> getHabitsForNow (
            @RequestHeader("Authorization") String authorization) {

        Long memberId = JwtTokenUtil.extractMemberId(authorization);
        return ApiResponse.onSuccess(habitService.getHabitsForNow(memberId));
    }

    @GetMapping("/habitAlarm")
    @Operation(summary = "습관 알림 API", description = "습관 알림(푸시, 홈 알림창)을 위한 API 입니다." +
            "null이 반환되면 그 날에 이미 기록을 하여 알림을 보낼 필요가 없고, 그렇지 않으면 마지막 기록일로부터 반환된 값 만큼 일 수가 흐른 것입니다.")
    public ApiResponse<HabitResponseDto.GetLastHabitHistoryDTO> getLastHabitHistory (@RequestHeader("Authorization") String authorization) {
        Long memberId = JwtTokenUtil.extractMemberId(authorization);

        HabitResponseDto.GetLastHabitHistoryDTO response = habitHistoryService.getLastHabitHistory(memberId);

        if (response == null) {
            return ApiResponse.onSuccess(null);
        }

        return ApiResponse.onSuccess(response);
    }

    @GetMapping("/habitHistory/{date}")
    @Operation(summary = "특정 날짜 습관 가져오기 API", description = "습관 달력의 특정 날짜에 기록한 습관 기록을 가져오는 API 입니다")
    public ApiResponse<List<HabitResponseDto.HabitHistoryDTO>> getHabitsForNow (@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                                                                  @RequestHeader("Authorization") String authorization) {
        Long memberId = JwtTokenUtil.extractMemberId(authorization);
        List<HabitResponseDto.HabitHistoryDTO> habitsForDate = habitHistoryService.getHabitsForDate(memberId, date);

        return ApiResponse.onSuccess(habitsForDate);
    }

    @PostMapping("/habitHistory")
    @Operation(summary = "습관 기록하기 API", description = "특정 날짜의 습관 기록을 추가하는 API 입니다")
    public ApiResponse<Long> addHabitHistory(@Valid @RequestBody HabitRequestDto.CreateHabitHistoryDto request,
                                             @RequestHeader("Authorization") String authorization) {
        Long memberId = JwtTokenUtil.extractMemberId(authorization);
        return ApiResponse.onSuccess(habitHistoryService.addHabitHistory(memberId, request));
    }

    @PatchMapping("/habitHistory")
    @Operation(summary = "습관 기록 수정 API", description = "특정 날짜의 습관 기록을 수정하는 API 입니다")
    public ApiResponse<Long> updateHabitHistory(@Valid @RequestBody HabitRequestDto.UpdateHabitHistoryDto request) {
        return ApiResponse.onSuccess(habitHistoryService.updateHabitHistory(request));
    }

}
