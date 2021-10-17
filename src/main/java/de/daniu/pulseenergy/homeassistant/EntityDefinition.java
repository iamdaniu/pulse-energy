package de.daniu.pulseenergy.homeassistant;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
class EntityDefinition {
    private String name;
    private DeviceDefinition device;
    private String unique_id;
    private String device_class;
    private String icon;

    private String state_topic;
    private String value_template;

    private String unit_of_measurement;
}

@AllArgsConstructor
@Data
class DeviceDefinition {
    private String name;
    private String manufacturer;
    private String ids;
}