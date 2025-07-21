package ru.yandex.practicum.interactionapi.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddProductToWarehouseRequest {
    @NotNull
    private UUID productId;
    @DecimalMin(value = "1")
    private Long quantity;
}