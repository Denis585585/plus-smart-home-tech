package ru.yandex.practicum.shoppingcart.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.interactionapi.dto.BookedProductsDto;
import ru.yandex.practicum.interactionapi.dto.ShoppingCartDto;
import ru.yandex.practicum.interactionapi.request.ChangeProductQuantityRequest;
import ru.yandex.practicum.shoppingcart.service.ShoppingCartService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/shopping-cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    public ShoppingCartDto findShoppingCartByUser(@RequestParam String username) {
        log.info("Получение актуальной корзины для авторизованного пользователя. {}", username);
        return shoppingCartService.findShoppingCartByUser(username);
    }

    @PutMapping
    public ShoppingCartDto addProductToShoppingCart(@RequestParam String username,
                                                    @RequestBody Map<UUID, Long> request) {
        log.info("Добавление товара в корзину {}", username);
        return shoppingCartService.addProductToShoppingCart(username, request);
    }

    @DeleteMapping
    public void deactivateShoppingCartByUser(@RequestParam String username) {
        log.info("Деактивация корзины товаров для пользователя {}", username);
        shoppingCartService.deactivateShoppingCartByUser(username);
    }

    @PostMapping("/remove")
    public ShoppingCartDto deleteProductsFromShoppingCart(@RequestParam String username,
                                                          @RequestBody List<UUID> request) {
        log.info("Изменение состава товаров в корзине {}", username);
        return shoppingCartService.deleteProductsFromShoppingCart(username, request);
    }

    @PostMapping("/change-quantity")
    public ShoppingCartDto updateProductQuantity(@RequestParam String username,
                                                 @RequestBody @Valid ChangeProductQuantityRequest requestDto) {
        log.info("Изменение количества товаров в корзине. {}", username);
        return shoppingCartService.updateProductQuantity(username, requestDto);
    }

    @PostMapping("/booking")
    public BookedProductsDto bookingCartProducts(@RequestParam String username) {
        log.info("Бронирование корзины покупок для пользователя {}", username);
        return shoppingCartService.bookingCartProducts(username);
    }
}