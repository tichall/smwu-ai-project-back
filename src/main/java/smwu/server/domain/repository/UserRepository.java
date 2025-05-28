package smwu.server.domain.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import smwu.server.domain.entity.User;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
}
