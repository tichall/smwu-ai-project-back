package smwu.server.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import smwu.server.domain.entity.User;
import smwu.server.domain.repository.UserRepository;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> oAuth2UserAttributes = oAuth2User.getAttributes();
        System.out.println(oAuth2UserAttributes);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo userInfo = OAuth2UserInfo.of(registrationId, oAuth2UserAttributes);
        User user = getOrSave(userInfo);
        return new UserDetailsImpl(user);
    }

    private User getOrSave(OAuth2UserInfo userInfo) {
        User user = userRepository.findByEmail(userInfo.getEmail()).orElseGet(userInfo::toEntity);
        return userRepository.save(user);
    }
}
