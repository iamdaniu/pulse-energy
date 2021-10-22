package de.daniu.pulseenergy.sensors;

import de.daniu.pulseenergy.domain.CounterType;
import de.daniu.pulseenergy.domain.EnergyCounter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


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

        EnergyCounter expected = counter(CounterType.day);
        Collection<EnergyCounter> counters = List.of(
                counter(CounterType.night), expected
        );
        Optional<EnergyCounter> valid = sut.findValid(counters, LocalDateTime.now())
                .findFirst();

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

        EnergyCounter expected = counter(CounterType.night);
        Collection<EnergyCounter> counters = List.of(
                counter(CounterType.day), expected
        );
        Optional<EnergyCounter> valid = sut.findValid(counters, LocalDateTime.now())
                .findFirst();

        assertThat(valid).isPresent();
        assertThat(valid.get()).isSameAs(expected);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4})
    void givesAlways(int inRangeAndSeason) {
        boolean inRange = (inRangeAndSeason & 1) == 1;
        boolean isSummer = (inRangeAndSeason & 2) == 2;

        when(summerDecider.isSummer(any())).thenReturn(isSummer);

        TimeRange range = mock(TimeRange.class);
        when(range.inRange(any())).thenReturn(inRange);

        SeasonConfiguration season = mock(SeasonConfiguration.class);
        when(season.getDay()).thenReturn(range);
        lenient().when(timeConfigurationProperties.getSummer()).thenReturn(season);
        lenient().when(timeConfigurationProperties.getWinter()).thenReturn(season);

        EnergyCounter expected = counter(CounterType.always);
        Collection<EnergyCounter> counters = List.of(
                counter(CounterType.day), expected, counter(CounterType.night)
        );
        Optional<EnergyCounter> valid = sut.findValid(counters, LocalDateTime.now())
                .filter(c -> c == expected)
                .findFirst();

        assertThat(valid).isPresent();
    }

    private static EnergyCounter counter(CounterType type) {
        EnergyCounter result = mock(EnergyCounter.class);
        lenient().when(result.getType()).thenReturn(type);
        return result;
    }
}