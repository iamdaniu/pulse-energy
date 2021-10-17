package de.daniu.pulseenergy.homeassistant;

import de.daniu.pulseenergy.SensorConfigurationProperties;

import java.util.List;

public class Entities {
    private static final DeviceDefinition DEVICE = new DeviceDefinition("pulse_energy_meter", "daniu", "47f8e41c-bfb3-4e5c-b383-515eb0f073da");

    static List<EntityDefinition> forSensorConfiguration(SensorConfigurationProperties sensorConfiguration) {
        return List.of(
                currentUsageEntity(sensorConfiguration),
                totalUsageEntity(sensorConfiguration),
                usageCounterReadout(sensorConfiguration)
        );
    }

    static String topicFromSensorname(String sensorname) {
        return "energy/" + sensorname.toLowerCase();
    }

    private static EntityDefinition currentUsageEntity(SensorConfigurationProperties sensorConfiguration) {
        EntityDefinition result = new EntityDefinition();
        result.setDevice_class("power");
        result.setUnit_of_measurement("W");
        result.setIcon("mdi:power");
        String lowercaseName = sensorConfiguration.getName().toLowerCase();
        result.setState_topic(topicFromSensorname(sensorConfiguration.getName()));
        result.setValue_template("{{ value_json.currentUsage }}");

        result.setName(sensorConfiguration.getName() + " Current Usage");
        result.setUnique_id(String.format("%s-%s-CU", DEVICE.getIds(), sensorConfiguration.getName()));
        result.setDevice(DEVICE);
        return result;
    }
    private static EntityDefinition totalUsageEntity(SensorConfigurationProperties sensorConfiguration) {
        EntityDefinition result = new EntityDefinition();
        result.setDevice_class("energy");
        result.setUnit_of_measurement("kWh");
        result.setIcon("mdi:energy");
        String lowercaseName = sensorConfiguration.getName().toLowerCase();
        result.setState_topic(topicFromSensorname(sensorConfiguration.getName()));
        result.setValue_template("{{ value_json.totalUsage }}");

        result.setName(sensorConfiguration.getName() + " Total Usage");
        result.setUnique_id(String.format("%s-%s-TU", DEVICE.getIds(), sensorConfiguration.getName()));
        result.setDevice(DEVICE);
        return result;
    }
    private static EntityDefinition usageCounterReadout(SensorConfigurationProperties sensorConfiguration) {
        EntityDefinition result = new EntityDefinition();
        result.setDevice_class("energy");
        result.setUnit_of_measurement("kWh");
        result.setIcon("mdi:energy");
        String lowercaseName = sensorConfiguration.getName().toLowerCase();
        result.setState_topic(topicFromSensorname(sensorConfiguration.getName()));
        result.setValue_template("{{ value_json.counter }}");

        result.setName(sensorConfiguration.getName() + " Meter");
        result.setUnique_id(String.format("%s-%s-CR", DEVICE.getIds(), sensorConfiguration.getName()));
        result.setDevice(DEVICE);
        return result;
    }
}
