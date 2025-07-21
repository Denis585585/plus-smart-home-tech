package ru.yandex.practicum.shoppingcart.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interactionapi.client.WarehouseClient;
import ru.yandex.practicum.interactionapi.dto.BookedProductsDto;
import ru.yandex.practicum.interactionapi.dto.ShoppingCartDto;
import ru.yandex.practicum.interactionapi.request.ChangeProductQuantityRequest;
import ru.yandex.practicum.shoppingcart.exception.NoProductsInShoppingCartException;
import ru.yandex.practicum.shoppingcart.exception.NotAuthorizedUserException;
import ru.yandex.practicum.shoppingcart.mapper.ShoppingCartMapper;
import ru.yandex.practicum.shoppingcart.model.ShoppingCart;
import ru.yandex.practicum.shoppingcart.repository.ShoppingCartRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final WarehouseClient warehouseClient;

    @Override
    @Transactional(readOnly = true)
    public ShoppingCartDto findShoppingCartByUser(String username) {
        log.info("Получение корзины покупок для пользователя: {}", username);
        checkUsername(username);

        ShoppingCart shoppingCart = getCartByUsername(username);

        if (shoppingCart == null) {
            log.warn("Корзина покупок не найдена для пользователя: {}", username);
        } else {
            log.debug("Найдена корзина покупок c id = : {}, Количество товаров: {}",
                    shoppingCart.getShoppingCartId(),
                    shoppingCart.getProducts().size());
        }
        return shoppingCartMapper.toShoppingCartDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto addProductToShoppingCart(String username, Map<UUID, Long> request) {
        log.info("Добавление товаров в корзину. Пользователь: {}, Кол-во товаров: {}", username, request.size());

        checkUsername(username);
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .username(username)
                .products(request)
                .active(true)
                .build();
        ShoppingCart savedCart = shoppingCartRepository.save(shoppingCart);
        log.info("Товары успешно добавлены в корзину. id корзины: {}, Кол-во товаров: {}",
                savedCart.getShoppingCartId(),
                savedCart.getProducts().size());

        return shoppingCartMapper.toShoppingCartDto(savedCart);
    }

    @Override
    public void deactivateShoppingCartByUser(String username) {
        log.info("Деактивация корзины пользователя: {}", username);

        checkUsername(username);
        ShoppingCart shoppingCart = getCartByUsername(username);
        if (shoppingCart != null) {
            shoppingCart.setActive(false);
            shoppingCartRepository.save(shoppingCart);
            log.info("Корзина деактивирована. id: {}", shoppingCart.getShoppingCartId());
        } else {
            log.warn("Не удалось деактивировать корзину: корзина не найдена для пользователя {}", username);
        }
    }

    @Override
    public ShoppingCartDto deleteProductsFromShoppingCart(String username, List<UUID> request) {
        log.info("Удаление товаров из корзины. Пользователь: {}, Кол-во удаляемых товаров: {}", username, request.size());
        checkUsername(username);
        ShoppingCart shoppingCart = getCartByUsername(username);
        if (shoppingCart == null) {
            throw new NoProductsInShoppingCartException("У пользователя " + username + " нет корзины покупок.");
        }
        request.forEach((productId) -> shoppingCart.getProducts().remove(productId));
        ShoppingCart updatedCart = shoppingCartRepository.save(shoppingCart);
        log.info("Товары успешно удалены из корзины. id корзины: {}, Осталось товаров: {}",
                updatedCart.getShoppingCartId(),
                updatedCart.getProducts().size());

        return shoppingCartMapper.toShoppingCartDto(updatedCart);
    }

    @Override
    public ShoppingCartDto updateProductQuantity(String username, ChangeProductQuantityRequest requestDto) {
        log.info("Изменение количества товара. Пользователь: {}, Товар: {}, Новое количество: {}",
                username,
                requestDto.getProductId(),
                requestDto.getNewQuantity());
        checkUsername(username);

        ShoppingCart shoppingCart = shoppingCartRepository.findByUsername(username)
                .orElseThrow(() -> new NoProductsInShoppingCartException("У пользователя " + username + " нет корзины покупок."));
        if (!shoppingCart.getProducts().containsKey(requestDto.getProductId())) {
            throw new NoProductsInShoppingCartException("Товар " + requestDto.getProductId() + " не найден в корзине пользователя " + username);
        }

        shoppingCart.getProducts().put(requestDto.getProductId(), requestDto.getNewQuantity());

        ShoppingCart savedCart = shoppingCartRepository.save(shoppingCart);
        log.info("Количество товара успешно изменено. id корзины: {}", savedCart.getShoppingCartId());

        return shoppingCartMapper.toShoppingCartDto(savedCart);
    }

    @Override
    public BookedProductsDto bookingCartProducts(String username) {
        log.info("Начало бронирования товаров для пользователя: {}", username);
        checkUsername(username);
        ShoppingCart shoppingCart = getCartByUsername(username);
        if (shoppingCart == null || shoppingCart.getProducts().isEmpty()) {
            String errorMessage = "Невозможно забронировать товары: корзина пуста или не существует";
            log.error(errorMessage);
            throw new NoProductsInShoppingCartException(errorMessage);
        }

        log.debug("Отправка запроса на бронирование. Кол-во товаров: {}", shoppingCart.getProducts().size());
        BookedProductsDto result = warehouseClient.bookingCartProducts(shoppingCartMapper.toShoppingCartDto(shoppingCart));

        log.info("Товары успешно забронированы.");
        return result;
    }

    private void checkUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new NotAuthorizedUserException("Имя пользователя должно быть заполнено.");
        }
    }

    private ShoppingCart getCartByUsername(String username) {
        return shoppingCartRepository.findByUsername(username)
                .orElseThrow(() -> new NotAuthorizedUserException("Пользователь " + username +
                        " не авторизован в системе и не имеет корзины"));
    }
}