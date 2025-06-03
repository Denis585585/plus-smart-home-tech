package ru.yandex.practicum.collector.model.hub;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.collector.model.hub.util.ActionType;

@Getter
@Setter
@ToString
public class DeviceAction {
    @NotNull
    private String sensorId;
    @NotNull
    private ActionType type;
    @NotNull
    private Integer value;
}
