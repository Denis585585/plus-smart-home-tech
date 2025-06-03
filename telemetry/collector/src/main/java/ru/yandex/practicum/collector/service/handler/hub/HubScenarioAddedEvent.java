package ru.yandex.practicum.collector.service.handler.hub;

import ru.yandex.practicum.collector.configuration.KafkaClient;
import ru.yandex.practicum.collector.configuration.KafkaTopicsConfig;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.collector.model.hub.HubEvent;
import ru.yandex.practicum.collector.model.hub.HubEventType;
import ru.yandex.practicum.collector.model.hub.ScenarioAddedEvent;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HubScenarioAddedEvent implements HubEventHandler {

    private final KafkaClient kafkaClient;
    private final KafkaTopicsConfig kafkaTopicsConfig;

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_ADDED;
    }

    @Override
    public void handle(HubEvent event) {
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


        ScenarioAddedEventAvro payload = ScenarioAddedEventAvro.newBuilder()
                .setName(scenarioAddedEvent.getName())
                .setConditions(conditionAvros)
                .setActions(actionAvros)
                .build();
        HubEventAvro hubEventAvro = HubEventAvro.newBuilder()
                .setHubId(scenarioAddedEvent.getHubId())
                .setPayload(payload)
                .setTimestamp(scenarioAddedEvent.getTimestamp())
                .build();
        kafkaClient.getProducer().send(new ProducerRecord<>(
                kafkaTopicsConfig.getHubs(),
                null,
                event.getTimestamp().toEpochMilli(),
                event.getHubId(),
                hubEventAvro));
    }
}
