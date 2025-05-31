package model.hub;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.hub.util.ActionType;

@Getter
@Setter
@ToString
public class DeviceAction {
    @NotNull
    private String sensorId;
    @NotNull
    private ActionType type;
    @NotNull
    private Integer value;
}
