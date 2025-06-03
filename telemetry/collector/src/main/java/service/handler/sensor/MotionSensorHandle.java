package service.handler.sensor;

import configuration.KafkaClient;
import configuration.KafkaTopicsConfig;
import lombok.RequiredArgsConstructor;
import model.sensor.MotionSensor;
import model.sensor.SensorEvent;
import model.sensor.SensorEventType;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Component
@RequiredArgsConstructor
public class MotionSensorHandle implements SensorEventHandler {

    private final KafkaClient kafkaClient;
    private final KafkaTopicsConfig kafkaTopicsConfig;

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.MOTION_SENSOR;
    }

    @Override
    public void handle(SensorEvent event) {
        MotionSensor motionSensor = (MotionSensor) event;
        MotionSensorAvro payload = MotionSensorAvro.newBuilder()
                .setLinkQuality(motionSensor.getLinkQuality())
                .setMotion(motionSensor.getMotion())
                .setVoltage(motionSensor.getVoltage())
                .build();
        SensorEventAvro sensorEventAvro = SensorEventAvro.newBuilder()
                .setId(motionSensor.getId())
                .setHubId(motionSensor.getHubId())
                .setTimestamp(motionSensor.getTimestamp())
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
