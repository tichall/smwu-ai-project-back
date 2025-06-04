package smwu.server.global.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import smwu.server.domain.entity.User;
import smwu.server.domain.entity.UserRole;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails, OAuth2User {
    private final User user;

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserRole role = user.getUserRole();

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role.toString());

        Collection<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(simpleGrantedAuthority);

        return authorities;
    }

    @Override
    public String getName() {
        return null;
    }
}
