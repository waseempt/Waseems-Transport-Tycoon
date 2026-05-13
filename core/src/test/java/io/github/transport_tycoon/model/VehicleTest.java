package io.github.transport_tycoon.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VehicleTest {

    @Test
    void testBusVehicleBasicProperties() {
        Vehicle vehicle = new Bus("City Bus", 1);

        assertEquals("City Bus", vehicle.getName());
        assertEquals(30, vehicle.getCapacity());
        assertEquals(1.5f, vehicle.getSpeed());
        assertEquals(GoodType.PASSENGERS, vehicle.getCargoType());
    }

    @Test
    void testTruckVehicleBasicProperties() {
        Vehicle vehicle = new Truck("Cargo Truck", 2, GoodType.STEEL);

        assertEquals("Cargo Truck", vehicle.getName());
        assertEquals(80, vehicle.getCapacity());
        assertEquals(0.8f, vehicle.getSpeed());
        assertEquals(GoodType.STEEL, vehicle.getCargoType());
    }

    @Test
    void testSetPosition() {
        Vehicle vehicle = new Bus("Bus", 1);

        vehicle.setPosition(100f, 200f);

        assertEquals(100f, vehicle.getWorldX());
        assertEquals(200f, vehicle.getWorldY());
    }

    @Test
    void testAssignRoute() {
        Vehicle vehicle = new Bus("Bus", 1);

        Route route = new Route();

        vehicle.assignRoute(route);

        assertTrue(vehicle.hasRoute());
        assertEquals(route, vehicle.getAssignedRoute());
    }

    @Test
    void testAssignNullRoute() {
        Vehicle vehicle = new Bus("Bus", 1);

        vehicle.assignRoute(null);

        assertFalse(vehicle.hasRoute());
        assertNull(vehicle.getAssignedRoute());
    }

    @Test
    void testPurchasePrice() {
        Vehicle vehicle = new Truck("Truck", 1, GoodType.WOOD);

        vehicle.setPurchasePrice(500f);

        assertEquals(500f, vehicle.getPurchasePrice());
    }

    @Test
    void testPendingSale() {
        Vehicle vehicle = new Bus("Bus", 1);

        assertFalse(vehicle.isPendingSale());

        vehicle.sellAfterDelivery();

        assertTrue(vehicle.isPendingSale());
    }

    @Test
    void testPendingRetirement() {
        Vehicle vehicle = new Bus("Bus", 1);

        assertFalse(vehicle.isPendingRetirement());

        vehicle.retireAfterDelivery();

        assertTrue(vehicle.isPendingRetirement());
    }

    @Test
    void testSetCurrentTile() {
        Vehicle vehicle = new Bus("Bus", 1);

        Tile tile = new Tile(2, 3);

        vehicle.setCurrentTile(tile);

        assertEquals(tile, vehicle.getCurrentTile());
    }

    @Test
    void testClearPathDoesNotCrash() {
        Vehicle vehicle = new Bus("Bus", 1);

        assertDoesNotThrow(vehicle::clearPath);
    }

    @Test
    void testDefaultConstructorBus() {
        Vehicle vehicle = new Bus();

        assertNull(vehicle.getName());
        assertEquals(0, vehicle.getCapacity());
        assertEquals(0.0f, vehicle.getSpeed());
        assertNull(vehicle.getCargoType());
    }

    @Test
    void testDefaultConstructorTruck() {
        Vehicle vehicle = new Truck();

        assertNull(vehicle.getName());
        assertEquals(0, vehicle.getCapacity());
        assertEquals(0.0f, vehicle.getSpeed());
        assertNull(vehicle.getCargoType());
    }
}
