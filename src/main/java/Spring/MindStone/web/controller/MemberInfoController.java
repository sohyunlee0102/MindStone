package Spring.MindStone.web.controller;

import Spring.MindStone.apiPayload.ApiResponse;
import Spring.MindStone.config.jwt.JwtTokenUtil;
import Spring.MindStone.domain.member.MemberInfo;
import Spring.MindStone.web.dto.memberDto.MemberInfoRequestDTO;
import Spring.MindStone.web.dto.memberDto.MemberInfoResponseDTO;
import Spring.MindStone.service.memberInfoService.MemberInfoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Validated
public class MemberInfoController {

    private final MemberInfoService memberInfoService;

    @PostMapping
    @Operation(summary = "회원가입 API", description = "유저 회원가입을 처리하는 API 입니다.")
    public ApiResponse<MemberInfoResponseDTO.JoinResultDTO> join(@Valid @RequestBody MemberInfoRequestDTO.JoinDto request) {
        return ApiResponse.onSuccess(memberInfoService.joinMember(request));
    }

    @GetMapping("/info")
    @Operation(summary = "유저 정보 API", description = "마이페이지에서 유저 프로필 정보를 가져오는 API 입니다.")
    public ApiResponse<MemberInfoResponseDTO.MemberProfileDto> getMemberInfo(@RequestHeader("Authorization") String authorization) {
        Long memberId = JwtTokenUtil.extractMemberId(authorization);
        MemberInfo memberInfo = memberInfoService.findMemberById(memberId);
        return ApiResponse.onSuccess(new MemberInfoResponseDTO.MemberProfileDto(memberInfo.getEmail(), memberInfo.getNickname(), LocalDateTime.now()));
    }

    @PatchMapping("/nickname")
    @Operation(summary = "닉네임 변경 API", description = "마이페이지에서 닉네임을 변경하는 API 입니다.")
    public ApiResponse<Map<Long, LocalDateTime>> patchNickname(@Valid @RequestBody MemberInfoRequestDTO.NicknameDto request,
                                               @RequestHeader("Authorization") String authorization) {
        Long memberId = JwtTokenUtil.extractMemberId(authorization);
        return ApiResponse.onSuccess(Map.of(memberInfoService.updateNickname(request, memberId), LocalDateTime.now()));
    }

    @PatchMapping("/password")
    @Operation(summary = "비밀번호 변경 API", description = "마이페이지에서 비밀번호를 변경하는 API 입니다.")
    public ApiResponse<Map<Long, LocalDateTime>> patchPassword(@Valid @RequestBody MemberInfoRequestDTO.PasswordDto request,
                                           @RequestHeader("Authorization") String authorization) {
        Long memberId = JwtTokenUtil.extractMemberId(authorization);
        return ApiResponse.onSuccess(Map.of(memberInfoService.updatePassword(request, memberId), LocalDateTime.now()));
    }

}
