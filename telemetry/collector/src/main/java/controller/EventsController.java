package controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.hub.HubEvent;
import model.sensor.SensorEvent;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.hub.HubEventService;
import service.sensor.SensorEventService;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping(path = "/events", consumes = MediaType.APPLICATION_JSON_VALUE)
public class EventsController {

    private final HubEventService hubEventService;
    private final SensorEventService sensorEventService;

    @PostMapping("/sensors")
    public ResponseEntity<Void> collectSensorEvent(@Valid @RequestBody SensorEvent request) {
        log.info("Handling sensor event {} - Started", request);
        sensorEventService.handleEvent(request);
        log.info("Handling sensor event {} - Finished", request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/hubs")
    public ResponseEntity<Void> collectHubEvent(@Valid @RequestBody HubEvent request) {
        log.info("Handling hub event {} - Started", request);
        hubEventService.handleEvent(request);
        log.info("Handling hub event {} - Finished", request);
        return ResponseEntity.ok().build();
    }
}
