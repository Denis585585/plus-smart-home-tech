package service.handler.sensor;

import configuration.KafkaClient;
import configuration.KafkaTopicsConfig;
import lombok.RequiredArgsConstructor;
import model.sensor.SensorEvent;
import model.sensor.SensorEventType;
import model.sensor.SwitchSensor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

@Component
@RequiredArgsConstructor
public class SwitchSensorHandle implements SensorEventHandler {

    private final KafkaClient kafkaClient;
    private final KafkaTopicsConfig kafkaTopicsConfig;

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.SWITCH_SENSOR;
    }

    @Override
    public void handle(SensorEvent event) {
        SwitchSensor switchSensor = (SwitchSensor) event;
        SwitchSensorAvro payload = SwitchSensorAvro.newBuilder()
                .setState(switchSensor.getState())
                .build();
        SensorEventAvro sensorEventAvro = SensorEventAvro.newBuilder()
                .setId(switchSensor.getId())
                .setHubId(switchSensor.getHubId())
                .setTimestamp(switchSensor.getTimestamp())
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
