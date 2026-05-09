package io.github.transport_tycoon.model;

import java.util.HashMap;
import java.util.Map;
import java.util.EnumMap;

public class City extends Zone {

    private String name;
    private Map<GoodType, Integer> demands;

    private float demandTimer = 0f;
    private static final float DEMAND_UPDATE_INTERVAL = 10f;

    public City(String name) {
        super();
        this.name = name;

        this.demands = new HashMap<>();
        this.demands.put(GoodType.PASSENGERS, 50);
        this.demands.put(GoodType.WOOD, 20);
        this.demands.put(GoodType.IRON, 10);
        this.demands.put(GoodType.STEEL, 30);
    }

    public City() {}

    public String getName() {
        return name;
    }

    public Map<GoodType, Integer> getDemands() {
        return demands;
    }

    public void consumeGoods(GoodType type, int amount) {
        if (type == null || amount <= 0) {
            return;
        }

        int currentDemand = demands.getOrDefault(type, 0);
        int newDemand = Math.max(0, currentDemand - amount);
        demands.put(type, newDemand);
    }

    public void updateDemands(float delta) {
        demandTimer += delta;

        if (demandTimer >= DEMAND_UPDATE_INTERVAL) {
            demandTimer -= DEMAND_UPDATE_INTERVAL;

            demands.put(GoodType.PASSENGERS, demands.get(GoodType.PASSENGERS) + 5);
            demands.put(GoodType.WOOD, demands.get(GoodType.WOOD) + 2);
            demands.put(GoodType.IRON, demands.get(GoodType.IRON) + 1);
            demands.put(GoodType.STEEL, demands.get(GoodType.STEEL) + 2);
        }
    }

    // Used to restore enum values in the map from strings to enum (bug fix for loading saved game)
    public void restoreDemands() {
        if (demands == null) return;

        Map<GoodType, Integer> fixedDemands = new HashMap<>();

        // Iterate using Object to avoid type-casting crashes
        for (Object key : demands.keySet()) {
            if (key instanceof String) {
                // Convert the String back into our proper Enum
                fixedDemands.put(GoodType.valueOf((String) key), demands.get(key));
            } else if (key instanceof GoodType) {
                // Already an Enum (safe fallback)
                fixedDemands.put((GoodType) key, demands.get(key));
            }
        }

        // Replace the broken map with our repaired one
        this.demands = fixedDemands;
    }
}
