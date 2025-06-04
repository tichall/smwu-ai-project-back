package smwu.server.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import smwu.server.domain.entity.FinancialProduct;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialProductResponseDto {

    private String id;
    private boolean liked;
    private String recommendedAt;
    private String productName;
    private List<String> productType;
    private String category;
    private String eligibilityTarget;

    private SubscriptionConditionsDto subscriptionConditions;
    private InterestInfoDto interestInfo;

    private String interestPaymentMethod;
    private List<String> subscriptionMethods;

    private String earlyTerminationInfo;
    private Map<String, String> earlyTerminationRates;
    private Map<String, String> postMaturityRates;

    private String features;
    private Boolean depositProtection;

    private List<String> eligibilityTags;

    private Double minAmount10kWon;
    private Double maxAmount10kWon;
    private Integer minPeriodMonths;
    private Integer maxPeriodMonths;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubscriptionConditionsDto {
        private String subscriptionAmount;
        private String contractPeriod;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InterestInfoDto {
        private String baseRate;
        private String preferentialRate;
        private List<String> preferentialConditions;
    }

    public static FinancialProductResponseDto of(FinancialProduct product, boolean liked, OffsetDateTime recommendedAt) {
        FinancialProduct.SubscriptionConditions sub = product.getSubscriptionConditions();
        FinancialProduct.InterestInfo interest = product.getInterestInfo();

        String formattedDate = recommendedAt != null
                ? recommendedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                : null;

        return FinancialProductResponseDto.builder()
                .id(product.getId())
                .liked(liked)
                .recommendedAt(formattedDate)
                .productName(product.getProductName())
                .productType(product.getProductType())
                .category(product.getCategory())
                .eligibilityTarget(product.getEligibilityTarget())

                .subscriptionConditions(
                        sub == null ? null : SubscriptionConditionsDto.builder()
                                .subscriptionAmount(sub.getSubscriptionAmount())
                                .contractPeriod(sub.getContractPeriod())
                                .build()
                )

                .interestInfo(
                        interest == null ? null : InterestInfoDto.builder()
                                .baseRate(interest.getBaseRate())
                                .preferentialRate(interest.getPreferentialRate())
                                .preferentialConditions(interest.getPreferentialConditions())
                                .build()
                )

                .interestPaymentMethod(product.getInterestPaymentMethod())
                .subscriptionMethods(product.getSubscriptionMethods())

                .earlyTerminationInfo(product.getEarlyTerminationInfo())
                .earlyTerminationRates(product.getEarlyTerminationRates())
                .postMaturityRates(product.getPostMaturityRates())

                .features(product.getFeatures())
                .depositProtection(product.getDepositProtection())
                .eligibilityTags(product.getEligibilityTags())

                .minAmount10kWon(product.getMinAmount10kWon())
                .maxAmount10kWon(product.getMaxAmount10kWon())
                .minPeriodMonths(product.getMinPeriodMonths())
                .maxPeriodMonths(product.getMaxPeriodMonths())

                .build();
    }
}
