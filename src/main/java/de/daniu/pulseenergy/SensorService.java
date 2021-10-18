package de.daniu.pulseenergy;

import java.util.Collection;

public interface SensorService {
    void countImpulse(String sensorId);

    Collection<PulseSensor> getAllSensors();

    void setSensorCounter(String sensorId, String counterId, double counter);
}
