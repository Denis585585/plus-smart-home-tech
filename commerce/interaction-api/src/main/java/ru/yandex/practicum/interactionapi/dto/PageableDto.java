package ru.yandex.practicum.interactionapi.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageableDto {
    @Min(value = 0, message = "Page number must be positive")
    private Integer page = 0;
    @Min(value = 1, message = "Page size must be at least 1")
    private Integer size = 20;
    private List<String> sort;
}