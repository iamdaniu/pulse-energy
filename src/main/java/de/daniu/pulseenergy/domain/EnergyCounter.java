package de.daniu.pulseenergy.domain;

import lombok.Getter;
import lombok.Setter;

public class EnergyCounter {
    @Getter
    @Setter
    private String name;

    @Getter
    private final boolean dayCounter;

    @Setter
    private double reading;

    EnergyCounter(String main) {
        this(main, true);
    }
    public EnergyCounter(String name, boolean dayCounter) {
        this.name = name;
        this.dayCounter = dayCounter;
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
