package ru.mirea.policy.service.entity.constant;

public enum ProductStatus {
    ACTIVE,
    INACTIVE;

    public static ProductStatus getCreationStatus() {
        return ProductStatus.INACTIVE;
    }
}
