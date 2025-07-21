package ru.yandex.practicum.shoppingstore.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.interactionapi.dto.PageableDto;
import ru.yandex.practicum.interactionapi.dto.ProductDto;
import ru.yandex.practicum.interactionapi.enums.ProductCategory;
import ru.yandex.practicum.interactionapi.enums.QuantityState;
import ru.yandex.practicum.shoppingstore.service.ShoppingStoreService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
public class ShoppingStoreController {
    private final ShoppingStoreService shoppingStoreService;

    @GetMapping("/{productId}")
    public ProductDto findProductById(@PathVariable @NotNull UUID productId) {
        log.info("Получение сведений по товару из БД: {}", productId);
        return shoppingStoreService.findProductById(productId);
    }

    @GetMapping
    public Page<ProductDto> getProductsByCategory(@RequestParam @NotNull ProductCategory category, @Valid PageableDto pageable) {
        log.info("Получение списка товаров по типу в пагинированном виде.");
        return shoppingStoreService.findProductsByCategory(category, pageable);
    }

    @PutMapping
    public ProductDto createProduct(@RequestBody @Valid ProductDto productDto) {
        log.info("Создание нового товара в ассортименте {}", productDto);
        return shoppingStoreService.createProduct(productDto);
    }

    @PostMapping
    public ProductDto updateProduct(@RequestBody @Valid ProductDto productDto) {
        log.info("Обновление товара в ассортименте {}", productDto);
        return shoppingStoreService.updateProduct(productDto);
    }

    @PostMapping("/removeProductFromStore")
    public void deleteProduct(@RequestBody @NotNull UUID productId) {
        log.info("Удаление товара из ассортимента магазина. Функция для менеджерского состава. {}", productId);
        shoppingStoreService.deleteProduct(productId);
    }

    @PostMapping("/quantityState")
    public ProductDto setProductQuantityState(@RequestParam UUID productId, @RequestParam QuantityState quantityState) {
        log.info("quantityState request: productId {}, quantityState {}", productId, quantityState);
        return shoppingStoreService.setProductQuantityState(productId, quantityState);
    }
}