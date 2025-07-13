package ru.yandex.practicum.interactionapi.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pageable {
    @Min(0)
    private Integer page;
    @Min(1)
    @Max(100)
    private Integer size;

    private List<String> sort;
}