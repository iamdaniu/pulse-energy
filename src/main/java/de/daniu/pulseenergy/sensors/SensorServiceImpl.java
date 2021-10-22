package de.daniu.pulseenergy.sensors;

import de.daniu.pulseenergy.domain.CounterDecider;
import de.daniu.pulseenergy.domain.EnergyCounter;
import de.daniu.pulseenergy.domain.PulseSensor;
import de.daniu.pulseenergy.SensorService;
import de.daniu.pulseenergy.SensorUpdateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
class SensorServiceImpl implements SensorService {
    private final Map<String, PulseSensor> sensors;
    private final ApplicationEventPublisher applicationEventPublisher;

    public SensorServiceImpl(CounterDecider counterDecider,
                             PulseSensorConfigurationProperties properties,
                             ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
        sensors = new HashMap<>();
        properties.getSensors().stream()
                .map(p -> createFromProperties(p, counterDecider))
                .forEach(s -> sensors.put(s.getName().toLowerCase(), s));
    }

    @Override
    public void countImpulse(String sensorId) {
        log.info("received impulse for " + sensorId);
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

    private static PulseSensor createFromProperties(SensorConfigurationProperties properties,
                                                    CounterDecider decider) {
        PulseSensor result = new PulseSensor(decider, properties.getName(), properties.getPulsesPerKwh());
        if (properties.getCounters() != null) {
            List<EnergyCounter> counters = properties.getCounters().stream()
                    .map(EnergyCounterConfigurationProperties::create)
                    .collect(Collectors.toList());
            result.setCounters(counters);
        }
        return result;
    }
}
