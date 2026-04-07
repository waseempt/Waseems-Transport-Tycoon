package io.github.transport_tycoon;

import java.util.Map;
import java.util.EnumMap;

public class City extends Zone {

    private String name;
    private Map<GoodType, Integer> demands;

    public City(String name) {
        super();
        this.name = name;

        // initialize demands
        this.demands = new EnumMap<>(GoodType.class);
        this.demands.put(GoodType.PASSENGERS, 50);
        this.demands.put(GoodType.WOOD, 20);
        this.demands.put(GoodType.IRON, 10);
        this.demands.put(GoodType.STEEL, 30);
    }

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
}
