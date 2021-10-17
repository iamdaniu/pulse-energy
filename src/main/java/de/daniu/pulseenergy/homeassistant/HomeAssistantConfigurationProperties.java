package de.daniu.pulseenergy.homeassistant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("homeassistant")
class HomeAssistantConfigurationProperties {
    private String deviceUuid;
    private String component;
    private DiscoveryConfigurationProperties discovery;

    public String getDiscoveryTopic(String entityId) {
        return String.format("%s/%s/%s/config", discovery.getPrefix(), component, entityId);
    }
}

@Data
class DiscoveryConfigurationProperties {
    String prefix;
}
