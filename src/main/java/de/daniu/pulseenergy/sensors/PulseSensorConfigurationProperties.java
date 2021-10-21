package de.daniu.pulseenergy.sensors;

import de.daniu.pulseenergy.domain.EnergyCounter;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties
class PulseSensorConfigurationProperties {
    private List<SensorConfigurationProperties> sensors;
}

@Data
class SensorConfigurationProperties {
    private String name;
    private double pulsesPerKwh;
    private String guid;
    private List<EnergyCounterConfigurationProperties> counters;
}

@Data
class EnergyCounterConfigurationProperties {
    private String name;
    private String type;

    EnergyCounter create() {
        return new EnergyCounter(name, "day".equals(type));
    }
}