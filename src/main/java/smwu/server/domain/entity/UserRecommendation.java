package smwu.server.domain.entity;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.OffsetDateTime;
import java.util.List;

@Container(containerName = "user_recommendations")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRecommendation {
    @Id
    private String id;

    @PartitionKey
    private String userId;

    private String message;
    private OffsetDateTime recommendedAt;

    private List<RecommendedItem> recommendedProducts;

    @Getter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class RecommendedItem {
        private String productId;
        private boolean liked;
    }
}

