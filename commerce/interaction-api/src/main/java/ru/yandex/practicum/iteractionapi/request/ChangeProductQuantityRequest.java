package ru.yandex.practicum.iteractionapi.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeProductQuantityRequest {
    @NotNull
    private UUID productId;
    @NotNull
    @Min(0)
    private Long newQuantity;
}