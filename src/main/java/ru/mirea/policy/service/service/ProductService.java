package ru.mirea.policy.service.service;

import org.springframework.data.domain.Page;
import ru.mirea.policy.service.dto.ProductCreateDto;
import ru.mirea.policy.service.dto.ProductResponseDto;
import ru.mirea.policy.service.dto.ProductUpdateDto;
import ru.mirea.policy.service.entity.constant.ProductStatus;
import ru.mirea.policy.service.specification.constant.PriceFilterType;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ProductService {
    ProductResponseDto findById(UUID productId);
    Page<ProductResponseDto> findAll(String category,
                                     BigDecimal basePrice,
                                     PriceFilterType priceFilterType,
                                     List<ProductStatus> statuses,
                                     int page, int size);
    UUID create(ProductCreateDto productCreateDto);
    UUID activateProduct(UUID productId);
    UUID deactivateProduct(UUID productId);
    UUID update(UUID productId, ProductUpdateDto dto);
    void delete(UUID productId);
}
