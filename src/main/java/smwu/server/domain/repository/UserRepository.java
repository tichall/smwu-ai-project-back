package smwu.server.domain.repository;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import org.springframework.stereotype.Repository;
import smwu.server.domain.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends CosmosRepository<User, String> {
    Optional<User> findByEmail(String email);
}
