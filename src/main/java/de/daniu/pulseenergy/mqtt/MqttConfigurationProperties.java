package de.daniu.pulseenergy.mqtt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("mqtt")
public class MqttConfigurationProperties {
    private String publisher;

    private String host;
    private int port;

    private String user;
    private char[] password;
}
