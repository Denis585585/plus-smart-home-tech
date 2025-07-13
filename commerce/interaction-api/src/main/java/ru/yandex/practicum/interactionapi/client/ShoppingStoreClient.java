package ru.yandex.practicum.interactionapi.client;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import ru.yandex.practicum.interactionapi.dto.PageableDto;
import ru.yandex.practicum.interactionapi.dto.ProductDto;
import ru.yandex.practicum.interactionapi.enums.ProductCategory;
import ru.yandex.practicum.interactionapi.request.SetProductQuantityStateRequest;


import java.util.UUID;

@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store", configuration = FeignConfig.class)
public interface ShoppingStoreClient {
    @GetMapping("/{productId}")
    ProductDto findProductById(@PathVariable @NotNull UUID productId);

    @GetMapping
    Page<ProductDto> getProductsByCategory(@RequestParam ProductCategory category, @Valid PageableDto pageable);

    @PutMapping
    ProductDto createProduct(@RequestBody @Valid ProductDto productDto);

    @PostMapping
    ProductDto updateProduct(@RequestBody @Valid ProductDto productDto);

    @PostMapping("/removeProductFromStore")
    Boolean deleteProduct(@RequestBody @NotNull UUID productId);

    @PostMapping("/quantityState")
    Boolean setProductQuantityState(@Valid SetProductQuantityStateRequest setProductQuantityStateRequest);
}