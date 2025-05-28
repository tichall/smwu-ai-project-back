package smwu.server.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import smwu.server.domain.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
}
