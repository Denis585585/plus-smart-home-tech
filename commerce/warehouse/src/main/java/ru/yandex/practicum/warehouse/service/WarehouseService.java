package ru.yandex.practicum.warehouse.service;

import ru.yandex.practicum.interactionapi.dto.AddressDto;
import ru.yandex.practicum.interactionapi.dto.BookedProductsDto;
import ru.yandex.practicum.interactionapi.dto.ShoppingCartDto;
import ru.yandex.practicum.interactionapi.request.AddProductToWarehouseRequest;
import ru.yandex.practicum.interactionapi.request.NewProductInWarehouseRequest;

public interface WarehouseService {
    void addNewProductToWarehouse(NewProductInWarehouseRequest newProductInWarehouseRequest);

    void addProductToWarehouse(AddProductToWarehouseRequest requestDto);

    BookedProductsDto checkProductQuantityForCart(ShoppingCartDto shoppingCartDto);

    AddressDto fetchWarehouseAddress();
}