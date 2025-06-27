package ru.mirea.policy.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEvent {
    private UUID productId;
    private EventType eventType;
    private LocalDateTime eventTime;
    private ProductResponseDto productSnapshot;
}
