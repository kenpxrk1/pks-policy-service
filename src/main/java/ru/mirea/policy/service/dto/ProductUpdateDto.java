package ru.mirea.policy.service.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateDto {
        @NotBlank(message = "Name must not be blank")
        @Size(max = 100, message = "Name must not exceed 100 characters")
        private String name;

        @Size(max = 1000, message = "Description must not exceed 1000 characters")
        private String description;

        @NotNull(message = "Base price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Base price must be positive")
        @Digits(integer = 12, fraction = 2, message = "Base price format is invalid")
        private BigDecimal basePrice;

        @NotBlank(message = "Category must not be blank")
        @Size(max = 50, message = "Category must not exceed 50 characters")
        private String category;

        @Min(value = 1, message = "Minimum duration must be at least 1 day")
        private int minDurationDays;

        @Min(value = 1, message = "Maximum duration must be at least 1 day")
        private int maxDurationDays;

        @AssertTrue(message = "maxDurationDays must be greater than or equal to minDurationDays")
        public boolean isValidDurationRange() {
            return maxDurationDays >= minDurationDays;
        }
}
