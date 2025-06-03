package smwu.server.global.security.filter;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import smwu.server.domain.entity.User;
import smwu.server.global.jwt.JwtProvider;
import smwu.server.global.jwt.RefreshTokenService;
import smwu.server.global.security.UserDetailsImpl;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler  {
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        String accessToken = jwtProvider.createAccessToken(user.getEmail(), user.getUserRole());
        String refreshToken = jwtProvider.createRefreshToken(user.getEmail());
        refreshTokenService.saveRefreshTokenInfo(user.getEmail(), refreshToken);

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
//        refreshTokenCookie.setHttpOnly(true);
//        refreshTokenCookie.setSecure(true);
//        refreshTokenCookie.setPath("/");
//        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);
//
//        response.addCookie(refreshTokenCookie);

        String frontendRedirectUrl = "http://localhost:3000/social-success";
        response.sendRedirect(frontendRedirectUrl + "?accessToken=" + accessToken);
    }
}
