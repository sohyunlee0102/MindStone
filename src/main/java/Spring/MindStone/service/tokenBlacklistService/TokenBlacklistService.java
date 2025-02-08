package Spring.MindStone.service.tokenBlacklistService;

import Spring.MindStone.config.jwt.JwtTokenUtil;
import Spring.MindStone.domain.tokenBlacklist.TokenBlackList;
import Spring.MindStone.repository.tokenBlacklistRepository.TokenBlacklistRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private final TokenBlacklistRepository tokenBlacklistRepository;

    public void addToBlackList(String refreshToken) {
        LocalDateTime expiredAt = JwtTokenUtil.getExpirationDate(refreshToken);
        TokenBlackList tokenBlackList = TokenBlackList.builder()
                        .token(refreshToken)
                        .expiredAt(expiredAt)
                        .build();
        tokenBlacklistRepository.save(tokenBlackList);
    }

    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklistRepository.existsByToken(token);
    }

    @Transactional
    @Scheduled(cron = "0 0 3 * * ?")
    public void deleteExpiredTokens() {
        tokenBlacklistRepository.deleteAllByExpiredAtBefore(LocalDateTime.now());
    }

}
