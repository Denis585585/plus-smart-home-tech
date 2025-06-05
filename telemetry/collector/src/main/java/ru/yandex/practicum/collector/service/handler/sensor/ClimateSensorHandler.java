package ru.yandex.practicum.collector.service.handler.sensor;

import ru.yandex.practicum.collector.configuration.KafkaClient;
import ru.yandex.practicum.collector.configuration.KafkaTopicsConfig;
import ru.yandex.practicum.collector.model.sensor.ClimateSensor;
import ru.yandex.practicum.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.collector.model.sensor.SensorEventType;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;

@Component
public class ClimateSensorHandler extends BaseSensorEventHandler<ClimateSensorAvro> {

    public ClimateSensorHandler(KafkaClient kafkaClient, KafkaTopicsConfig kafkaTopicsConfig) {
        super(kafkaClient, kafkaTopicsConfig);
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.CLIMATE_SENSOR;
    }

    @Override
    protected ClimateSensorAvro mapToAvro(SensorEvent event) {
        ClimateSensor climateSensor = (ClimateSensor) event;
        return ClimateSensorAvro.newBuilder()
                .setTemperatureC(climateSensor.getTemperatureC())
                .setHumidity(climateSensor.getHumidity())
                .setCo2Level(climateSensor.getCo2Level())
                .build();
    }
}
