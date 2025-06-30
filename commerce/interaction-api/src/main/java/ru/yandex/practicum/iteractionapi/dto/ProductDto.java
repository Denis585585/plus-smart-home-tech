package ru.yandex.practicum.iteractionapi.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.yandex.practicum.iteractionapi.enums.ProductCategory;
import ru.yandex.practicum.iteractionapi.enums.ProductState;
import ru.yandex.practicum.iteractionapi.enums.QuantityState;

import java.util.UUID;

@Data
public class ProductDto {
    private UUID productId;
    @NotBlank
    private String productName;
    @NotBlank
    private String description;
    private String imageSrc;
    @NotNull
    private QuantityState quantityState;
    @NotNull
    private ProductState productState;
    @Min(1)
    @Max(5)
    private Integer rating;
    private ProductCategory productCategory;
    @NotNull
    private Double price;
}