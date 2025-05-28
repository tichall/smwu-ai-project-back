package smwu.server.global.security;

import lombok.Builder;
import lombok.Getter;
import smwu.server.domain.entity.User;
import smwu.server.domain.entity.UserRole;
import smwu.server.domain.entity.UserStatus;
import smwu.server.domain.enums.OAuthProvider;
import smwu.server.global.exception.CustomException;
import smwu.server.global.exception.errorCode.AuthErrorCode;

import java.util.Map;

@Getter
@Builder
public class OAuth2UserInfo {
    private String registrationId;
    private String email;
    private String name;

    public static OAuth2UserInfo of(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) {
            case "kakao" -> ofKakao(registrationId, attributes);
            case "naver" -> ofNaver(registrationId, attributes);
            default -> throw new CustomException(AuthErrorCode.INVALID_OAUTH2_PROVIDER);
        };
    }

    private static OAuth2UserInfo ofKakao(String registrationId, Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return OAuth2UserInfo.builder()
                .registrationId(registrationId)
                .email((String) account.get("email"))
                .name((String) profile.get("nickname"))
                .build();
    }

    private static OAuth2UserInfo ofNaver(String registrationId, Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("response");

        return OAuth2UserInfo.builder()
                .registrationId(registrationId)
                .email((String) account.get("email"))
                .name((String) account.get("name"))
                .build();
    }

    public User toEntity() {
        return User.builder()
                .email(email)
                .name(name)
                .oAuthProvider(OAuthProvider.fromString(registrationId))
                .userRole(UserRole.USER)
                .userStatus(UserStatus.ACTIVATE)
                .build();
    }
}
