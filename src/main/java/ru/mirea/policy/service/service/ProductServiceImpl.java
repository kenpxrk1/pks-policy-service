package ru.mirea.policy.service.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.policy.service.dto.ProductCreateDto;
import ru.mirea.policy.service.dto.ProductResponseDto;
import ru.mirea.policy.service.dto.ProductUpdateDto;
import ru.mirea.policy.service.entity.InsuranceProduct;
import ru.mirea.policy.service.entity.constant.ProductStatus;
import ru.mirea.policy.service.exception.InvalidStateException;
import ru.mirea.policy.service.mapper.ProductMapper;
import ru.mirea.policy.service.repository.ProductRepository;
import ru.mirea.policy.service.specification.ProductSpecification;
import ru.mirea.policy.service.specification.constant.PriceFilterType;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Transactional(readOnly = true)
    @Override
    public ProductResponseDto findById(UUID productId) {
        log.info("Getting product with id={} from database", productId);
        InsuranceProduct product = findProductEntityByUUID(productId);
        return productMapper.toResponseDto(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> findAll(String category,
                                            BigDecimal basePrice,
                                            PriceFilterType priceFilterType,
                                            List<ProductStatus> statuses,
                                            int page, int size) {

        Specification<InsuranceProduct> spec = Specification
                .where(ProductSpecification.hasCategory(category))
                .and(ProductSpecification.hasBasePrice(basePrice, priceFilterType))
                .and(ProductSpecification.hasStatusIn(statuses));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        return productRepository.findAll(spec, pageable).map(productMapper::toResponseDto);
    }

    @Transactional
    @Override
    public UUID create(ProductCreateDto productCreateDto) {
        log.info("Creating entity product={}", productCreateDto);
        InsuranceProduct entity = productMapper.toEntity(productCreateDto);
        productRepository.save(entity);
        log.info("product with id={} successfully created", entity.getId());
        return entity.getId();
    }

    @Transactional
    @Override
    public UUID deactivateProduct(UUID productId) {
        InsuranceProduct product = findProductEntityByUUID(productId);
        switch (product.getStatus()) {
            case ACTIVE -> product.setStatus(ProductStatus.INACTIVE);
            case INACTIVE -> throw new InvalidStateException("Product already deactivated");
        }
        log.info("Product with id={} successfully deactivated: status={}", product.getId(), product.getStatus().name());

        return product.getId();
    }

    @Transactional
    @Override
    public UUID activateProduct(UUID productId) {
        log.info("Activating product with id={}", productId);
        InsuranceProduct product = findProductEntityByUUID(productId);
        switch (product.getStatus()) {
            case INACTIVE -> product.setStatus(ProductStatus.ACTIVE);
            case ACTIVE -> throw new InvalidStateException("Product already activated");
        }
        log.info("Product with id={} successfully activated: status={}", product.getId(), product.getStatus().name());
        return product.getId();
    }

    @Transactional
    @Override
    public UUID update(UUID productId, ProductUpdateDto dto) {
        log.info("Updating product with id={}", productId);
        InsuranceProduct product = findProductEntityByUUID(productId);
        productMapper.updateEntityFromDto(dto, product);
        log.info("Product with id={} successfully updated", product.getId());
        return product.getId();
    }

    @Transactional
    @Override
    public void delete(UUID productId) {
        log.info("Deleting product with id={}", productId);
        InsuranceProduct product = findProductEntityByUUID(productId);
        productRepository.delete(product);
        log.info("Product with id={} successfully deleted", product.getId());
    }

    private InsuranceProduct findProductEntityByUUID(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product with id=" + productId + " not found"));
    }
}
