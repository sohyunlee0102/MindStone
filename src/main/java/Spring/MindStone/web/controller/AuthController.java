package Spring.MindStone.web.controller;

import Spring.MindStone.apiPayload.ApiResponse;
import Spring.MindStone.service.authService.AuthService;
import Spring.MindStone.service.emailService.EmailService;
import Spring.MindStone.service.memberInfoService.MemberInfoService;
import Spring.MindStone.service.tokenBlacklistService.TokenBlacklistService;
import Spring.MindStone.web.dto.memberDto.MemberInfoRequestDTO;
import Spring.MindStone.web.dto.memberDto.MemberInfoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final MemberInfoService memberInfoService;
    private final EmailService emailService;
    private final TokenBlacklistService tokenBlacklistService;

    @PostMapping("/login")
    @Operation(summary = "로그인 API", description = "유저 로그인을 처리하는 API 입니다.")
    public ApiResponse<MemberInfoResponseDTO.LoginResponseDto> login(@Valid @RequestBody MemberInfoRequestDTO.LoginDto request) {
        return ApiResponse.onSuccess(authService.login(request));
    }

    @PostMapping("/refreshToken")
    @Operation(summary = "Refresh Token API", description = "Access Token 을 재발급 하는 API 입니다.")
    public ApiResponse<MemberInfoResponseDTO.LoginResponseDto> refresh(@Valid @RequestBody MemberInfoRequestDTO.RefreshTokenDTO request) {
        return ApiResponse.onSuccess(authService.refreshAccessToken(request.getRefreshToken(), request.getEmail()));
    }

    @PostMapping("/email")
    @Operation(summary = "Find email API & check duplication email API", description = "이메일 찾기/이메일 중복 확인 API 입니다.")
    public ApiResponse<MemberInfoResponseDTO.EmailDto> findEmail(@Valid @RequestBody MemberInfoRequestDTO.EmailDto request) {
        return ApiResponse.onSuccess(memberInfoService.findEmail(request));
    }

    @PostMapping("/send")
    @Operation(summary = "Send Verification Code API", description = "인증번호 전송 API 입니다.")
    public ApiResponse<String> sendVerificationCode(@Valid @RequestBody MemberInfoRequestDTO.EmailDto request) {
        emailService.handleVerificationCode(request);
        return ApiResponse.onSuccess("인증번호가 성공적으로 전송되었습니다.");
    }

    @PostMapping("/verify")
    @Operation(summary = "Verify Verification Code API", description = "인증번호 일치 여부 확인 API 입니다.")
    public ApiResponse<MemberInfoResponseDTO.EmailDto> sendVerificationCode(@Valid @RequestBody MemberInfoRequestDTO.VerificationDto request) {
        return ApiResponse.onSuccess(emailService.verifyCode(request));
    }

    @PostMapping("/tempPassword")
    @Operation(summary = "Send Temporary Password API", description = "임시 비밀번호 전송 API 입니다.")
    public ApiResponse<String> sendTemporaryPassword(@Valid @RequestBody MemberInfoRequestDTO.EmailDto request) {
        emailService.handleTemporaryPassword(request);
        return ApiResponse.onSuccess("임시 비밀번호가 성공적으로 전송되었습니다.");
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout API", description = "로그아웃 API 입니다. 토큰 탈취 공격 방지를 위해 RefreshToken 을 보내주세요.")
    public ApiResponse<String> logout(@Valid @RequestBody MemberInfoRequestDTO.LogoutDto request) {
        tokenBlacklistService.addToBlackList(request.getRefreshToken());
        return ApiResponse.onSuccess("로그아웃이 완료되었습니다.");
    }

}
