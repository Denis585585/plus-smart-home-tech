package service.hub;

import model.hub.HubEvent;

public interface HubEventService {

    void handleEvent(HubEvent event);
}
