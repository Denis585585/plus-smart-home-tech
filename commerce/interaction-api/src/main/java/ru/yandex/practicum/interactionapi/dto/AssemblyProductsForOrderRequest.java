package ru.yandex.practicum.interactionapi.dto;

import jakarta.validation.constraints.NotNull;
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
public class AssemblyProductsForOrderRequest {
    @NotNull
    private Map<UUID, Long> products;
    @NotNull
    private UUID orderId;
}