package de.daniu.pulseenergy;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class SensorUpdateEvent extends ApplicationEvent {
    @Getter
    private final PulseSensor sensor;
    public SensorUpdateEvent(Object source, PulseSensor sensor) {
        super(source);
        this.sensor = sensor;
    }
}
