package Spring.MindStone.service.authService;

import Spring.MindStone.apiPayload.code.status.ErrorStatus;
import Spring.MindStone.apiPayload.exception.handler.AuthHandler;
import Spring.MindStone.config.jwt.JwtTokenUtil;
import Spring.MindStone.service.memberInfoService.MemberInfoService;
import Spring.MindStone.service.tokenBlacklistService.TokenBlacklistService;
import Spring.MindStone.web.dto.memberDto.MemberInfoRequestDTO;
import Spring.MindStone.web.dto.memberDto.MemberInfoResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final MemberInfoService memberInfoService;
    private final TokenBlacklistService tokenBlacklistService;

    // 로그인 및 JWT 토큰 발급
    @Transactional
    public MemberInfoResponseDTO.LoginResponseDto login(MemberInfoRequestDTO.LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
            );

            String accessToken = JwtTokenUtil.generateAccessToken(loginDto.getEmail(), memberInfoService);
            String refreshToken = JwtTokenUtil.generateRefreshToken(loginDto.getEmail(), memberInfoService);
            System.out.println(JwtTokenUtil.getExpirationDate(refreshToken));

            return MemberInfoResponseDTO.LoginResponseDto.builder()
                    .email(loginDto.getEmail())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .createdAt(LocalDateTime.now())
                    .build();
        } catch (AuthenticationException e) {
            throw new AuthHandler(ErrorStatus.AUTHENTICATION_FAILED);
        }
    }

    // accessToken 재발급
    @Transactional
    public MemberInfoResponseDTO.LoginResponseDto refreshAccessToken(String refreshToken, String email) {

        if (tokenBlacklistService.isTokenBlacklisted(refreshToken)) {
            throw new AuthHandler(ErrorStatus.LOGOUT_TOKEN);
        }

        try {
            JwtTokenUtil.validateToken(refreshToken);
        } catch (JwtTokenUtil.InvalidTokenException e) {
            throw new AuthHandler(ErrorStatus.AUTHENTICATION_FAILED);
        } catch (JwtTokenUtil.TokenExpiredException e) {
            throw new AuthHandler(ErrorStatus.TOKEN_EXPIRED);
        }

        String newAccessToken = JwtTokenUtil.generateAccessToken(email, memberInfoService);

        return MemberInfoResponseDTO.LoginResponseDto.builder()
                .email(email)
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .createdAt(LocalDateTime.now())
                .build();

    }

}
