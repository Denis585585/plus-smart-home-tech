package ru.yandex.practicum.collector.service.handler.sensor;

import ru.yandex.practicum.collector.configuration.KafkaClient;
import ru.yandex.practicum.collector.configuration.KafkaTopicsConfig;
import ru.yandex.practicum.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.collector.model.sensor.SensorEventType;
import ru.yandex.practicum.collector.model.sensor.TemperatureSensor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

@Component
public class TemperatureSensorHandler extends BaseSensorEventHandler<TemperatureSensorAvro> {

    public TemperatureSensorHandler(KafkaClient kafkaClient, KafkaTopicsConfig kafkaTopicsConfig) {
        super(kafkaClient, kafkaTopicsConfig);
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.TEMPERATURE_SENSOR;
    }

    @Override
    protected TemperatureSensorAvro mapToAvro(SensorEvent event) {
        TemperatureSensor temperatureSensor = (TemperatureSensor) event;
        return TemperatureSensorAvro.newBuilder()
                .setId(temperatureSensor.getId())
                .setHubId(temperatureSensor.getHubId())
                .setTimestamp(temperatureSensor.getTimestamp())
                .setTemperatureC(temperatureSensor.getTemperatureC())
                .setTemperatureF(temperatureSensor.getTemperatureF())
                .build();
    }
}

