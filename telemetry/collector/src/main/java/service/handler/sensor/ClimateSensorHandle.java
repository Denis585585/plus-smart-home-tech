package service.handler.sensor;

import configuration.KafkaClient;
import configuration.KafkaTopicsConfig;
import lombok.RequiredArgsConstructor;
import model.sensor.ClimateSensor;
import model.sensor.SensorEvent;
import model.sensor.SensorEventType;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Component
@RequiredArgsConstructor
public class ClimateSensorHandle implements SensorEventHandler {

    private final KafkaClient kafkaClient;
    private final KafkaTopicsConfig kafkaTopicsConfig;

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.CLIMATE_SENSOR;
    }

    @Override
    public void handle(SensorEvent event) {
        ClimateSensor climateSensor = (ClimateSensor) event;
        ClimateSensorAvro payload = ClimateSensorAvro.newBuilder()
                .setTemperatureC(climateSensor.getTemperatureC())
                .setHumidity(climateSensor.getHumidity())
                .setCo2Level(climateSensor.getCo2Level())
                .build();
        SensorEventAvro sensorEventAvro = SensorEventAvro.newBuilder()
                .setId(climateSensor.getId())
                .setHubId(climateSensor.getHubId())
                .setTimestamp(climateSensor.getTimestamp())
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
