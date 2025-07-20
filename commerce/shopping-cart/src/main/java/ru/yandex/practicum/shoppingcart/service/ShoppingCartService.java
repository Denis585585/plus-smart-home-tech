package ru.yandex.practicum.shoppingcart.service;

import ru.yandex.practicum.interactionapi.dto.BookedProductsDto;
import ru.yandex.practicum.interactionapi.dto.ShoppingCartDto;
import ru.yandex.practicum.interactionapi.request.ChangeProductQuantityRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ShoppingCartService {
    ShoppingCartDto findShoppingCartByUser(String username);

    ShoppingCartDto addProductToShoppingCart(String username, Map<UUID, Long> request);

    void deactivateShoppingCartByUser(String username);

    ShoppingCartDto deleteProductsFromShoppingCart(String username, List<UUID> request);

    ShoppingCartDto updateProductQuantity(String username, ChangeProductQuantityRequest requestDto);

    BookedProductsDto bookingCartProducts(String username);
}