package ru.yandex.practicum.warehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import ru.yandex.practicum.iteractionapi.client.ShoppingStoreClient;
import ru.yandex.practicum.iteractionapi.client.WarehouseClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(clients = {WarehouseClient.class})
public class WarehouseApplication {
    public static void main(String[] args) {
        SpringApplication.run(WarehouseApplication.class, args);
    }
}