package ru.yandex.practicum.shoppingstore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.interactionapi.dto.PageableDto;
import ru.yandex.practicum.interactionapi.dto.ProductDto;
import ru.yandex.practicum.interactionapi.enums.ProductCategory;
import ru.yandex.practicum.interactionapi.enums.ProductState;
import ru.yandex.practicum.interactionapi.enums.QuantityState;
import ru.yandex.practicum.shoppingstore.exception.ProductNotFoundException;
import ru.yandex.practicum.shoppingstore.mapper.ProductMapper;
import ru.yandex.practicum.shoppingstore.model.Product;
import ru.yandex.practicum.shoppingstore.repository.ShoppingStoreRepository;


import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShoppingStoreServiceImpl implements ShoppingStoreService {
    private final ProductMapper productMapper;
    private final ShoppingStoreRepository shoppingStoreRepository;

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
        Product product = productMapper.productDtoToProduct(productDto);
        Product savedProduct = shoppingStoreRepository.save(product);
        return productMapper.productToProductDto(savedProduct);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        getProduct(productDto.getProductId());
        Product product = productMapper.productDtoToProduct(productDto);
        return productMapper.productToProductDto(shoppingStoreRepository.save(product));
    }

    @Override
    public ProductDto findProductById(UUID productId) {
        Product product = getProduct(productId);
        return productMapper.productToProductDto(product);
    }

    @Override
    public void deleteProduct(UUID productId) {
        Product product = getProduct(productId);
        product.setProductState(ProductState.DEACTIVATE);
        shoppingStoreRepository.save(product);
    }

    @Override
    public ProductDto setProductQuantityState(UUID productId, QuantityState quantityState) {
        Product product = getProduct(productId);
        product.setQuantityState(quantityState);
        return productMapper.productToProductDto(shoppingStoreRepository.save(product));
    }

    private Product getProduct(UUID id) {
        return shoppingStoreRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product is not found by id"));
    }

}
