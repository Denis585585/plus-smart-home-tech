package ru.yandex.practicum.interactionapi.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DimensionDto {
    @Min(1)
    private Double width;
    @Min(1)
    private Double height;
    @Min(1)
    private Double depth;
}