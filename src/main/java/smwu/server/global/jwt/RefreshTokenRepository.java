package smwu.server.global.jwt;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CosmosRepository<RefreshToken, String> {
    boolean existsByEmail(String email);

    void deleteByEmail(String email);
}
