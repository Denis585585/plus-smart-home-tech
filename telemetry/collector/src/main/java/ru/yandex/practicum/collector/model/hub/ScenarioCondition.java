package ru.yandex.practicum.collector.model.hub;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.collector.model.hub.util.ConditionOperation;
import ru.yandex.practicum.collector.model.hub.util.ConditionType;

@Getter
@Setter
@ToString
public class ScenarioCondition {
    @NotNull
    private String sensorId;

    @NotNull
    private ConditionType type;

    @NotNull
    private ConditionOperation operation;

    @NotNull
    private Integer value;
}
