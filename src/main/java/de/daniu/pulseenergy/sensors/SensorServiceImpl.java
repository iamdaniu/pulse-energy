package de.daniu.pulseenergy.sensors;

import de.daniu.pulseenergy.PulseSensor;
import de.daniu.pulseenergy.SensorService;
import de.daniu.pulseenergy.SensorUpdateEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
class SensorServiceImpl implements SensorService {
    private final Map<String, PulseSensor> sensors;
    private final ApplicationEventPublisher applicationEventPublisher;

    public SensorServiceImpl(PulseSensorConfigurationProperties properties, ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
        sensors = new HashMap<>();
        properties.getSensors().stream()
                .map(SensorServiceImpl::createFromProperties)
                .forEach(s -> sensors.put(s.getName().toLowerCase(), s));
    }

    @Override
    public void countImpulse(String sensorId) {
        PulseSensor sensor = sensors.get(sensorId.toLowerCase());
        sensors.get(sensorId.toLowerCase()).pulse(Instant.now());
        applicationEventPublisher.publishEvent(new SensorUpdateEvent(this, sensor));
    }

    @Override
    public Collection<PulseSensor> getAllSensors() {
        return sensors.values();
    }

    @Override
    public void setSensorCounter(String sensorId, String counterId, double counter) {
        PulseSensor sensor = sensors.get(sensorId.toLowerCase());
        sensor.setCounterBase(counterId, counter);
        applicationEventPublisher.publishEvent(new SensorUpdateEvent(this, sensor));
    }

    @PostConstruct
    public void dump() {
        sensors.forEach((k, v) -> System.out.printf("%s: %s%n", k, v));
    }

    private static PulseSensor createFromProperties(SensorConfigurationProperties properties) {
        PulseSensor result = new PulseSensor(properties.getName(), properties.getPulsesPerKwh());
        if (properties.getCounters() != null) {
            List<PulseSensor.EnergyCounter> counters = properties.getCounters().stream()
                    .map(p -> new PulseSensor.EnergyCounter(p.getName(), p.getFrom(), p.getTo()))
                    .collect(Collectors.toList());
            result.setCounters(counters);
        }
        return result;
    }
}
