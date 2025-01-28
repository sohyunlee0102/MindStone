package Spring.MindStone.web.controller;

import Spring.MindStone.apiPayload.ApiResponse;
import Spring.MindStone.service.AuthService.AuthService;
import Spring.MindStone.service.EmailService.EmailService;
import Spring.MindStone.service.MemberInfoService.MemberInfoService;
import Spring.MindStone.web.dto.memberInfoDto.MemberInfoRequestDTO;
import Spring.MindStone.web.dto.memberInfoDto.MemberInfoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final MemberInfoService memberInfoService;
    private final EmailService emailService;

    @PostMapping("/login")
    @Operation(summary = "로그인 API", description = "유저 로그인을 처리하는 API 입니다.")
    @Parameters({
            @Parameter(name = "email", description = "유저의 이메일, Request Body 입니다!"),
            @Parameter(name = "password", description = "유저의 비밀번호, Request Body 입니다!")
    })
    public ApiResponse<MemberInfoResponseDTO.LoginResponseDto> login(@Valid @RequestBody MemberInfoRequestDTO.LoginDto request) {
        return ApiResponse.onSuccess(authService.login(request));
    }

    @PostMapping("/refreshToken")
    @Operation(summary = "Refresh Token API", description = "Access Token 을 재발급 하는 API 입니다.")
    @Parameters({
            @Parameter(name = "email", description = "유저의 이메일, Request Body 입니다!"),
            @Parameter(name = "refreshToken", description = "refreshToken, Request Body 입니다!")
    })
    public ApiResponse<MemberInfoResponseDTO.LoginResponseDto> refresh(@Valid @RequestBody MemberInfoRequestDTO.RefreshTokenDTO request) {
        return ApiResponse.onSuccess(authService.refreshAccessToken(request.getRefreshToken(), request.getEmail()));
    }

    @PostMapping("/email")
    @Operation(summary = "Find email API", description = "이메일 찾기 API 입니다.")
    @Parameters({
            @Parameter(name = "email", description = "유저의 이메일, Request Body 입니다!"),
    })
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
    @Parameters({
            @Parameter(name = "email", description = "유저의 이메일, Request Body 입니다!"),
            @Parameter(name = "code", description = "유저가 입력한 인증번호, Request Body 입니다!"),
    })
    public ApiResponse<MemberInfoResponseDTO.EmailDto> sendVerificationCode(@Valid @RequestBody MemberInfoRequestDTO.VerificationDto request) {
        return ApiResponse.onSuccess(emailService.verifyCode(request));
    }

    @PostMapping("/tempPassword")
    @Operation(summary = "Send Temporary Password API", description = "임시 비밀번호 전송 API 입니다.")
    public ApiResponse<String> sendTemporaryPassword(@Valid @RequestBody MemberInfoRequestDTO.EmailDto request) {
        emailService.handleTemporaryPassword(request);
        return ApiResponse.onSuccess("임시 비밀번호가 성공적으로 전송되었습니다.");
    }

}
