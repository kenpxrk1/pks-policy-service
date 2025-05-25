package ru.mirea.policy.service.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.mirea.policy.service.dto.ProductCreateDto;
import ru.mirea.policy.service.dto.ProductResponseDto;
import ru.mirea.policy.service.dto.ProductUpdateDto;
import ru.mirea.policy.service.entity.InsuranceProduct;
import ru.mirea.policy.service.entity.constant.ProductStatus;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponseDto toResponseDto(InsuranceProduct entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(getCreationStatus())")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    InsuranceProduct toEntity(ProductCreateDto dto);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ProductUpdateDto dto, @MappingTarget InsuranceProduct product);

    default ProductStatus getCreationStatus() {
        return ProductStatus.getCreationStatus();
    }
}
