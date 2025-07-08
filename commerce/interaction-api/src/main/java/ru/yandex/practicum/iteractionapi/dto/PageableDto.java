package ru.yandex.practicum.iteractionapi.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageableDto {
    @Min(0)
    private Integer page;
    @Min(1)
    private Integer size;

    private List<String> sort;
}