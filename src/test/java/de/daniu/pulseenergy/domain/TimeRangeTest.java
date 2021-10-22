package de.daniu.pulseenergy.domain;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class TimeRangeTest {
    private LocalDate today = LocalDate.now();

    @ParameterizedTest
    @MethodSource("outsideTimes")
    void outsideRange(int from, int to, LocalTime localTime) {
        // given
        TimeRange sut = new TimeRange(from, to);
        LocalDateTime time = LocalDateTime.of(today, localTime);

        // when
        boolean result = sut.inRange(time);

        // then
        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @MethodSource("insideTimes")
    void insideRange(int from, int to, LocalTime ltime) {
        // given
        TimeRange sut = new TimeRange(from, to);
        LocalDateTime time = LocalDateTime.of(today, ltime);

        // when
        boolean result = sut.inRange(time);

        // then
        assertThat(result).isTrue();
    }

    private static Stream<Arguments> outsideTimes() {
        return Stream.of(
                Arguments.of(7, 20, LocalTime.of(6, 59)),
                Arguments.of(7, 20, LocalTime.of(20, 0)),
                Arguments.of(20, 7, LocalTime.of(19, 59)),
                Arguments.of(20, 7, LocalTime.of(7, 0))
                );
    }

    private static Stream<Arguments> insideTimes() {
        return Stream.of(
                Arguments.of(7, 20, LocalTime.of(7, 0)),
                Arguments.of(7, 20, LocalTime.of(19, 59)),
                Arguments.of(20, 7, LocalTime.of(20, 0)),
                Arguments.of(20, 7, LocalTime.of(6, 59))
                );
    }
}