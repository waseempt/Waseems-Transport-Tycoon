package io.github.transport_tycoon;

public class Facility extends Zone {
    private String facilityType;

    public Facility(String facilityType) {
        super();
        this.facilityType = facilityType;
    }

    public String getFacilityType() {
        return facilityType;
    }
}
