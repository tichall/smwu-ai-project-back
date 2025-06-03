package smwu.server.global.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static smwu.server.global.jwt.JwtProvider.BEARER_PREFIX;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public void saveRefreshTokenInfo(String email, String token) {
        String parsedRefreshToken = token.substring(BEARER_PREFIX.length());
        refreshTokenRepository.deleteByEmail(email);

        RefreshToken refreshToken = RefreshToken.builder()
                .email(email)
                .token(parsedRefreshToken)
                .createdAt(Instant.now())
                .build();

        refreshTokenRepository.save(refreshToken);
    }

    /**
     * 리프레시 토큰 존재 여부 확인
     *
     * @param email 유저의 이메일
     * @return 리프레시 토큰 존재 여부
     */
    public boolean isRefreshTokenPresent(String email) {
        return refreshTokenRepository.existsByEmail(email);
    }
}
