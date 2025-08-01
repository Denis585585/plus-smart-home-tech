package ru.yandex.practicum.interactionapi.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductReturnRequest {
    private UUID orderId;
    private Map<UUID, Long> products;
}