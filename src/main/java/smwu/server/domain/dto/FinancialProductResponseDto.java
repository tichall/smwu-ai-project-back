package smwu.server.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import smwu.server.domain.entity.FinancialProduct;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialProductResponseDto {

    private String id;
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

    public FinancialProductResponseDto toDto(FinancialProduct product) {
        return FinancialProductResponseDto.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .productType(product.getProductType())
                .category(product.getCategory())
                .eligibilityTarget(product.getEligibilityTarget())

                .subscriptionConditions(
                        FinancialProductResponseDto.SubscriptionConditionsDto.builder()
                                .subscriptionAmount(product.getSubscriptionConditions().getSubscriptionAmount())
                                .contractPeriod(product.getSubscriptionConditions().getContractPeriod())
                                .build()
                )

                .interestInfo(
                        FinancialProductResponseDto.InterestInfoDto.builder()
                                .baseRate(product.getInterestInfo().getBaseRate())
                                .preferentialRate(product.getInterestInfo().getPreferentialRate())
                                .preferentialConditions(product.getInterestInfo().getPreferentialConditions())
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
