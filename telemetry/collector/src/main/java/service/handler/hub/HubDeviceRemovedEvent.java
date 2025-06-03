package service.handler.hub;

import configuration.KafkaClient;
import configuration.KafkaTopicsConfig;
import lombok.RequiredArgsConstructor;
import model.hub.DeviceRemovedEvent;
import model.hub.HubEvent;
import model.hub.HubEventType;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Component
@RequiredArgsConstructor
public class HubDeviceRemovedEvent implements HubEventHandler {

    private final KafkaClient kafkaClient;
    private final KafkaTopicsConfig kafkaTopicsConfig;

    @Override
    public HubEventType getMessageType() {
        return HubEventType.DEVICE_REMOVED;
    }

    @Override
    public void handle(HubEvent event) {
        DeviceRemovedEvent deviceRemovedEvent = (DeviceRemovedEvent) event;
        DeviceRemovedEventAvro payload = DeviceRemovedEventAvro.newBuilder()
                .setId(deviceRemovedEvent.getId())
                .build();
        HubEventAvro hubEventAvro = HubEventAvro.newBuilder()
                .setHubId(deviceRemovedEvent.getHubId())
                .setPayload(payload)
                .setTimestamp(deviceRemovedEvent.getTimestamp())
                .build();
        kafkaClient.getProducer().send(new ProducerRecord<>(
                kafkaTopicsConfig.getHubs(),
                null,
                event.getTimestamp().toEpochMilli(),
                event.getHubId(),
                hubEventAvro));
    }
}
