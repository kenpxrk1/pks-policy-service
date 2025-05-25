package ru.mirea.policy.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.mirea.auth.lib.dto.ErrorResponseDto;
import ru.mirea.policy.service.dto.ProductCreateDto;
import ru.mirea.policy.service.dto.ProductResponseDto;
import ru.mirea.policy.service.dto.ProductUpdateDto;
import ru.mirea.policy.service.entity.constant.ProductStatus;
import ru.mirea.policy.service.service.ProductService;
import ru.mirea.policy.service.specification.constant.PriceFilterType;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/api/insurance/products")
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "Find insurance product by ID", description = "Returns full product info by its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found",
                    content = @Content(schema = @Schema(implementation = ProductResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> findById(
            @Parameter(description = "UUID of the product to retrieve", required = true)
            @PathVariable UUID productId) {
        return ResponseEntity.ok(productService.findById(productId));
    }

    @Operation(summary = "Получить список продуктов с фильтрацией и пагинацией")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список продуктов успешно получен"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса")
    })
    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> findAll(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal basePrice,
            @RequestParam(required = false, defaultValue = "GT") PriceFilterType priceFilterType,
            @RequestParam(required = false) List<ProductStatus> statuses,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size
    ) {
        Page<ProductResponseDto> result = productService.findAll(
                category,
                basePrice,
                priceFilterType,
                statuses,
                page,
                size
        );
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Create a new insurance product", description = "Creates a new insurance product with the specified attributes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid ProductCreateDto dto) {
        UUID id = productService.create(dto);
        return ResponseEntity.created(URI.create("v1/api/insurance/products/" + id)).build();
    }

    @Operation(
            summary = "Удалить продукт",
            description = "Полностью удаляет страховой продукт из базы данных по его UUID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Продукт успешно удалён"),
                    @ApiResponse(responseCode = "404", description = "Продукт не найден",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
            }
    )
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "UUID продукта", required = true)
            @PathVariable UUID productId
    ) {
        productService.delete(productId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Деактивировать продукт",
            description = "Переводит продукт в статус INACTIVE, если он был активен.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Продукт успешно деактивирован",
                            content = @Content(schema = @Schema(implementation = UUID.class))),
                    @ApiResponse(responseCode = "409", description = "Продукт уже деактивирован",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Продукт не найден",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
            }
    )
    @PutMapping("{productId}/deactivate")
    public ResponseEntity<Void> deactivateProduct(@PathVariable UUID productId) {
        UUID id = productService.deactivateProduct(productId);
        return ResponseEntity.created(URI.create("v1/api/insurance/products/" + id)).build();
    }

    @Operation(
            summary = "Активировать продукт",
            description = "Переводит продукт в статус ACTIVE, если он был неактивен.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Продукт успешно активирован"),
                    @ApiResponse(responseCode = "409", description = "Продукт уже активен"),
                    @ApiResponse(responseCode = "404", description = "Продукт не найден"),
            }
    )
    @PutMapping("{productId}/activate")
    public ResponseEntity<Void> activateProduct(@PathVariable UUID productId) {
        UUID id = productService.activateProduct(productId);
        return ResponseEntity.created(URI.create("v1/api/insurance/products/" + id)).build();
    }

    @PutMapping("{productId}")
    public ResponseEntity<Void> updateProduct(@PathVariable UUID productId, @RequestBody ProductUpdateDto dto) {
        UUID id = productService.update(productId, dto);
        return ResponseEntity.created(URI.create("v1/api/insurance/products/" + id)).build();
    }
}
