package smwu.server.global.jwt;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {
    boolean existsByEmail(String email);

    void deleteByEmail(String email);
}
