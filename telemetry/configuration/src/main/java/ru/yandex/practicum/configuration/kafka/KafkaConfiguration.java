package ru.yandex.practicum.configuration.kafka;

import jakarta.annotation.PreDestroy;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.deserializer.HubEventDeserializer;
import ru.yandex.practicum.kafka.deserializer.SensorEventDeserializer;
import ru.yandex.practicum.kafka.deserializer.SnapshotDeserializer;
import ru.yandex.practicum.kafka.serializer.GeneralAvroSerializer;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

@Configuration
public class KafkaConfiguration {
    @Value("${kafka.consumer.client-id}")
    private String clientId;

    private AtomicBoolean shuttingDownSensorEvent = new AtomicBoolean(false);
    private AtomicBoolean shuttingDownSnapshot = new AtomicBoolean(false);
    private AtomicBoolean shuttingDownHubEvent = new AtomicBoolean(false);

    @Bean
    KafkaClient getClient() {
        return new KafkaClient() {
            private Consumer<String, SensorEventAvro> sensorEventConsumer;
            private Consumer<String, SensorsSnapshotAvro> snapshotConsumer;
            private Consumer<String, HubEventAvro> hubEventConsumer;
            private Producer<String, SpecificRecordBase> producer;

            @Override
            public Consumer<String, SensorEventAvro> getSensorEventConsumer() {
                if (sensorEventConsumer == null) {
                    initSensorEventConsumer();
                }
                return sensorEventConsumer;
            }

            @Override
            public Consumer<String, SensorsSnapshotAvro> getSnapshotConsumer() {
                if (snapshotConsumer == null) {
                    initSnapshotConsumer();
                }
                return snapshotConsumer;
            }

            @Override
            public Producer<String, SpecificRecordBase> getProducer() {
                if (producer == null) {
                    initProducer();
                }
                return producer;
            }

            @Override
            public Consumer<String, HubEventAvro> getHubEventConsumer() {
                if (hubEventConsumer == null) {
                    initHubEventConsumer();
                }
                return hubEventConsumer;
            }

            private void initProducer() {
                Properties config = new Properties();
                config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
                config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
                config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GeneralAvroSerializer.class);

                producer = new KafkaProducer<>(config);
            }

            private void initSensorEventConsumer() {
                Properties config = new Properties();
                config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
                config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
                config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SensorEventDeserializer.class);
                config.put(ConsumerConfig.CLIENT_ID_CONFIG, "sensor_%s".formatted(clientId));
                config.put(ConsumerConfig.GROUP_ID_CONFIG, "sensor-event-group");
                config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

                sensorEventConsumer = new KafkaConsumer<>(config);
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    shuttingDownSensorEvent.set(true);
                    sensorEventConsumer.wakeup();
                }));
            }

            private void initSnapshotConsumer() {
                Properties config = new Properties();
                config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
                config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
                config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SnapshotDeserializer.class);
                config.put(ConsumerConfig.CLIENT_ID_CONFIG, "snapshot_%s".formatted(clientId));
                config.put(ConsumerConfig.GROUP_ID_CONFIG, "snapshot-group");
                config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

                snapshotConsumer = new KafkaConsumer<>(config);
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    shuttingDownSnapshot.set(true);
                    snapshotConsumer.wakeup();
                }));
            }

            private void initHubEventConsumer() {
                Properties config = new Properties();
                config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
                config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
                config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, HubEventDeserializer.class);
                config.put(ConsumerConfig.CLIENT_ID_CONFIG, "hub_%s".formatted(clientId));
                config.put(ConsumerConfig.GROUP_ID_CONFIG, "snapshot-group");
                config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

                hubEventConsumer = new KafkaConsumer<>(config);
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    shuttingDownHubEvent.set(true);
                    hubEventConsumer.wakeup();
                }));
            }

            @PreDestroy
            @Override
            public void stopProducer() {
                if (producer != null) {
                    producer.flush();
                    producer.close();
                }
            }

            @PreDestroy
            @Override
            public synchronized void stopSensorEventConsumer() {
                if (sensorEventConsumer != null) {
                    if (!shuttingDownSensorEvent.get()) {
                        sensorEventConsumer.wakeup();
                    }
                    sensorEventConsumer.close();
                }
            }

            @PreDestroy
            @Override
            public synchronized void stopSnapshotConsumer() {
                if (snapshotConsumer != null) {
                    if (!shuttingDownSnapshot.get()) {
                        snapshotConsumer.wakeup();
                    }
                    snapshotConsumer.close();
                }
            }

            @PreDestroy
            @Override
            public synchronized void stopHubEventConsumer() {
                if (hubEventConsumer != null) {
                    if (!shuttingDownHubEvent.get()) {
                        hubEventConsumer.wakeup();
                    }
                    hubEventConsumer.close();
                }
            }
        };
    }
}