package ru.yandex.practicum.collector.service.handler.sensor;

import ru.yandex.practicum.collector.configuration.KafkaClient;
import ru.yandex.practicum.collector.configuration.KafkaTopicsConfig;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.collector.model.sensor.SensorEventType;
import ru.yandex.practicum.collector.model.sensor.TemperatureSensor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

@Component
@RequiredArgsConstructor
public class TemperatureSensorHandler implements SensorEventHandler {

    private final KafkaClient kafkaClient;
    private final KafkaTopicsConfig kafkaTopicsConfig;

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.TEMPERATURE_SENSOR;
    }

    @Override
    public void handle(SensorEvent event) {
        TemperatureSensor temperatureSensor = (TemperatureSensor) event;
        TemperatureSensorAvro payload = TemperatureSensorAvro.newBuilder()
                .setId(temperatureSensor.getId())
                .setHubId(temperatureSensor.getHubId())
                .setTimestamp(temperatureSensor.getTimestamp())
                .setTemperatureC(temperatureSensor.getTemperatureC())
                .setTemperatureF(temperatureSensor.getTemperatureF())
                .build();
        SensorEventAvro sensorEventAvro = SensorEventAvro.newBuilder()
                .setId(temperatureSensor.getId())
                .setHubId(temperatureSensor.getHubId())
                .setTimestamp(temperatureSensor.getTimestamp())
                .setPayload(payload)
                .build();
        kafkaClient.getProducer().send(new ProducerRecord<>(
                kafkaTopicsConfig.getSensors(),
                null,
                event.getTimestamp().toEpochMilli(),
                event.getHubId(),
                sensorEventAvro));
    }
}

