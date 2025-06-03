package service.handler.hub;

import configuration.KafkaClient;
import configuration.KafkaTopicsConfig;
import lombok.RequiredArgsConstructor;
import model.hub.HubEvent;
import model.hub.HubEventType;
import model.hub.ScenarioRemovedEvent;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

@Component
@RequiredArgsConstructor
public class HubScenarioRemovedEvent implements HubEventHandler {

    private final KafkaClient kafkaClient;
    private final KafkaTopicsConfig kafkaTopicsConfig;

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_REMOVED;
    }

    @Override
    public void handle(HubEvent event) {
        ScenarioRemovedEvent scenarioRemovedEvent = (ScenarioRemovedEvent) event;
        ScenarioRemovedEventAvro payload = ScenarioRemovedEventAvro.newBuilder()
                .setName(scenarioRemovedEvent.getName())
                .build();
        HubEventAvro hubEventAvro = HubEventAvro.newBuilder()
                .setHubId(scenarioRemovedEvent.getHubId())
                .setPayload(payload)
                .setTimestamp(scenarioRemovedEvent.getTimestamp())
                .build();
        kafkaClient.getProducer().send(new ProducerRecord<>(
                kafkaTopicsConfig.getHubs(),
                null,
                event.getTimestamp().toEpochMilli(),
                event.getHubId(),
                hubEventAvro));
    }
}
