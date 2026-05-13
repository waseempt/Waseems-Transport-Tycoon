package io.github.transport_tycoon.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FacilityTest {

    @Test
    void testConstructorAndGettersSetters() {
        Facility facility = new Facility("Steel Mill");

        facility.setProduces(GoodType.STEEL);
        facility.setConsumes(GoodType.IRON);
        facility.setStoredInput(3);
        facility.setStoredOutput(7);
        facility.setProductionTimer(2.5f);

        assertEquals("Steel Mill", facility.getFacilityType());
        assertEquals(GoodType.STEEL, facility.getProduces());
        assertEquals(GoodType.IRON, facility.getConsumes());
        assertEquals(3, facility.getStoredInput());
        assertEquals(7, facility.getStoredOutput());
        assertEquals(2.5f, facility.getProductionTimer());
    }

    @Test
    void testProcessGoodsBeforeFiveSecondsDoesNothing() {
        Facility facility = new Facility("Coal Mine");
        facility.setConsumes(null);
        facility.setStoredOutput(0);

        facility.processGoods(4.9f);

        assertEquals(0, facility.getStoredOutput());
        assertEquals(4.9f, facility.getProductionTimer());
    }

    @Test
    void testProcessGoodsWithoutInputRequirementProducesOutput() {
        Facility facility = new Facility("Coal Mine");
        facility.setConsumes(null);

        facility.processGoods(5.0f);

        assertEquals(1, facility.getStoredOutput());
        assertEquals(0.0f, facility.getProductionTimer());
    }

    @Test
    void testProcessGoodsWithInputConsumesAndProduces() {
        Facility facility = new Facility("Steel Mill");
        facility.setConsumes(GoodType.IRON);
        facility.setStoredInput(2);
        facility.setStoredOutput(0);

        facility.processGoods(5.0f);

        assertEquals(1, facility.getStoredInput());
        assertEquals(1, facility.getStoredOutput());
        assertEquals(0.0f, facility.getProductionTimer());
    }

    @Test
    void testProcessGoodsWithInputRequirementButNoStoredInput() {
        Facility facility = new Facility("Steel Mill");
        facility.setConsumes(GoodType.IRON);
        facility.setStoredInput(0);
        facility.setStoredOutput(0);

        facility.processGoods(5.0f);

        assertEquals(0, facility.getStoredInput());
        assertEquals(0, facility.getStoredOutput());
        assertEquals(0.0f, facility.getProductionTimer());
    }

    @Test
    void testDefaultConstructor() {
        Facility facility = new Facility();

        assertNull(facility.getFacilityType());
        assertNull(facility.getProduces());
        assertNull(facility.getConsumes());
        assertEquals(0, facility.getStoredInput());
        assertEquals(0, facility.getStoredOutput());
        assertEquals(0.0f, facility.getProductionTimer());
    }
}
