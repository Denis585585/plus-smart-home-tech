package ru.yandex.practicum.interactionapi.exception;

public class WarehouseServerUnavailableException extends RuntimeException {
    public WarehouseServerUnavailableException(String message) {
        super(message);
    }
}