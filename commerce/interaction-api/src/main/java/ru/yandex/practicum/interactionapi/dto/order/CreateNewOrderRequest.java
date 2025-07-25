package ru.yandex.practicum.interactionapi.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.interactionapi.dto.AddressDto;
import ru.yandex.practicum.interactionapi.dto.ShoppingCartDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateNewOrderRequest {
    private ShoppingCartDto shoppingCart;
    private AddressDto deliveryAddress;
}