package io.github.transport_tycoon;

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
}
