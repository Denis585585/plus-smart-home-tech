package ru.yandex.practicum.collector.service.handler.sensor;

import ru.yandex.practicum.collector.configuration.KafkaClient;
import ru.yandex.practicum.collector.configuration.KafkaTopicsConfig;
import ru.yandex.practicum.collector.model.sensor.LightSensor;
import ru.yandex.practicum.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.collector.model.sensor.SensorEventType;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;

@Component
public class LightSensorHandler extends BaseSensorEventHandler<LightSensorAvro> {

    public LightSensorHandler(KafkaClient kafkaClient, KafkaTopicsConfig kafkaTopicsConfig) {
        super(kafkaClient, kafkaTopicsConfig);
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.LIGHT_SENSOR;
    }

    @Override
    protected LightSensorAvro mapToAvro(SensorEvent event) {
        LightSensor lightSensor = (LightSensor) event;
        return LightSensorAvro.newBuilder()
                .setLinkQuality(lightSensor.getLinkQuality())
                .setLuminosity(lightSensor.getLuminosity())
                .build();
    }
}

