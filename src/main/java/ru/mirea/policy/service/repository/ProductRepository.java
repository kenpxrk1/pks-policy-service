package ru.mirea.policy.service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.mirea.policy.service.entity.InsuranceProduct;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<InsuranceProduct, UUID> {
    Page<InsuranceProduct> findAll(Specification<InsuranceProduct> spec, Pageable pageable);
}
