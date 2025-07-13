package ru.yandex.practicum.shoppingstore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interactionapi.dto.PageableDto;
import ru.yandex.practicum.interactionapi.dto.ProductDto;
import ru.yandex.practicum.interactionapi.enums.ProductCategory;
import ru.yandex.practicum.interactionapi.enums.ProductState;
import ru.yandex.practicum.interactionapi.request.SetProductQuantityStateRequest;
import ru.yandex.practicum.shoppingstore.exception.ProductNotFoundException;
import ru.yandex.practicum.shoppingstore.mapper.ProductMapper;
import ru.yandex.practicum.shoppingstore.model.Product;
import ru.yandex.practicum.shoppingstore.repository.ShoppingStoreRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingStoreServiceImpl implements ShoppingStoreService {
    private final ShoppingStoreRepository shoppingStoreRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductDto findProductById(UUID productId) {
        log.info("Запрос товара c id =: {}", productId);

        Product product = shoppingStoreRepository.findByProductId(productId).orElseThrow(
                () -> new ProductNotFoundException(String.format("Товар c id =: %s в БД не найден", productId))
        );

        log.debug("Товар найден. id = : {}, name: {}", productId, product.getProductName());
        return productMapper.productToProductDto(product);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProductDto> findProductsByCategory(ProductCategory category, PageableDto pageable) {

        List<Sort.Order> sortOrderList =
                pageable.getSort() != null ? pageable.getSort().stream().map(Sort.Order::asc).toList() : null;

        PageRequest pageRequest = PageRequest.of(pageable.getPage(), pageable.getSize(),
                sortOrderList == null ? Sort.unsorted() : Sort.by(sortOrderList));

        Page<Product> products = shoppingStoreRepository.findAllByProductCategory(category, pageRequest);

        return products.map(product -> new ProductDto(
                product.getProductId(),
                product.getProductName(),
                product.getDescription(),
                product.getImageSrc(),
                product.getQuantityState(),
                product.getProductState(),
                product.getRating(),
                product.getProductCategory(),
                product.getPrice()));
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        log.info("Создание нового товара. Name: {}", productDto.getProductName());

        Product newProduct = productMapper.productDtoToProduct(productDto);
        Product savedProduct = shoppingStoreRepository.save(newProduct);

        log.info("Товар успешно создан. id: {}, name: {}",
                savedProduct.getProductId(),
                savedProduct.getProductName());

        return productMapper.productToProductDto(savedProduct);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        log.info("Попытка обновить продукт с id = : {}", productDto.getProductId());
        Product oldProduct = shoppingStoreRepository.findByProductId(productDto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(
                        String.format("Товар c id =: %s в БД не найден", productDto.getProductId()))
                );
        Product newProduct = productMapper.productDtoToProduct(productDto);
        newProduct.setProductId(oldProduct.getProductId());
        ProductDto updatedProductDto = productMapper.productToProductDto(shoppingStoreRepository.save(newProduct));
        log.info("Успешно обновленный продукт: id={}, new name={}", updatedProductDto.getProductId(), updatedProductDto.getProductName());
        return productMapper.productToProductDto(shoppingStoreRepository.save(newProduct));
    }

    @Override
    public boolean deleteProduct(UUID productId) {
        log.info("Запрос на удаление товара с id = : {}", productId);

        Product product = shoppingStoreRepository.findByProductId(productId)
                .orElseThrow(() -> {
                    String errorMessage = String.format("Товар c id =: %s в БД не найден", productId);
                    log.error(errorMessage);
                    return new ProductNotFoundException(errorMessage);
                });
        product.setProductState(ProductState.DEACTIVATE);

        log.info("Товар деактивирован c id=: {}", productId);
        return true;
    }

    @Override
    public boolean setProductQuantityState(SetProductQuantityStateRequest request) {
        log.info("Изменение статуса товара c id = : {}, Новый статус: {}",
                request.getProductId(),
                request.getQuantityState());

        Product product = shoppingStoreRepository.findByProductId(request.getProductId())
                .orElseThrow(
                        () -> new ProductNotFoundException(String.format("Товар c id =: %s в БД не найден", request.getProductId()))
                );
        product.setQuantityState(request.getQuantityState());

        log.info("Статус товара обновлён: id = {}, Статус: {}",
                product.getProductId(),
                product.getQuantityState());

        return true;
    }
}