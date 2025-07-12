package ru.yandex.practicum.interactionapi.client;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.interactionapi.dto.AddressDto;
import ru.yandex.practicum.interactionapi.dto.BookedProductsDto;
import ru.yandex.practicum.interactionapi.dto.ShoppingCartDto;
import ru.yandex.practicum.interactionapi.request.AddProductToWarehouseRequest;
import ru.yandex.practicum.interactionapi.request.NewProductInWarehouseRequest;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse", configuration = FeignConfig.class)
public interface WarehouseClient {
    @PutMapping
    void addNewProductToWarehouse(@RequestBody @Valid NewProductInWarehouseRequest requestDto);

    @PostMapping("/add")
    void addProductToWarehouse(@RequestBody @Valid AddProductToWarehouseRequest requestDto);

    @PostMapping("/check")
    BookedProductsDto checkProductQuantityForCart(@RequestBody @Valid ShoppingCartDto shoppingCartDto);

    @GetMapping("/address")
    AddressDto fetchWarehouseAddress();

    @PostMapping("/booking")
    BookedProductsDto bookingCartProducts(@RequestBody @Valid ShoppingCartDto shoppingCartDto);
}