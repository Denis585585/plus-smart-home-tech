package ru.yandex.practicum.interactionapi.client;

import feign.Feign;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.interactionapi.client.decoder.FeignErrorDecoder;

@Configuration
public class FeignConfig {

    @Bean
    public Feign.Builder feignBuilder() {
        return Feign.builder().errorDecoder(new FeignErrorDecoder());
    }

    @Bean
    public Retryer retryer() {
        return new Retryer.Default();
    }
}