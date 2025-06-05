package ru.yandex.practicum.collector.service;

import ru.yandex.practicum.collector.model.hub.HubEvent;

public interface HubEventService {

    void handleEvent(HubEvent event);
}
