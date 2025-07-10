
package ru.yandex.practicum.interactionapi.circuitbreaker;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.interactionapi.client.WarehouseClient;
import ru.yandex.practicum.interactionapi.dto.AddressDto;
import ru.yandex.practicum.interactionapi.dto.BookedProductsDto;
import ru.yandex.practicum.interactionapi.dto.ShoppingCartDto;
import ru.yandex.practicum.interactionapi.exception.WarehouseServerUnavailableException;
import ru.yandex.practicum.interactionapi.request.AddProductToWarehouseRequest;
import ru.yandex.practicum.interactionapi.request.NewProductInWarehouseRequest;

@Component
public class WarehouseClientFallback implements WarehouseClient {

    @Override
    public void addNewProductToWarehouse(NewProductInWarehouseRequest requestDto) {
        throw new WarehouseServerUnavailableException("Fallback response: сервис временно недоступен.");
    }

    @Override
    public void addProductToWarehouse(AddProductToWarehouseRequest requestDto) {
        throw new WarehouseServerUnavailableException("Fallback response: сервис временно недоступен.");
    }

    @Override
    public BookedProductsDto checkProductQuantityForCart(ShoppingCartDto shoppingCartDto) {
        throw new WarehouseServerUnavailableException("Fallback response: сервис временно недоступен.");
    }

    @Override
    public AddressDto fetchWarehouseAddress() {
        throw new WarehouseServerUnavailableException("Fallback response: сервис временно недоступен.");
    }

    @Override
    public BookedProductsDto bookingCartProducts(ShoppingCartDto shoppingCartDto) {
        throw new WarehouseServerUnavailableException("Fallback response: сервис временно недоступен.");
    }
}
