package ru.yandex.practicum.payment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interactionapi.client.OrderClient;
import ru.yandex.practicum.interactionapi.client.ShoppingStoreClient;
import ru.yandex.practicum.interactionapi.dto.ProductDto;
import ru.yandex.practicum.interactionapi.dto.order.OrderDto;
import ru.yandex.practicum.interactionapi.dto.order.OrderState;
import ru.yandex.practicum.interactionapi.dto.payment.PaymentDto;
import ru.yandex.practicum.interactionapi.dto.payment.PaymentStatus;
import ru.yandex.practicum.payment.exception.PaymentNotFoundException;
import ru.yandex.practicum.payment.mapper.PaymentMapper;
import ru.yandex.practicum.payment.model.Payment;
import ru.yandex.practicum.payment.repository.PaymentRepository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final ShoppingStoreClient shoppingStoreClient;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final OrderClient orderClient;

    @Transactional
    @Override
    public PaymentDto createPayment(OrderDto orderDto) {
        PaymentStatus status = PaymentStatus.PENDING;
        if (orderDto.getState().equals(OrderState.PAID)) {
            status = PaymentStatus.SUCCESS;
        } else if (orderDto.getState().equals(OrderState.PAYMENT_FAILED)) {
            status = PaymentStatus.FAILED;
        }
        Payment payment = Payment.builder()
                .deliveryPrice(orderDto.getDeliveryPrice())
                .totalPrice(orderDto.getTotalPrice())
                .productPrice(orderDto.getProductPrice())
                .status(status)
                .build();
        Payment saved = paymentRepository.save(payment);
        return paymentMapper.toDto(saved);
    }

    @Transactional
    @Override
    public BigDecimal calculateTotalCost(OrderDto orderDto) {
        Payment payment = getFromRepository(orderDto.getOrderId());
        BigDecimal deliveryPrice = payment.getDeliveryPrice();
        BigDecimal productPrice = payment.getProductPrice();
        BigDecimal totalPrice = productPrice.add(deliveryPrice);
        payment.setTotalPrice(totalPrice);
        paymentRepository.save(payment);
        return totalPrice;
    }

    @Transactional
    @Override
    public void successPayment(UUID orderId) {
        Payment payment = getFromRepository(orderId);

        payment.setStatus(PaymentStatus.SUCCESS);
        paymentRepository.save(payment);
        orderClient.completedOrder(orderId);
    }


    @Transactional
    @Override
    public BigDecimal calculateProductCost(OrderDto orderDto) {
        Payment payment = getFromRepository(orderDto.getOrderId());
        Map<UUID, Long> products = orderDto.getProducts();
        BigDecimal productCost = BigDecimal.valueOf(0);
        for (Map.Entry<UUID, Long> entry : products.entrySet()) {
            ProductDto product = shoppingStoreClient.findProductById(entry.getKey());
            Double price = product.getPrice();
            productCost = productCost.add(BigDecimal.valueOf(price * entry.getValue()));
        }
        payment.setProductPrice(productCost);
        return productCost;
    }

    @Transactional
    @Override
    public void failedPayment(UUID orderId) {
        Payment payment = getFromRepository(orderId);

        payment.setStatus(PaymentStatus.FAILED);
        paymentRepository.save(payment);
        orderClient.paymentFailedOrder(orderId);
    }


    private Payment getFromRepository(UUID orderId) {
        return paymentRepository.findByOrderId(orderId).orElseThrow(() ->
                new PaymentNotFoundException("Payment with orderId" + orderId + " not found "));
    }
}