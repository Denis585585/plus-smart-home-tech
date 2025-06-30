package ru.yandex.practicum.iteractionapi.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AddProductToWarehouseRequest {
    @NotNull
    private UUID productId;
    @NotNull
    private Long quantity;
}