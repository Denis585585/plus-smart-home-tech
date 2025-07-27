package ru.yandex.practicum.interactionapi.client;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.interactionapi.circuitbreaker.WarehouseClientFallback;
import ru.yandex.practicum.interactionapi.dto.*;
import ru.yandex.practicum.interactionapi.request.AddProductToWarehouseRequest;
import ru.yandex.practicum.interactionapi.request.NewProductInWarehouseRequest;

import java.util.Map;
import java.util.UUID;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse", fallback = WarehouseClientFallback.class, configuration = FeignConfig.class)
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

    @PostMapping("/api/v1/warehouse/shipped")
    void shippedToDelivery(@RequestBody ShippedToDeliveryRequest deliveryRequest);

    @PostMapping("/api/v1/warehouse/assembly")
    BookedProductsDto createAssembly(@RequestBody AssemblyProductsForOrderRequest assembly);

    @PostMapping("/api/v1/warehouse/return")
    void addReturnProductInWarehouse(Map<UUID, Long> products);

    @GetMapping("/api/v1/warehouse/address")
    AddressDto getWarehouseAddress();
}