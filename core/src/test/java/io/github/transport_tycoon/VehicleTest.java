package io.github.transport_tycoon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VehicleTest {
    private Bus bus;
    private City city;

    @BeforeEach
    void setUp() {
        bus = new Bus("City Express");
        city = new City("Testville");
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
