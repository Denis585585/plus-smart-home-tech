package ru.yandex.practicum.interactionapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDto {
    private String status;
    private String reason;
    private String message;
    private String timestamp;
}