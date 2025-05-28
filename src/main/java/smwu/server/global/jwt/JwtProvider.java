package smwu.server.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import smwu.server.domain.entity.UserRole;
import smwu.server.global.exception.CustomSecurityException;
import smwu.server.global.exception.errorCode.SecurityErrorCode;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtProvider")
@Component
@RequiredArgsConstructor
public class JwtProvider {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_HEADER = "Refreshtoken";
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String BEARER_PREFIX = "Bearer ";

    // 액세스 토큰 만료시간 (30분)
    public static final long ACCESS_TOKEN_TIME = 30 * 60 * 1000L;

    // 리프레시 토큰 만료시간 (7일)
    public static final long REFRESH_TOKEN_TIME = 7 * 24 * 60 * 60 * 1000L;

    // JWT secret key
    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] accessKeyBytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(accessKeyBytes);
    }

    /**
     * Access Token 생성
     *
     * @param userEmail 유저 이메일
     * @param userRole 유저 권한
     * @return 생성된 Access Token
     */
    public String createAccessToken(String userEmail, UserRole userRole) {
        Date date = new Date();

        return BEARER_PREFIX + Jwts.builder()
                .setSubject(userEmail)
                .claim(AUTHORIZATION_KEY, userRole)
                .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    /**
     * Refresh Token 생성
     *
     * @param userEmail 유저 이메일
     * @return 생성된 Refresh Token
     */
    public String createRefreshToken(String userEmail) {
        Date date = new Date();

        return BEARER_PREFIX + Jwts.builder()
                .setSubject(userEmail)
                .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    /**
     * 헤더에서 JWT 토큰 추출
     *
     * @param request
     * @param header
     * @return JWT 토큰값
     */
    public String getJwtFromHeader(HttpServletRequest request, String header) {
        String bearerToken = request.getHeader(header);
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    /**
     * JWT 토큰 검증
     *
     * @param request
     * @param token   토큰값
     * @return 검증 결과 boolean
     */
    public boolean validateTokenInternal(HttpServletRequest request, String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
            request.setAttribute("exception",
                    new CustomSecurityException(SecurityErrorCode.EXPIRED_TOKEN));
        }
        return false;
    }

    /**
     * Access Token 만료 여부 검사
     *
     * @param accessToken
     * @return 만료 여부 boolean
     */
    public boolean isExpiredAccessToken(String accessToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken);
        } catch (ExpiredJwtException e) {
            return true;
        }
        return false;
    }

    /**
     * 토큰에서 Claims 추출
     *
     * @param token
     * @return Claims
     */
    public Claims getUserInfoFromClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}