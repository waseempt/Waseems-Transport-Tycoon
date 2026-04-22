package io.github.transport_tycoon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VehicleTest {
    private Bus bus;
    private Vehicle vehicle;
    private City city;

    @BeforeEach
    void setUp() {
        bus = new Bus("City Express");
        city = new City("Testville");
        vehicle = new Bus("Test Bus");
    }

    @Test
    void testPositionGettersAndSetters() {
        vehicle.setPosition(100f, 200f);
        assertEquals(100f, vehicle.getWorldX());
        assertEquals(200f, vehicle.getWorldY());
    }

    @Test
    void testRouteAssignment() {
        assertNull(vehicle.getAssignedRoute());

        Route route = new Route();
        vehicle.assignRoute(route);
        assertEquals(route, vehicle.getAssignedRoute());
    }

    @Test
    void testVehicleAttributes() {
        assertEquals("Test Bus", vehicle.getName());
        assertEquals(40, vehicle.getCapacity());
        assertEquals(1.5f, vehicle.getSpeed());
        assertEquals(GoodType.PASSENGERS, vehicle.getCargoType());
    }

    @Test
    void testVehicleInitialization() {
        assertEquals("City Express", bus.getName());
        assertEquals(40, bus.getCapacity());
        assertEquals(GoodType.PASSENGERS, bus.getCargoType());
    }

    @Test
    void testLoadCargoFromCity() {
        // Since cities generate unlimited passengers, loading should fill the bus to its capacity
        bus.loadCargo(city);

        // This will now successfully check the new getter you added!
        assertEquals(40, bus.getCurrentLoad());
    }
}
