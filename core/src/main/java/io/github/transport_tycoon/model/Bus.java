package io.github.transport_tycoon.model;

public class Bus extends Vehicle {

    private int modelVariant;

    public Bus(String name, int variant) {
        // Variant 1: Express Bus
        // Variant 2: Heavy Transit
        super(name, variant == 1 ? 30 : 60, variant == 1 ? 1.5f : 1.0f, GoodType.PASSENGERS);
        this.modelVariant = variant;

        // Update maintenance cost based on variant
        setMaintenanceCost(variant == 1 ? 100f : 50f);
    }

    public Bus() {}

    @Override
    public int getModelVariant() {
        return modelVariant;
    }
}
