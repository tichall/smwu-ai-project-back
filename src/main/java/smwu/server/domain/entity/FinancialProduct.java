package smwu.server.domain.entity;

import com.azure.spring.data.cosmos.core.mapping.Container;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Map;

@Container(containerName = "product")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialProduct {
    @Id
    private String id;

    private String productName;
    private List<String> productType;
    private String eligibilityTarget;

    private SubscriptionConditions subscriptionConditions;
    private InterestInfo interestInfo;

    private String interestPaymentMethod;
    private List<String> subscriptionMethods;

    private String earlyTerminationInfo;
    private Map<String, String> earlyTerminationRates;
    private Map<String, String> postMaturityRates;

    private String features;
    private Boolean depositProtection;

    private String category; // 예금 / 적금
    private List<String> eligibilityTags;

    private Double minAmount10kWon;
    private Double maxAmount10kWon;

    private Integer minPeriodMonths;
    private Integer maxPeriodMonths;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class SubscriptionConditions {
        private String subscriptionAmount;
        private String contractPeriod;
    }

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    public static class InterestInfo {
        private String baseRate;
        private String preferentialRate;
        private List<String> preferentialConditions;
    }
}
