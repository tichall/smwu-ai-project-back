package smwu.server.global.cosmosDb;

import com.azure.cosmos.*;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.util.CosmosPagedIterable;
import com.azure.spring.data.cosmos.core.CosmosTemplate;
import com.azure.spring.data.cosmos.core.query.CosmosQuery;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import smwu.server.domain.entity.UserRecommendation;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class CosmosDbService {
    @Value("${spring.cloud.azure.cosmos.endpoint}")
    private String endpoint;

    @Value("${spring.cloud.azure.cosmos.key}")
    private String key;

    @Value("${spring.cloud.azure.cosmos.database}")
    private String databaseName;

    @Value("${spring.cloud.azure.cosmos.container}")
    private String containerName;

    private CosmosContainer container;

    @PostConstruct
    public void init() {
        CosmosClient client = new CosmosClientBuilder()
                .endpoint(endpoint)
                .key(key)
                .consistencyLevel(ConsistencyLevel.EVENTUAL)
                .buildClient();

        CosmosDatabase database = client.getDatabase(databaseName);
        this.container = database.getContainer(containerName);
    }

    public List<JsonNode> filterProducts(String category, int amount10k, int period) {
        String query = String.format("""
            SELECT TOP 10 * FROM c 
            WHERE c.category = "%s"
            AND (IS_NULL(c.min_amount_10k_won) OR c.min_amount_10k_won <= %d)
            AND (IS_NULL(c.max_amount_10k_won) OR c.max_amount_10k_won >= %d)
            AND c.min_period_months <= %d
            AND c.max_period_months >= %d
            ORDER BY c.interest_info.base_rate DESC
        """, category, amount10k, amount10k, period, period);

        CosmosPagedIterable<JsonNode> results = container.queryItems(
                query,
                new CosmosQueryRequestOptions(),
                JsonNode.class
        );

        return StreamSupport.stream(results.spliterator(), false)
                .collect(Collectors.toList());
    }

    public List<UserRecommendation.RecommendedItem> findProductsByNames(List<String> names) {
        List<UserRecommendation.RecommendedItem> items = new ArrayList<>();

        for (String name : names) {
            String query = String.format("""
            SELECT TOP 1 c.id FROM c
            WHERE c.product_name = "%s"
        """, name);

            CosmosPagedIterable<JsonNode> response = container.queryItems(
                    query,
                    new CosmosQueryRequestOptions(),
                    JsonNode.class
            );

            response.iterator().forEachRemaining(json -> {
                String productId = json.get("id").asText();
                System.out.println(productId);
                items.add(new UserRecommendation.RecommendedItem(productId, false)); // 기본 liked = false
            });
        }
        return items;
    }

    public JsonNode findProductById(String productId) {
        String query = String.format("SELECT * FROM c WHERE c.id = '%s'", productId);
        CosmosPagedIterable<JsonNode> results = container.queryItems(query, new CosmosQueryRequestOptions(), JsonNode.class);
        return results.iterator().hasNext() ? results.iterator().next() : null;
    }
}
