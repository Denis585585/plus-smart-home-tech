package ru.yandex.practicum.collector.service.handler.hub;

import ru.yandex.practicum.collector.configuration.KafkaClient;
import ru.yandex.practicum.collector.configuration.KafkaTopicsConfig;
import ru.yandex.practicum.collector.model.hub.HubEvent;
import ru.yandex.practicum.collector.model.hub.HubEventType;
import ru.yandex.practicum.collector.model.hub.ScenarioAddedEvent;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.List;

@Component
public class HubScenarioAddedEventHandler extends BaseHubEventHandler<ScenarioAddedEventAvro> {

    public HubScenarioAddedEventHandler(KafkaClient kafkaClient, KafkaTopicsConfig kafkaTopicsConfig) {
        super(kafkaClient, kafkaTopicsConfig);
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_ADDED;
    }

    @Override
    public ScenarioAddedEventAvro mapToAvro(HubEvent event) {
        ScenarioAddedEvent scenarioAddedEvent = (ScenarioAddedEvent) event;
        List<DeviceActionAvro> actionAvros = scenarioAddedEvent.getActions().stream()
                .map(deviceAction -> DeviceActionAvro.newBuilder()
                        .setSensorId(deviceAction.getSensorId())
                        .setType(ActionTypeAvro.valueOf(deviceAction.getType().name()))
                        .setValue(deviceAction.getValue())
                        .build())
                .toList();
        List<ScenarioConditionAvro> conditionAvros = scenarioAddedEvent.getConditions().stream()
                .map(scenarioCondition -> ScenarioConditionAvro.newBuilder()
                        .setSensorId(scenarioCondition.getSensorId())
                        .setType(ConditionTypeAvro.valueOf(scenarioCondition.getType().name()))
                        .setOperation(ConditionOperationAvro.valueOf(scenarioCondition.getOperation().name()))
                        .setValue(scenarioCondition.getValue())
                        .build())
                .toList();
        return ScenarioAddedEventAvro.newBuilder()
                .setName(scenarioAddedEvent.getName())
                .setConditions(conditionAvros)
                .setActions(actionAvros)
                .build();
    }
}
