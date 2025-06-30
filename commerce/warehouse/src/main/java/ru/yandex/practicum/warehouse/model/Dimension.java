package ru.yandex.practicum.warehouse.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
public class Dimension {
    private Double width;
    private Double height;
    private Double depth;
}