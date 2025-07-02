package ru.yandex.practicum.iteractionapi.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class DimensionDto {
    @Min(1)
    private Double width;
    @Min(1)
    private Double height;
    @Min(1)
    private Double depth;
}