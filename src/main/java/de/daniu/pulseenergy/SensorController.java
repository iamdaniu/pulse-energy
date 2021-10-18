package de.daniu.pulseenergy;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
@RestController("/")
public class SensorController {
    private final SensorService sensorService;

    @GetMapping(path = "sensors")
    public Collection<PulseSensor> allSensors() {
        return sensorService.getAllSensors();
    }

    @GetMapping(path = "sensors/{sensorId}")
    public Optional<PulseSensor> allSensors(@PathVariable String sensorId) {
        return sensorService.getAllSensors().stream()
                .filter(s -> sensorId.equals(s.getName()))
                .findAny();
    }

    @PutMapping(path = "sensor/{sensorId}/impulse")
    public void impulseReceived(@PathVariable String sensorId) {
        sensorService.countImpulse(sensorId);
    }

    @PutMapping(path = "sensor/{sensorId}/counter/{counterId}")
    public void setSensorCounter(@PathVariable String sensorId,
                                 @PathVariable String counterId,
                                 @RequestBody double counter) {
        sensorService.setSensorCounter(sensorId, counterId, counter);
    }
}
