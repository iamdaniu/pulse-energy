package de.daniu.pulseenergy;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@ToString
@RequiredArgsConstructor
public class PulseSensor {
    private static final double SECONDS_PER_HOUR = 60 * 60;

    @Getter
    private final String name;
    private final double pulsesPerKWh;

    private long totalPulses;
    private Instant lastPulse;
    private double secondsBetweenLastPulses;

    private double counterBase;

    public void pulse(Instant pulseTime) {
        if (lastPulse != null) {
            secondsBetweenLastPulses = (double) lastPulse.until(pulseTime, ChronoUnit.MILLIS) / 1_000d;
        }
        lastPulse = pulseTime;
        totalPulses++;
    }

    // in watts
    public int getCurrentUsage() {
        double result = 0;
        if (secondsBetweenLastPulses != 0) {
            double pulsesPerSecond = 1.0d / secondsBetweenLastPulses;
            double pulsesPerHour = SECONDS_PER_HOUR * pulsesPerSecond;
            result = 1000 * pulsesPerHour / pulsesPerKWh;
        }
        return (int) result;
    }

    public double getTotalUsage() {
        return round((double) totalPulses / pulsesPerKWh, 3);
    }

    public void setCounterBase(double currentCounter) {
        counterBase = currentCounter - getTotalUsage();
    }

    public double getCounter() {
        return round(counterBase + getTotalUsage(), 1);
    }

    private static double round(double toRound, int digits) {
        return new BigDecimal(toRound).setScale(digits, RoundingMode.UP).doubleValue();
    }
}
