package ru.yandex.practicum.aggregator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.configuration.kafka.KafkaClient;
import ru.yandex.practicum.configuration.kafka.KafkaTopicsConfig;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AggregationServiceImpl implements AggregationService {
    private final KafkaClient kafkaClient;
    private final KafkaTopicsConfig kafkaTopics;
    private Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();

    public void handleSensorEvent(SensorEventAvro event) {
        log.info("Handling event with id {}", event.getId());

        updateState(event).ifPresent(snapshot ->
        {
            log.info("Sending snapshot");
            kafkaClient.getProducer().send(new ProducerRecord<>(
                    kafkaTopics.getSnapshots(),
                    null,
                    event.getTimestamp().toEpochMilli(),
                    event.getHubId(),
                    snapshot
            ));
        });
    }

    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        SensorsSnapshotAvro snapshot = snapshots.computeIfAbsent(event.getHubId(), snap -> new SensorsSnapshotAvro());

        if (snapshot.getSensorsState() == null) {
            snapshot.setSensorsState(new HashMap<>());
        }
        SensorStateAvro oldState = snapshot.getSensorsState().get(event.getId());
        if (oldState != null) {
            if (oldState.getTimestamp().isAfter(event.getTimestamp()) || oldState.getData().equals(event.getPayload())) {
                return Optional.empty();
            }
        }
        SensorStateAvro state = SensorStateAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setData(event.getPayload())
                .build();
        snapshot.getSensorsState().put(event.getId(), state);
        snapshot.setTimestamp(event.getTimestamp());
        snapshot.setHubId(event.getHubId());
        return Optional.of(snapshot);
    }
}
