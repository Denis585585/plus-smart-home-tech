package ru.yandex.practicum.interactionapi.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageableDto {
    @NotNull(message = "Page cannot be null")
    @Min(value = 0, message = "Page number must be positive")
    private Integer page;

    @NotNull(message = "Size cannot be null")
    @Min(value = 1, message = "Page size must be at least 1")
    @Max(value = 100, message = "Page size must be less than 100")
    private Integer size;
    private List<String> sort;
}