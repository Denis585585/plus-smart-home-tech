package ru.yandex.practicum.interactionapi.client;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.interactionapi.dto.ShoppingCartDto;
import ru.yandex.practicum.interactionapi.request.ChangeProductQuantityRequest;

import java.util.Map;
import java.util.UUID;


@FeignClient(name = "shopping-cart", path = "/api/v1/shopping-cart", configuration = FeignConfig.class)
public interface ShoppingCartClient {
    @GetMapping
    ShoppingCartDto findShoppingCartByUser(@RequestParam String username);

    @PutMapping
    ShoppingCartDto addProductToShoppingCart(@RequestParam String username, @RequestBody Map<UUID, Long> request);

    @DeleteMapping
    void deactivateShoppingCartByUser(@RequestParam String username);

    @PostMapping("/remove")
    ShoppingCartDto deleteProductsFromShoppingCart(@RequestParam String username, @RequestBody Map<UUID, Long> request);

    @PostMapping("/change-quantity")
    ShoppingCartDto updateProductQuantity(@RequestParam String username,
                                          @RequestBody @Valid ChangeProductQuantityRequest requestDto);
}
