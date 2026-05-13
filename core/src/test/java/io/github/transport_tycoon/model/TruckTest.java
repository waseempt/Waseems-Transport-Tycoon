package io.github.transport_tycoon.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TruckTest {

    @Test
    void testLightFreightTruck() {
        Truck truck = new Truck("Light Truck", 1, GoodType.WOOD);

        assertEquals("Light Truck", truck.getName());
        assertEquals(40, truck.getCapacity());
        assertEquals(1.2f, truck.getSpeed());
        assertEquals(GoodType.WOOD, truck.getCargoType());
        assertEquals(1, truck.getModelVariant());
        assertFalse(truck.hasRoute());
    }

    @Test
    void testHeavyHaulerTruck() {
        Truck truck = new Truck("Heavy Truck", 2, GoodType.IRON);

        assertEquals("Heavy Truck", truck.getName());
        assertEquals(80, truck.getCapacity());
        assertEquals(0.8f, truck.getSpeed());
        assertEquals(GoodType.IRON, truck.getCargoType());
        assertEquals(2, truck.getModelVariant());
    }

    @Test
    void testNonOneVariantUsesHeavyValues() {
        Truck truck = new Truck("Other Truck", 99, GoodType.COAL);

        assertEquals(80, truck.getCapacity());
        assertEquals(0.8f, truck.getSpeed());
        assertEquals(99, truck.getModelVariant());
        assertEquals(GoodType.COAL, truck.getCargoType());
    }

    @Test
    void testDefaultConstructor() {
        Truck truck = new Truck();

        assertNull(truck.getName());
        assertEquals(0, truck.getCapacity());
        assertEquals(0.0f, truck.getSpeed());
        assertNull(truck.getCargoType());
        assertEquals(0, truck.getModelVariant());
    }
}
