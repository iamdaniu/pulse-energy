package de.daniu.pulseenergy.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ConfigurationProperties(prefix = "times")
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
class TimeConfigurationProperties {
    private SeasonConfiguration summer;
    private SeasonConfiguration winter;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class SeasonConfiguration {
    private StartDate start;
    private TimeRange day;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class StartDate {
    private int day;
    private int month;

    LocalDate forYear(int year) {
        return LocalDate.of(year, month, day);
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class TimeRange {
    private int from;
    private int to;

    boolean inRange(LocalDateTime time) {
        boolean result;
        int hourOfDay = time.getHour();
        if (from < to) {
            result = from <= hourOfDay && hourOfDay < to;
        } else {
            result = hourOfDay < to || hourOfDay >= from;
        }
        return result;
    }
}