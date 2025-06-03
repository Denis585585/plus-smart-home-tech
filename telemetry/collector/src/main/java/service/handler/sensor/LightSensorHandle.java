package service.handler.sensor;

import configuration.KafkaClient;
import configuration.KafkaTopicsConfig;
import lombok.RequiredArgsConstructor;
import model.sensor.LightSensor;
import model.sensor.SensorEvent;
import model.sensor.SensorEventType;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Component
@RequiredArgsConstructor
public class LightSensorHandle implements SensorEventHandler {

    private final KafkaClient kafkaClient;
    private final KafkaTopicsConfig kafkaTopicsConfig;

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.LIGHT_SENSOR;
    }

    @Override
    public void handle(SensorEvent event) {
        LightSensor lightSensor = (LightSensor) event;
        LightSensorAvro payload = LightSensorAvro.newBuilder()
                .setLinkQuality(lightSensor.getLinkQuality())
                .setLuminosity(lightSensor.getLuminosity())
                .build();
        SensorEventAvro sensorEventAvro = SensorEventAvro.newBuilder()
                .setId(lightSensor.getId())
                .setHubId(lightSensor.getHubId())
                .setTimestamp(lightSensor.getTimestamp())
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

