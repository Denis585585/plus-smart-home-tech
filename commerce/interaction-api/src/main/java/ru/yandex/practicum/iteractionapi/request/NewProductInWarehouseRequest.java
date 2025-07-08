package ru.yandex.practicum.iteractionapi.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.iteractionapi.dto.DimensionDto;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewProductInWarehouseRequest {
    @NotNull
    private UUID productId;
    private Boolean fragile;
    @NotNull
    private DimensionDto dimension;
    @Min(1)
    private Double weight;
}