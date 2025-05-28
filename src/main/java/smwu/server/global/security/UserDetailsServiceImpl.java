package smwu.server.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import smwu.server.domain.entity.User;
import smwu.server.domain.repository.UserRepository;
import smwu.server.global.exception.CustomSecurityException;
import smwu.server.global.exception.errorCode.SecurityErrorCode;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =  userRepository.findByEmail(username).orElseThrow(() ->
                new CustomSecurityException(SecurityErrorCode.USER_NOT_FOUND));

        return new UserDetailsImpl(user);
    }
}
