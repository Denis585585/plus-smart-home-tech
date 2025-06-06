package ru.yandex.practicum.collector.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "kafka.topics")
public class KafkaTopicsConfig {
    private String sensors;
    private String hubs;
}
