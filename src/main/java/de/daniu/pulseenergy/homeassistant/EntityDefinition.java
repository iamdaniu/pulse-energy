package de.daniu.pulseenergy.homeassistant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

@NoArgsConstructor
@AllArgsConstructor
@Data
class DeviceDefinition {
    private String name;
    private String manufacturer;
    private String ids;
}