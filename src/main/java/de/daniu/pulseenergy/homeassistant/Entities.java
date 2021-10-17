package de.daniu.pulseenergy.homeassistant;


import java.util.stream.Stream;

class Entities {
    private static final DeviceDefinition DEVICE = new DeviceDefinition("pulse_energy_meter", "daniu", "47f8e41c-bfb3-4e5c-b383-515eb0f073da");

    static Stream<EntityDefinition> entitiesForSensor(String name) {
        return Stream.of(
                currentUsageEntity(name),
                totalUsageEntity(name),
                usageCounterReadout(name)
        );
    }

    static String topicFromSensorname(String sensorname) {
        return "energy/" + sensorname.toLowerCase();
    }

    private static EntityDefinition currentUsageEntity(String name) {
        EntityDefinition result = new EntityDefinition();
        result.setDevice_class("power");
        result.setUnit_of_measurement("W");
        result.setIcon("mdi:power");
        result.setState_topic(topicFromSensorname(name));
        result.setValue_template("{{ value_json.currentUsage }}");

        result.setName(name + " Current Usage");
        result.setUnique_id(String.format("%s-%s-CU", DEVICE.getIds(), name));
        result.setDevice(DEVICE);
        return result;
    }

    private static EntityDefinition totalUsageEntity(String name) {
        return usageBuilder()
                .value_template("{{ value_json.totalUsage }}")
                .name(name + " Total Usage")
                .unique_id(String.format("%s-%s-TU", DEVICE.getIds(), name))
                .state_topic(topicFromSensorname(name))
                .build();
    }
    private static EntityDefinition usageCounterReadout(String name) {
        return usageBuilder()
                .value_template("{{ value_json.counter }}")
                .name(name + " Meter")
                .unique_id(String.format("%s-%s-CR", DEVICE.getIds(), name))
                .state_topic(topicFromSensorname(name))
                .build();
    }

    private static EntityDefinition.EntityDefinitionBuilder usageBuilder() {
        return new EntityDefinition.EntityDefinitionBuilder()
                .device_class("energy")
                .unit_of_measurement("kWh")
                .icon("mdi:energy")
                .device(DEVICE);
    }
}
