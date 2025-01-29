package Spring.MindStone.web.controller;

import Spring.MindStone.apiPayload.ApiResponse;
import Spring.MindStone.config.jwt.JwtTokenUtil;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/habits")
public class HabitController {

    private final HabitService habitService;

    @GetMapping
    @Operation(summary = "사용자의 모든 습관 조회 API", description = "사용자의 전체 습관 목록을 조회하는 API 입니다.")
    public ApiResponse<List<HabitResponseDto.GetHabitDTO>> getAllHabits(
            @RequestHeader("Authorization") String authorization) {

        Long memberId = JwtTokenUtil.extractMemberId(authorization);
        return ApiResponse.onSuccess(habitService.getAllHabits(memberId));
    }

    @PostMapping
    @Operation(summary = "습관 추가 API", description = "새로운 습관을 생성하는 API 입니다." +
            "HabitColor의 경우 PURPLE, ORANGE, BLUE, GRAY, GREEN, YELLOW, PINK 중 하나로 입력해주시면 됩니다.")
    public ApiResponse<HabitResponseDto.HabitDTO> createHabit(@Valid @RequestBody HabitRequestDto.HabitDto request,
                                                              @RequestHeader("Authorization") String authorization) {
        Long memberId = JwtTokenUtil.extractMemberId(authorization);
        return ApiResponse.onSuccess(habitService.createHabit(request, memberId));
    }

    @PatchMapping
    @Operation(summary = "습관 수정 API", description = "습관을 수정하는 API 입니다.")
    public ApiResponse<HabitResponseDto.HabitDTO> updateHabit(@RequestBody HabitRequestDto.HabitDto request,
                                                              @RequestHeader("Authorization") String authorization) {
        Long memberId = JwtTokenUtil.extractMemberId(authorization);
        return ApiResponse.onSuccess(habitService.updateHabit(request, memberId));
    }

}
