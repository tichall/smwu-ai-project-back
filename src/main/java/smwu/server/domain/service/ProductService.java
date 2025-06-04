package smwu.server.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import smwu.server.domain.dto.OpenAiResponseDto;
import smwu.server.domain.dto.UserInputRequestDto;
import smwu.server.domain.entity.UserRecommendation;
import smwu.server.domain.repository.UserRecommendationRepository;
import smwu.server.global.cosmosDb.CosmosDbService;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final CosmosDbService cosmosDbService;
    private final OpenAiService openAiService;
    private final UserRecommendationRepository recommendationRepository;

    public OpenAiResponseDto recommend(String userId, UserInputRequestDto requestDto) {
        int amount10k = requestDto.getDepositAmount() / 10000;
        int period = requestDto.getPeriodMonths();
        String category = requestDto.getDepositType();

        List<JsonNode> filtered = cosmosDbService.filterProducts(category, amount10k, period);

        String contentText = buildContentText(requestDto, filtered);
        String resultText = openAiService.callOpenAi(contentText);

        // 여기에서 추천 상품 저장 로직 추가 필요
        saveRecommendation(userId, resultText);

        return OpenAiResponseDto.of(resultText);
    }

    public String buildContentText(UserInputRequestDto input, List<JsonNode> filteredProducts) {
        ObjectMapper mapper = new ObjectMapper();

        // 사용자 입력을 지정된 포맷으로 변환
        String userInputText = String.format(
                "- 예치금: %,d원\n- 예치 기간: %d개월\n- 예치 유형: %s\n- 조건: %s",
                input.getDepositAmount(),
                input.getPeriodMonths(),
                input.getDepositType(),
                input.getConditions()
        );

        String productListJson;

        try {
            // 상품 JSON 리스트를 보기 좋게 정렬
            productListJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(filteredProducts);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            productListJson = "[상품 리스트 직렬화 실패]";
        }

        // 최종 content 구성
        return """
        사용자 입력:
        %s

        후보 금융 상품 리스트:
        %s
        """.formatted(userInputText, productListJson);
    }

    public List<String> extractProductNames(String gptText) {
        List<String> productNames = new ArrayList<>();

        // 제외 섹션 이전까지만 잘라냄
        String mainSection = gptText.split("(?m)^제외된 고금리 상품")[0];

        // "상품명: ..." 패턴 추출
        Pattern pattern = Pattern.compile("- 상품명: (.+)");
        Matcher matcher = pattern.matcher(mainSection);

        while (matcher.find()) {
            productNames.add(matcher.group(1).trim());
        }
        System.out.println(productNames);
        return productNames;
    }

    public void saveRecommendation(String userId, String gptText) {
        List<String> productNames = extractProductNames(gptText);
        List<UserRecommendation.RecommendedItem> recommendedItems = cosmosDbService.findProductsByNames(productNames);
        System.out.println(userId);
        UserRecommendation recommendation = UserRecommendation.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .message(gptText)
                .recommendedAt(OffsetDateTime.now())
                .recommendedProducts(recommendedItems)
                .build();

        recommendationRepository.save(recommendation);
    }
}
