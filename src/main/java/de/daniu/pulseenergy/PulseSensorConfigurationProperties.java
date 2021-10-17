package de.daniu.pulseenergy;

import de.daniu.pulseenergy.SensorConfigurationProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties
public class PulseSensorConfigurationProperties {
    private List<SensorConfigurationProperties> sensors;

}
