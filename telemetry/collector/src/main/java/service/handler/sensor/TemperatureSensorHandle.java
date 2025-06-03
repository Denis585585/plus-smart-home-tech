package service.handler.sensor;

import configuration.KafkaClient;
import configuration.KafkaTopicsConfig;
import lombok.RequiredArgsConstructor;
import model.sensor.SensorEvent;
import model.sensor.SensorEventType;
import model.sensor.TemperatureSensor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

@Component
@RequiredArgsConstructor
public class TemperatureSensorHandle implements SensorEventHandler {

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
}
