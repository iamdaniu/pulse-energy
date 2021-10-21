package de.daniu.pulseenergy.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Service;

@Service
public class MqttService {
    private IMqttClient client;
    public MqttService(MqttConfigurationProperties properties) throws MqttException {
        String uri = String.format("tcp://%s:%d", properties.getHost(), properties.getPort());
        client = new MqttClient(uri, properties.getPublisher());

        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(properties.getUser());
        options.setPassword(properties.getPassword());
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        client.connect(options);
    }

    public void sendMessage(String topic, String payload) throws MqttException {
        MqttMessage message = new MqttMessage(payload.getBytes());
        message.setQos(0);
        message.setRetained(true);
        client.publish(topic, message);
    }
}
