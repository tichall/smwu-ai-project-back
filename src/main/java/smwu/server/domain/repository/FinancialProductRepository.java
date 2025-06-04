package smwu.server.domain.repository;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import org.springframework.stereotype.Repository;
import smwu.server.domain.entity.FinancialProduct;

import java.util.List;

@Repository
public interface FinancialProductRepository extends CosmosRepository<FinancialProduct, String> {
    List<FinancialProduct> findByIdIn(List<String> productIds);
}
