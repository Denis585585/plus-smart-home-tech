package ru.yandex.practicum.interactionapi.dto.delivery;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.interactionapi.dto.AddressDto;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryDto {
    private UUID deliveryId;
    private AddressDto fromAddress;
    private AddressDto toAddress;
    private UUID orderId;
    private DeliveryState deliveryState;
}