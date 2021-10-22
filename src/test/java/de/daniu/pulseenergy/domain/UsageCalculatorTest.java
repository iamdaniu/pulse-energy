package de.daniu.pulseenergy.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

class UsageCalculatorTest {
    private UsageCalculator sut = new UsageCalculator(500);

    @Test
    void usagePerKWh() {
        assertThat(sut.usagePerPulseKWh()).isEqualTo(0.002d);
    }

    @Test
    void usageBetweenPulses() {
        sut = new UsageCalculator(60);

        // pulse half a minute apart
        LocalTime firstPulse = LocalTime.of(13, 0, 0, 0);
        LocalTime secondPulse = LocalTime.of(13, 0, 30);
        long millis = firstPulse.until(secondPulse, ChronoUnit.MILLIS);

        assertThat(sut.getUsage(millis)).isEqualTo(2_000);
    }
}