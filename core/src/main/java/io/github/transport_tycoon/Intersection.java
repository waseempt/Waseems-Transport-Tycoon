package io.github.transport_tycoon;

import java.util.HashMap;
import java.util.Map;

public class Intersection {
    private boolean hasTrafficLights = false;

    // Integers for directions: 0=North, 1=East, 2=South, 3=West
    private Map<Integer, TrafficLight> trafficLights;

    public Intersection() {
        this.trafficLights = new HashMap<>();
    }

    public void installLights() {
        this.hasTrafficLights = true;

        // Initial states: North/South Green, East/West Red
        trafficLights.put(0, new TrafficLight(LightState.GREEN));
        trafficLights.put(2, new TrafficLight(LightState.GREEN));

        trafficLights.put(1, new TrafficLight(LightState.RED));
        trafficLights.put(3, new TrafficLight(LightState.RED));
    }

    public boolean hasLights() {
        return hasTrafficLights;
    }

    public void setGreenDuration(int direction, float seconds) {
        // Will be implemented in milestone 4
    }

    public void updateLights(float delta) {
        // Do nothing if the player hasn't bought the upgrade
        if (!hasTrafficLights) {
            return;
        }

        // Logic...
    }
}
