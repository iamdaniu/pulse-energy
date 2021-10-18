package de.daniu.pulseenergy.homeassistant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.daniu.pulseenergy.SensorService;
import de.daniu.pulseenergy.SensorUpdateEvent;
import de.daniu.pulseenergy.mqtt.MqttService;
import de.daniu.pulseenergy.PulseSensor;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;


@Service
class EntityService implements ApplicationListener<SensorUpdateEvent> {
    private final MqttService mqttService;
    private ObjectMapper mapper = new ObjectMapper();

    public EntityService(MqttService mqttService,
                         HomeAssistantConfigurationProperties properties,
                         SensorService sensorService) {
        this.mqttService = mqttService;

        DiscoverySender discoverySender = new DiscoverySender(properties);
        sensorService.getAllSensors().forEach(discoverySender::sendDiscovery);
    }

    @Override
    public void onApplicationEvent(SensorUpdateEvent event) {
        PulseSensor sensor = event.getSensor();
        try {
            String state_payload = mapper.writeValueAsString(sensor);
            mqttService.sendMessage(Entities.topicFromSensorname(sensor.getName()), state_payload);
        } catch (JsonProcessingException | MqttException e) {
            e.printStackTrace();
        }
    }

    @RequiredArgsConstructor
    class DiscoverySender {
        private final HomeAssistantConfigurationProperties properties;

        private void sendDiscovery(PulseSensor sensor) {
            Entities.entitiesForSensor(sensor)
                    .forEach(this::sendDiscovery);
        }

        private void sendDiscovery(EntityDefinition entityDefinition) {
            try {
                String payload = mapper.writeValueAsString(entityDefinition);
                mqttService.sendMessage(properties.getDiscoveryTopic(entityDefinition.getUnique_id()), payload);
            } catch (JsonProcessingException | MqttException e) {
                e.printStackTrace();
            }
        }
    }
}

