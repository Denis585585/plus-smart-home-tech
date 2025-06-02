package service.hub;

import model.hub.HubEvent;
import model.hub.HubEventType;
import org.springframework.stereotype.Service;
import service.handler.hub.HubEventHandler;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class HubEventServiceImpl implements HubEventService {

    private final Map<HubEventType, HubEventHandler> hubEventHandlers;

    public HubEventServiceImpl(List<HubEventHandler> hubEventHandlers) {
        this.hubEventHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(HubEventHandler::getMessageType, Function.identity()));
    }

    @Override
    public void handleEvent(HubEvent request) {
        if (hubEventHandlers.containsKey(request.getType())) {
            hubEventHandlers.get(request.getType()).handle(request);
        } else {
            throw new IllegalArgumentException(String.format("Handler for event with type %s not found", request.getType()));
        }
    }
}
