package Spring.MindStone.config.jwt;

import Spring.MindStone.apiPayload.code.status.ErrorStatus;
import Spring.MindStone.apiPayload.exception.handler.AuthHandler;
import Spring.MindStone.apiPayload.exception.handler.MemberInfoHandler;
import Spring.MindStone.domain.member.MemberInfo;
import Spring.MindStone.service.memberInfoService.MemberInfoService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtTokenUtil {

    private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 30; // 30분
    private static final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000; // 7일

    @Value("${spring.jwt.secret-key}")
    private String secretKey;

    private static SecretKey SECRET_KEY;
    @PostConstruct
    public void init() {
        SECRET_KEY = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // AccessToken 생성
    public static String generateAccessToken(String email, MemberInfoService memberInfoService) {

        MemberInfo memberInfo = memberInfoService.findMemberByEmail(email);

        if (memberInfo == null) {
            throw new MemberInfoHandler(ErrorStatus.MEMBER_NOT_FOUND);
        }

        return Jwts.builder()
                .setSubject(email)
                .setId(memberInfo.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // RefreshToken 생성
    public static String generateRefreshToken(String email, MemberInfoService memberInfoService) {

        MemberInfo memberInfo = memberInfoService.findMemberByEmail(email);

        if (memberInfo == null) {
            throw new MemberInfoHandler(ErrorStatus.MEMBER_NOT_FOUND);
        }

        return Jwts.builder()
                .setSubject(email)
                .setId(memberInfo.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰 검증
    public static Claims validateToken(String token) {
        try {
            System.out.println("Token : " + token);
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new AuthHandler(ErrorStatus.TOKEN_EXPIRED);
        } catch (JwtException e) {
            throw new AuthHandler(ErrorStatus.INVALID_TOKEN);
        }
    }

    public static Long extractMemberId(String authorization) {
        String accessToken = authorization.substring(7);
        Claims claims = validateToken(accessToken);
        String memberId = claims.getId();
        if (memberId == null || memberId.isEmpty()) {
            throw new AuthHandler(ErrorStatus.INVALID_TOKEN);
        }
        return Long.parseLong(memberId);
    }

    // 유저 이메일 추출 (온보딩 사용 - 임시)
    public static String extractUserEmail(String token) {
        String accessToken = token.substring(7);
        Claims claims = validateToken(accessToken);
        String email = claims.getSubject();
        if (email == null || email.isEmpty()) {
            throw new AuthHandler(ErrorStatus.INVALID_TOKEN);  // 커스텀 예외 처리
        }
        return email;
    }

    public static LocalDateTime getExpirationDate(String token) {
        Claims claims = validateToken(token);
        return claims.getExpiration()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static class InvalidTokenException extends RuntimeException {
        public InvalidTokenException(String message) {
            super(message);
        }
    }

    public static class TokenExpiredException extends RuntimeException {
        public TokenExpiredException(String message) {
            super(message);
        }
    }

}
