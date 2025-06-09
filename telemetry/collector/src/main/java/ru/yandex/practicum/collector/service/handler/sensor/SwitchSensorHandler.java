package ru.yandex.practicum.collector.service.handler.sensor;

import ru.yandex.practicum.collector.configuration.KafkaClient;
import ru.yandex.practicum.collector.configuration.KafkaTopicsConfig;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SwitchSensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

@Component
public class SwitchSensorHandler extends BaseSensorEventHandler<SwitchSensorAvro> {

    public SwitchSensorHandler(KafkaClient kafkaClient, KafkaTopicsConfig kafkaTopicsConfig) {
        super(kafkaClient, kafkaTopicsConfig);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR_EVENT;
    }

    @Override
    protected SwitchSensorAvro mapToAvro(SensorEventProto event) {
        SwitchSensorEvent switchSensor = event.getSwitchSensorEvent();
        return SwitchSensorAvro.newBuilder()
                .setState(switchSensor.getState())
                .build();
    }
}
