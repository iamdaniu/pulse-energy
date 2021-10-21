package de.daniu.pulseenergy.domain;

import de.daniu.pulseenergy.sensors.CounterDecider;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@ToString
@RequiredArgsConstructor
public class PulseSensor {
    private static final double SECONDS_PER_HOUR = 60 * 60;

    private final CounterDecider counterDecider;
    @Getter
    private final String name;
    private final double pulsesPerKWh;

    private long totalPulses;
    private Instant lastPulse;
    private double secondsBetweenLastPulses;

    private Map<String, EnergyCounter> counters = Map.of("main", new EnergyCounter("main"));

    public void pulse(Instant pulseTime) {
        if (lastPulse != null) {
            long millisSinceLastPulse = lastPulse.until(pulseTime, ChronoUnit.MILLIS);
            if (millisSinceLastPulse < 200) {
                log.warn("Time since last pulse too short ({} ms), dropping current impulse", millisSinceLastPulse);
                return;
            }
            secondsBetweenLastPulses = (double) millisSinceLastPulse / 1_000d;
        }
        lastPulse = pulseTime;
        totalPulses++;

        LocalDateTime localDateTime = LocalDateTime.ofInstant(pulseTime, ZoneId.systemDefault());
        counterDecider.findValid(counters.values(), localDateTime)
                .ifPresent(c -> c.increase(1d / pulsesPerKWh));
    }

    // in watts
    @SuppressWarnings("unused")
    public int getCurrentUsage() {
        double result = 0;
        if (secondsBetweenLastPulses != 0) {
            double pulsesPerSecond = 1.0d / secondsBetweenLastPulses;
            double pulsesPerHour = SECONDS_PER_HOUR * pulsesPerSecond;
            result = 1000 * pulsesPerHour / pulsesPerKWh;
        }
        return (int) result;
    }

    @SuppressWarnings("unused")
    public double getTotalUsage() {
        return round((double) totalPulses / pulsesPerKWh, 3);
    }

    public void setCounters(List<EnergyCounter> newCounters) {
        counters = newCounters.stream()
                .collect(Collectors.toMap(e -> e.getName().toLowerCase(), Function.identity()));
    }
    public Map<String, EnergyCounter> getCounters() {
        return Collections.unmodifiableMap(counters);
    }

    public void setCounterBase(String name, double currentCounter) {
        counters.get(name)
                .setReading(currentCounter);
    }

    static double round(double toRound, int digits) {
        return new BigDecimal(toRound).setScale(digits, RoundingMode.HALF_UP).doubleValue();
    }

}

