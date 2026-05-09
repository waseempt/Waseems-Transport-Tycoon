package io.github.transport_tycoon.model;

public class Facility extends Zone {
    private String facilityType;

    private GoodType produces;
    private GoodType consumes;
    private int storedInput;
    private int storedOutput;
    private float productionTimer;

    public Facility(String facilityType) {
        super();
        this.facilityType = facilityType;
    }

    public Facility() {}

    public String getFacilityType() {
        return facilityType;
    }

    public GoodType getProduces() {
        return produces;
    }

    public void setProduces(GoodType produces) {
        this.produces = produces;
    }

    public GoodType getConsumes() {
        return consumes;
    }

    public void setConsumes(GoodType consumes) {
        this.consumes = consumes;
    }

    public int getStoredInput() {
        return storedInput;
    }

    public void setStoredInput(int storedInput) {
        this.storedInput = storedInput;
    }

    public int getStoredOutput() {
        return storedOutput;
    }

    public void setStoredOutput(int storedOutput) {
        this.storedOutput = storedOutput;
    }

    public float getProductionTimer() {
        return productionTimer;
    }

    public void setProductionTimer(float productionTimer) {
        this.productionTimer = productionTimer;
    }

    public void processGoods(float scaledDelta) {
        productionTimer += scaledDelta;

        if (productionTimer >= 5.0f) {
            productionTimer = 0f;

            if (consumes == null) {
                storedOutput += 1;
            } else {
                if (storedInput > 0) {
                    storedInput -= 1;
                    storedOutput += 1;
                }
            }
        }
    }
}
