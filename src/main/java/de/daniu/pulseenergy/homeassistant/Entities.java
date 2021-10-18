package de.daniu.pulseenergy.homeassistant;


import de.daniu.pulseenergy.PulseSensor;

import java.util.stream.Stream;

class Entities {
    private static final DeviceDefinition DEVICE = new DeviceDefinition("pulse_energy_meter", "daniu", "47f8e41c-bfb3-4e5c-b383-515eb0f073da");

    static Stream<EntityDefinition> entitiesForSensor(PulseSensor sensor) {
        String name = sensor.getName();
        return Stream.concat(
                Stream.of(
                        currentUsageEntity(name),
                        totalUsageEntity(name)
                ),
                sensor.getCounters().values().stream()
                        .map(c -> counterReadout(name, c)));
    }

    static String topicFromSensorname(String sensorname) {
        return "energy/" + sensorname.toLowerCase();
    }

    private static EntityDefinition currentUsageEntity(String name) {
        return new EntityDefinition.EntityDefinitionBuilder()
                .name(name + " Current Usage")
                .unique_id(String.format("%s-%s-CU", DEVICE.getIds(), name))
                .device_class("power")
                .unit_of_measurement("W")
                .icon("mdi:power-plug")
                .state_topic(topicFromSensorname(name))
                .value_template("{{ value_json.currentUsage }}")

                .device(DEVICE)
                .build();
    }

    private static EntityDefinition totalUsageEntity(String name) {
        return usageBuilder()
                .value_template("{{ value_json.totalUsage }}")
                .name(name + " Total Usage")
                .unique_id(String.format("%s-%s-TU", DEVICE.getIds(), name.toLowerCase()))
                .state_topic(topicFromSensorname(name))
                .icon("mdi:transmission-tower")
                .build();
    }

    private static EntityDefinition counterReadout(String sensorName, PulseSensor.EnergyCounter energyCounter) {
        String counterName = energyCounter.getName();
        return usageBuilder()
                .value_template(String.format("{{ value_json.counters.%s.reading }}", counterName.toLowerCase()))
                .name(sensorName + " " + counterName + " Meter")
                .unique_id(String.format("%s-%s-%s", DEVICE.getIds(), sensorName.toLowerCase(), counterName.toLowerCase()))
                .state_topic(topicFromSensorname(sensorName))
                .icon("mdi:counter")
                .build();
    }

    private static EntityDefinition.EntityDefinitionBuilder usageBuilder() {
        return new EntityDefinition.EntityDefinitionBuilder()
                .device_class("energy")
                .unit_of_measurement("kWh")
                .device(DEVICE);
    }
}
