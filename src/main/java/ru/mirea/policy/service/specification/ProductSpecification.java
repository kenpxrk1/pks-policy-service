package ru.mirea.policy.service.specification;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;
import ru.mirea.policy.service.entity.InsuranceProduct;
import ru.mirea.policy.service.entity.constant.ProductStatus;
import ru.mirea.policy.service.specification.constant.PriceFilterType;

import java.math.BigDecimal;
import java.util.List;

public class ProductSpecification {

    public static Specification<InsuranceProduct> hasCategory(String category) {
        return (root, query, cb) -> category == null ? null :
                cb.equal(root.get("category"), category);
    }

    public static Specification<InsuranceProduct> hasStatusIn(List<ProductStatus> statuses) {
        return (root, query, cb) -> (statuses == null || statuses.isEmpty()) ? null :
                root.get("status").in(statuses);
    }

    public static Specification<InsuranceProduct> hasBasePrice(BigDecimal price, PriceFilterType filterType) {
        if (price == null || filterType == null) return null;

        return (root, query, cb) -> switch (filterType) {
            case GTE -> cb.greaterThanOrEqualTo(root.get("basePrice"), price);
            case LTE -> cb.lessThanOrEqualTo(root.get("basePrice"), price);
        };
    }
}

