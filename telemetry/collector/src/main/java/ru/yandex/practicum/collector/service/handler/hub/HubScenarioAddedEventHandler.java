package ru.yandex.practicum.collector.service.handler.hub;


import org.springframework.stereotype.Component;
import ru.yandex.practicum.configuration.kafka.KafkaClient;
import ru.yandex.practicum.configuration.kafka.KafkaTopicsConfig;

import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioAddedEventProto;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.List;

@Component
public class HubScenarioAddedEventHandler extends BaseHubEventHandler<ScenarioAddedEventAvro> {

    public HubScenarioAddedEventHandler(KafkaClient kafkaClient, KafkaTopicsConfig kafkaTopicsConfig) {
        super(kafkaClient, kafkaTopicsConfig);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    @Override
    public ScenarioAddedEventAvro mapToAvro(HubEventProto event) {
        ScenarioAddedEventProto scenarioAddedEvent = event.getScenarioAdded();
        List<DeviceActionAvro> actionAvros = scenarioAddedEvent.getActionList().stream()
                .map(deviceAction -> DeviceActionAvro.newBuilder()
                        .setSensorId(deviceAction.getSensorId())
                        .setType(ActionTypeAvro.valueOf(deviceAction.getType().name()))
                        .setValue(deviceAction.getValue())
                        .build())
                .toList();
        List<ScenarioConditionAvro> conditionAvros = scenarioAddedEvent.getConditionList().stream()
                .map(scenarioCondition -> ScenarioConditionAvro.newBuilder()
                        .setSensorId(scenarioCondition.getSensorId())
                        .setType(ConditionTypeAvro.valueOf(scenarioCondition.getType().name()))
                        .setOperation(ConditionOperationAvro.valueOf(scenarioCondition.getOperation().name()))
                        .setValue(scenarioCondition.hasIntValue() ? scenarioCondition.getIntValue() : scenarioCondition.getBoolValue())
                        .build())
                .toList();
        return ScenarioAddedEventAvro.newBuilder()
                .setName(scenarioAddedEvent.getName())
                .setConditions(conditionAvros)
                .setActions(actionAvros)
                .build();
    }
}
