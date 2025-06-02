package service.handler.sensor;

import model.sensor.SensorEvent;
import model.sensor.SensorEventType;

public interface SensorEventHandler {

    SensorEventType getMessageType();

    void handle(SensorEvent event);
}
