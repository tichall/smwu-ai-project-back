package smwu.server.domain.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smwu.server.domain.dto.FinancialProductResponseDto;
import smwu.server.domain.service.UserRecommendationService;
import smwu.server.global.response.BasicResponse;
import smwu.server.global.security.UserDetailsImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recommendation")
public class UserRecommendationController {
    private final UserRecommendationService recommendationService;
    @GetMapping
    public ResponseEntity<BasicResponse<List<FinancialProductResponseDto>>> getRecommendations(
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ) {
        List<FinancialProductResponseDto> responseDtoList = recommendationService.getUserRecommendedProducts(userDetails.getUser().getId());
        return ResponseEntity.ok()
                .body(BasicResponse.of("추천 상품 조회 완료", responseDtoList));
    }
}
