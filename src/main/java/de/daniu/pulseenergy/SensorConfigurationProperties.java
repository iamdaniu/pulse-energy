package de.daniu.pulseenergy;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties
public class SensorConfigurationProperties {
    private String name;
    private double pulsesPerKwh;
    private String guid;
}
