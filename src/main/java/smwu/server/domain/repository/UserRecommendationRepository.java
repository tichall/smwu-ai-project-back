package smwu.server.domain.repository;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import org.springframework.stereotype.Repository;
import smwu.server.domain.entity.UserRecommendation;

import java.util.List;

@Repository
public interface UserRecommendationRepository extends CosmosRepository<UserRecommendation, String> {
    List<UserRecommendation> findByUserIdOrderByRecommendedAtDesc(String userId);
}
