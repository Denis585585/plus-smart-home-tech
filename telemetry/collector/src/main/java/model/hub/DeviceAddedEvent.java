package model.hub;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.hub.util.DeviceType;

@Getter
@Setter
@ToString
public class DeviceAddedEvent extends HubEvent {
    @NotNull
    private String id;

    @NotNull
    private DeviceType deviceType;

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_ADDED;
    }
}
