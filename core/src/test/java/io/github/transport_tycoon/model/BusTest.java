package io.github.transport_tycoon.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BusTest {

    @Test
    void testExpressBusVariantOne() {
        Bus bus = new Bus("Express Bus", 1);

        assertEquals("Express Bus", bus.getName());
        assertEquals(30, bus.getCapacity());
        assertEquals(1.5f, bus.getSpeed());
        assertEquals(GoodType.PASSENGERS, bus.getCargoType());
        assertEquals(1, bus.getModelVariant());
        assertFalse(bus.hasRoute());
    }

    @Test
    void testHeavyTransitVariantTwo() {
        Bus bus = new Bus("Heavy Transit", 2);

        assertEquals("Heavy Transit", bus.getName());
        assertEquals(60, bus.getCapacity());
        assertEquals(1.0f, bus.getSpeed());
        assertEquals(GoodType.PASSENGERS, bus.getCargoType());
        assertEquals(2, bus.getModelVariant());
    }

    @Test
    void testAnyNonOneVariantUsesHeavyTransitValues() {
        Bus bus = new Bus("Other Bus", 99);

        assertEquals(60, bus.getCapacity());
        assertEquals(1.0f, bus.getSpeed());
        assertEquals(99, bus.getModelVariant());
    }

    @Test
    void testDefaultConstructor() {
        Bus bus = new Bus();

        assertNull(bus.getName());
        assertEquals(0, bus.getCapacity());
        assertEquals(0.0f, bus.getSpeed());
        assertNull(bus.getCargoType());
        assertEquals(0, bus.getModelVariant());
        assertFalse(bus.hasRoute());
    }
}
