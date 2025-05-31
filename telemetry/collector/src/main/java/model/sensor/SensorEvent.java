package model.sensor;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        defaultImpl = SensorEventType.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = LightSensor.class, name = "LIGHT_SENSOR"),
        @JsonSubTypes.Type(value = ClimateSensor.class, name = "CLIMATE_SENSOR"),
        @JsonSubTypes.Type(value = MotionSensor.class, name = "MOTION_SENSOR"),
        @JsonSubTypes.Type(value = SwitchSensor.class, name = "SWITCH_SENSOR"),
        @JsonSubTypes.Type(value = TemperatureSensor.class, name = "TEMPERATURE_SENSOR")
})
@Getter
@Setter
@ToString
public abstract class SensorEvent {
    @NotBlank
    private String id;
    @NotBlank
    private String hubId;
    private Instant timestamp = Instant.now();

    @NotNull
    public abstract SensorEventType getType();
}
