package ru.yandex.practicum.shoppingstore.service;

import org.springframework.data.domain.Page;
import ru.yandex.practicum.interactionapi.dto.PageableDto;
import ru.yandex.practicum.interactionapi.dto.ProductDto;
import ru.yandex.practicum.interactionapi.enums.ProductCategory;
import ru.yandex.practicum.interactionapi.request.SetProductQuantityStateRequest;

import java.util.UUID;

public interface ShoppingStoreService {
    ProductDto findProductById(UUID productId);

    Page<ProductDto> findProductsByCategory(ProductCategory category, PageableDto pageable);

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    boolean deleteProduct(UUID productId);

    ProductDto setProductQuantityState(SetProductQuantityStateRequest setProductQuantityStateRequest);
}