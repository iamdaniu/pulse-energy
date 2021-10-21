package de.daniu.pulseenergy.sensors;

import de.daniu.pulseenergy.domain.EnergyCounter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CounterDeciderTest {
    @Mock
    private SummerDecider summerDecider;
    @Mock
    private TimeConfigurationProperties timeConfigurationProperties;

    @InjectMocks
    private CounterDecider sut;

    @BeforeEach
    void setup() {
    }

    @Test
    void givesDayRange() {
        when(summerDecider.isSummer(any())).thenReturn(true);

        TimeRange alwaysDay = mock(TimeRange.class);
        when(alwaysDay.inRange(any())).thenReturn(true);

        SeasonConfiguration season = mock(SeasonConfiguration.class);
        when(season.getDay()).thenReturn(alwaysDay);
        when(timeConfigurationProperties.getSummer()).thenReturn(season);

        EnergyCounter expected = counter(true);
        Collection<EnergyCounter> counters = List.of(
                counter(false), expected
        );
        Optional<EnergyCounter> valid = sut.findValid(counters, LocalDateTime.now());

        assertThat(valid).isPresent();
        assertThat(valid.get()).isSameAs(expected);
    }

    @Test
    void givesNightRange() {
        when(summerDecider.isSummer(any())).thenReturn(true);

        TimeRange alwaysNight = mock(TimeRange.class);
        when(alwaysNight.inRange(any())).thenReturn(false);

        SeasonConfiguration season = mock(SeasonConfiguration.class);
        when(season.getDay()).thenReturn(alwaysNight);
        when(timeConfigurationProperties.getSummer()).thenReturn(season);

        EnergyCounter expected = counter(false);
        Collection<EnergyCounter> counters = List.of(
                counter(true), expected
        );
        Optional<EnergyCounter> valid = sut.findValid(counters, LocalDateTime.now());

        assertThat(valid).isPresent();
        assertThat(valid.get()).isSameAs(expected);
    }

    private static EnergyCounter counter(boolean day) {
        EnergyCounter result = mock(EnergyCounter.class);
        when(result.isDayCounter()).thenReturn(day);
        return result;
    }
}