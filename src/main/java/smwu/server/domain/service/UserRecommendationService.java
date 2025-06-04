package smwu.server.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import smwu.server.domain.dto.FinancialProductResponseDto;
import smwu.server.domain.entity.FinancialProduct;
import smwu.server.domain.entity.UserRecommendation;
import smwu.server.domain.repository.FinancialProductRepository;
import smwu.server.domain.repository.UserRecommendationRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserRecommendationService {
    private final UserRecommendationRepository recommendationRepository;
    private final FinancialProductRepository financialProductRepository;

    public List<FinancialProductResponseDto> getUserRecommendedProducts(String userId) {
        // 추천 이력 모두 조회 (최신순 정렬)
        List<UserRecommendation> recommendations = recommendationRepository.findByUserIdOrderByRecommendedAtDesc(userId);

        // 추천상품 ID 및 찜 여부 매핑
        Map<String, Boolean> productLikeMap = recommendations.stream()
                .flatMap(rec -> rec.getRecommendedProducts().stream())
                .collect(Collectors.toMap(
                        UserRecommendation.RecommendedItem::getProductId,
                        UserRecommendation.RecommendedItem::isLiked,
                        (existing, replacement) -> existing // 충돌 시 기존값 유지
                ));

        // 해당 ID로 상품 조회
        List<FinancialProduct> products = financialProductRepository.findByIdIn(productLikeMap.keySet().stream().toList());

        return products.stream()
                .map(product -> FinancialProductResponseDto.of(product, productLikeMap.getOrDefault(product.getId(), false)))
                .toList();
    }
}
