package smwu.server.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import smwu.server.domain.dto.FinancialProductResponseDto;
import smwu.server.domain.dto.ToggleLikeResponseDto;
import smwu.server.domain.entity.FinancialProduct;
import smwu.server.domain.entity.UserRecommendation;
import smwu.server.domain.repository.FinancialProductRepository;
import smwu.server.domain.repository.UserRecommendationRepository;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserRecommendationService {
    private final UserRecommendationRepository recommendationRepository;
    private final FinancialProductRepository financialProductRepository;

    public List<FinancialProductResponseDto> getUserRecommendedProducts(String userId) {
        // 추천 이력 모두 조회 (최신순 정렬)
//        List<UserRecommendation> recommendations = recommendationRepository.findByUserIdOrderByRecommendedAtDesc(userId);
        List<UserRecommendation> recommendations = recommendationRepository.findByUserId(userId);

        // 추천상품 ID 및 찜 여부 매핑
        Map<String, Boolean> productLikeMap = recommendations.stream()
                .flatMap(rec -> rec.getRecommendedProducts().stream())
                .collect(Collectors.toMap(
                        UserRecommendation.RecommendedItem::getProductId,
                        UserRecommendation.RecommendedItem::isLiked,
                        (existing, replacement) -> existing // 충돌 시 기존값 유지
                ));

        Map<String, OffsetDateTime> productRecommendedAtMap = recommendations.stream()
                .flatMap(rec -> rec.getRecommendedProducts().stream()
                        .map(item -> Map.entry(item.getProductId(), rec.getRecommendedAt()))
                )
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (existing, replacement) -> existing // 중복 시 기존 유지
                ));


        // 해당 ID로 상품 조회
        List<FinancialProduct> products = financialProductRepository.findByIdIn(productLikeMap.keySet().stream().toList());

        return products.stream()
                .map(product -> FinancialProductResponseDto.of(
                        product,
                        productLikeMap.getOrDefault(product.getId(), false),
                        productRecommendedAtMap.get(product.getId())
                ))
                .sorted(Comparator
                        .comparing(FinancialProductResponseDto::isLiked) // liked = true 먼저
                        .thenComparing(FinancialProductResponseDto::getRecommendedAt).reversed() // 최신 추천일 우선
                )
                .toList();
    }

    public ToggleLikeResponseDto toggleLike(String userId, String productId) {
        List<UserRecommendation> allRecommendations = recommendationRepository
                .findByUserIdOrderByRecommendedAtDesc(userId);

        if (allRecommendations.isEmpty()) {
            throw new IllegalArgumentException("추천 이력이 존재하지 않습니다.");
        }

        // 각 추천 이력 내의 추천 상품 탐색
        for (UserRecommendation recommendation : allRecommendations) {
            for (UserRecommendation.RecommendedItem item : recommendation.getRecommendedProducts()) {
                if (item.getProductId() != null && item.getProductId().trim().equals(productId.trim())) {
                    // 찜 상태 토글
                    boolean newLiked = !item.isLiked();
                    item.setLiked(newLiked);
                    recommendationRepository.save(recommendation);

                    return new ToggleLikeResponseDto(productId, newLiked);
                }
            }
        }

        throw new IllegalArgumentException("해당 상품은 최근 추천 목록에 존재하지 않습니다.");
    }
}
