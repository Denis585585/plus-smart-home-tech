package ru.yandex.practicum.kafka;

public class KafkaSerializationException extends RuntimeException {
    public KafkaSerializationException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
