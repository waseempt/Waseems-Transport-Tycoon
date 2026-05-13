package io.github.transport_tycoon.model;

public class Truck extends Vehicle {

    private int modelVariant;

    public Truck(String name, int variant, GoodType cargoType) {
        // Variant 1: Light Freight
        // Variant 2: Heavy Hauler
        super(name, variant == 1 ? 40 : 80, variant == 1 ? 1.2f : 0.8f, cargoType);
        this.modelVariant = variant;

        // Update maintenance cost based on variant
        setMaintenanceCost(variant == 1 ? 100f : 50f);
    }

    public Truck() {}

    @Override
    public int getModelVariant() {
        return modelVariant;
    }
}
