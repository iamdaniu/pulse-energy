package de.daniu.pulseenergy.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class CounterDecider {
    private final SummerDecider summerDecider;
    private final TimeConfigurationProperties timeConfiguration;

    public Stream<EnergyCounter> findValid(Collection<EnergyCounter> counters, LocalDateTime now) {
        TimeRange dayRange = summerDecider.isSummer(now.toLocalDate())
                ? timeConfiguration.getSummer().getDay()
                : timeConfiguration.getWinter().getDay();
        boolean isDay = dayRange.inRange(now);
        return counters.stream()
                .filter(c -> matchingType(isDay, c.getType()));
    }

    private static boolean matchingType(boolean isDay, CounterType type) {
        switch (type) {
            case day:
                return isDay;
            case night:
                return !isDay;
            case always:
        }
        return true;
    }
}

@Component
@RequiredArgsConstructor
class SummerDecider {
    private final TimeConfigurationProperties timeConfiguration;

    boolean isSummer(LocalDate now) {
        boolean result = false;
        LocalDate summerStart = timeConfiguration.getSummer().getStart().forYear(now.getYear());
        if (now.isAfter(summerStart)) {
            LocalDate summerEndDate = timeConfiguration.getWinter().getStart().forYear(now.getYear())
                    .minusDays(1);
            result = now.isBefore(summerEndDate);
        }
        return result;
    }
}

