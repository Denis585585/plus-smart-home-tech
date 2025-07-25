package ru.yandex.practicum.delivery.service;

import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.delivery.mapper.AddressMapper;
import ru.yandex.practicum.delivery.mapper.DeliveryMapper;
import ru.yandex.practicum.delivery.model.Delivery;
import ru.yandex.practicum.delivery.repository.DeliveryRepository;
import ru.yandex.practicum.interactionapi.client.OrderClient;
import ru.yandex.practicum.interactionapi.client.WarehouseClient;
import ru.yandex.practicum.interactionapi.dto.ShippedToDeliveryRequest;
import ru.yandex.practicum.interactionapi.dto.delivery.DeliveryDto;
import ru.yandex.practicum.interactionapi.dto.delivery.DeliveryState;
import ru.yandex.practicum.interactionapi.dto.order.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final AddressMapper addressMapper;
    private final DeliveryMapper deliveryMapper;
    private final WarehouseClient warehouseClient;
    private final OrderClient orderClient;
    private static final String ADDRESS_1 = "ADDRESS_1";
    private static final String ADDRESS_2 = "ADDRESS_2";

    @Override
    @Transactional
    public DeliveryDto planDelivery(DeliveryDto deliveryDto) {
        Delivery delivery = Delivery.builder()
                .fromAddress(addressMapper.toAddress(deliveryDto.getFromAddress()))
                .toAddress(addressMapper.toAddress(deliveryDto.getToAddress()))
                .deliveryState(deliveryDto.getDeliveryState())
                .build();
        Delivery save = deliveryRepository.save(delivery);
        return deliveryMapper.toDto(save);
    }

    @Transactional
    @Override
    public void successfulDelivery(UUID orderId) {
        Delivery delivery = getDeliveryFromRepository(orderId);
        delivery.setDeliveryState(DeliveryState.DELIVERED);
        try {
            orderClient.deliveryOrder(orderId);
        } catch (FeignException e) {
            log.warn("Failed to change status order: {}", orderId, e);
            throw e;
        }
    }

    @Override
    public void pickedToDelivery(UUID orderId) {
        Delivery delivery = getDeliveryFromRepository(orderId);
        ShippedToDeliveryRequest deliveryRequest = new ShippedToDeliveryRequest(orderId, delivery.getDeliveryId());
        try {
            warehouseClient.shippedToDelivery(deliveryRequest);
        } catch (FeignException e) {
            log.warn("Failed to change status order: {}", orderId, e);
            throw e;
        }
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        ShippedToDeliveryRequest request = ShippedToDeliveryRequest.builder()
                .orderId(delivery.getOrderId())
                .deliveryId(delivery.getDeliveryId())
                .build();
        warehouseClient.shippedToDelivery(request);
    }

    @Transactional
    @Override
    public void failedDelivery(UUID orderId) {
        Delivery delivery = getDeliveryFromRepository(orderId);
        try {
            orderClient.deliveryFailedOrder(orderId);
        } catch (FeignException e) {
            log.warn("Failed to change status order: {}", orderId, e);
            throw e;
        }
        delivery.setDeliveryState(DeliveryState.FAILED);
    }

    @Override
    public BigDecimal calculateFullDeliveryCost(OrderDto orderDto) {
        Delivery delivery = getDeliveryFromRepository(orderDto.getDeliveryId());
        BigDecimal deliveryCost = BigDecimal.valueOf(5);

        if (delivery.getFromAddress().getCity().equals(ADDRESS_1)) {
            deliveryCost = deliveryCost;
        } else {
            deliveryCost = deliveryCost.multiply(BigDecimal.valueOf(2));
        }

        if (orderDto.getFragile()) {
            deliveryCost = deliveryCost.multiply(BigDecimal.valueOf(1.2));
        }
        deliveryCost = deliveryCost.add(BigDecimal.valueOf(orderDto.getDeliveryWeight() * 0.3));
        deliveryCost = deliveryCost.add(BigDecimal.valueOf(orderDto.getDeliveryWeight() * 0.2));
        if (!delivery.getToAddress().getStreet().equals(delivery.getFromAddress().getStreet())) {
            deliveryCost = deliveryCost.multiply(BigDecimal.valueOf(1.2));
        }
        delivery.setDeliveryVolume(orderDto.getDeliveryVolume());
        delivery.setDeliveryWeight(orderDto.getDeliveryWeight());
        delivery.setFragile(orderDto.getFragile());

        return deliveryCost;
    }

    private Delivery getDeliveryFromRepository(UUID orderId) {
        return deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new EntityNotFoundException("No delivery found with id " + orderId));
    }
}