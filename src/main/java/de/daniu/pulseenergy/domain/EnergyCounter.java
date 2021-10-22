package de.daniu.pulseenergy.domain;

import lombok.Getter;
import lombok.Setter;

public class EnergyCounter {
    @Getter
    @Setter
    private String name;

    @Getter
    private final CounterType type;

    @Setter
    private double reading;

    EnergyCounter(String main) {
        this(main, CounterType.always);
    }
    public EnergyCounter(String name, CounterType type) {
        this.name = name;
        this.type = type;
        reading = 0;
    }

    @SuppressWarnings("unused")
    public double getReading() {
        return PulseSensor.round(reading, 1);
    }

    void increase(double v) {
        reading += v;
    }
}
