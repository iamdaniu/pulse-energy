package de.daniu.pulseenergy;

import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    private Map<String, EnergyCounter> counters = Map.of("main", new EnergyCounter("main"));

    public void pulse(Instant pulseTime) {
        if (lastPulse != null) {
            secondsBetweenLastPulses = (double) lastPulse.until(pulseTime, ChronoUnit.MILLIS) / 1_000d;
        }
        lastPulse = pulseTime;
        totalPulses++;

        LocalDateTime localDateTime = LocalDateTime.ofInstant(pulseTime, ZoneId.systemDefault());
        int hour = localDateTime.get(ChronoField.HOUR_OF_DAY);
        for (EnergyCounter counter : counters.values()) {
            if (counter.validFor(hour)) {
                counter.increase(1d / pulsesPerKWh);
            }
        }
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

    public static class EnergyCounter {
        @Getter
        @Setter
        private String name;
        private final int from;
        private final int to;

        @Setter
        private double reading;

        public EnergyCounter(String main) {
            this(main, 0, 24);
        }
        public EnergyCounter(String name, int from, int to) {
            this.name = name;
            this.from = from;
            this.to = to;
            reading = 0;
        }

        @SuppressWarnings("unused")
        public double getReading() {
            return PulseSensor.round(reading, 1);
        }

        public boolean validFor(int hourOfDay) {
            boolean result;
            if (from < to) {
                result = from <= hourOfDay && hourOfDay < to;
            } else {
                result = hourOfDay <= to || hourOfDay >= from;
            }
            return result;
        }
        public void increase(double v) {
            reading += v;
        }
    }
}

