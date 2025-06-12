package ru.yandex.practicum.collector.service.handler.hub;


import org.springframework.stereotype.Component;
import ru.yandex.practicum.configuration.kafka.KafkaClient;
import ru.yandex.practicum.configuration.kafka.KafkaTopicsConfig;
import ru.yandex.practicum.grpc.telemetry.event.DeviceAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;


@Component
public class HubDeviceAddedEventHandler extends BaseHubEventHandler<DeviceAddedEventAvro> {

    public HubDeviceAddedEventHandler(KafkaClient kafkaClient, KafkaTopicsConfig kafkaTopicsConfig) {
        super(kafkaClient, kafkaTopicsConfig);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_ADDED;
    }

    @Override
    protected DeviceAddedEventAvro mapToAvro(HubEventProto event) {
        DeviceAddedEventProto _event = event.getDeviceAdded();
        return DeviceAddedEventAvro.newBuilder()
                .setId(_event.getId())
                .setType(DeviceTypeAvro.valueOf(_event.getType().name()))
                .build();
    }
}
