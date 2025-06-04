package smwu.server.domain.entity;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("product_type")
    private List<String> productType;

    @JsonProperty("eligibility_target")
    private String eligibilityTarget;

    @JsonProperty("subscription_conditions")
    private SubscriptionConditions subscriptionConditions;

    @JsonProperty("interest_info")
    private InterestInfo interestInfo;

    @JsonProperty("interest_payment_method")
    private String interestPaymentMethod;

    @JsonProperty("subscription_methods")
    private List<String> subscriptionMethods;

    @JsonProperty("early_termination_info")
    private String earlyTerminationInfo;

    @JsonProperty("early_termination_rates")
    private Map<String, String> earlyTerminationRates;

    @JsonProperty("post_maturity_rates")
    private Map<String, String> postMaturityRates;

    private String features;

    @JsonProperty("deposit_protection")
    private Boolean depositProtection;

    private String category; // 예금 / 적금

    @JsonProperty("eligibility_tags")
    private List<String> eligibilityTags;

    @JsonProperty("min_amount_10k_won")
    private Double minAmount10kWon;

    @JsonProperty("max_amount_10k_won")
    private Double maxAmount10kWon;

    @JsonProperty("min_period_months")
    private Integer minPeriodMonths;

    @JsonProperty("max_period_months")
    private Integer maxPeriodMonths;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class SubscriptionConditions {
        @JsonProperty("subscription_amount")
        private String subscriptionAmount;
        @JsonProperty("contract_period")
        private String contractPeriod;
    }

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    public static class InterestInfo {
        @JsonProperty("base_rate")
        private String baseRate;
        @JsonProperty("preferential_rate")
        private String preferentialRate;
        @JsonProperty("preferential_conditions")
        private List<String> preferentialConditions;
    }
}
