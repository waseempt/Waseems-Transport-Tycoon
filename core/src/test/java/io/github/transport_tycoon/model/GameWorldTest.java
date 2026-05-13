package io.github.transport_tycoon.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameWorldTest {

    @Test
    void testConstructorInitialValues() {
        GameWorld world = new GameWorld("Naz");

        assertEquals("Naz", world.getTycoonName());
        assertEquals(5000f, world.getPlayerBalance());
        assertNotNull(world.getMap());
        assertNotNull(world.getCities());
        assertNotNull(world.getFacilities());
        assertNotNull(world.getRoutes());
        assertNotNull(world.getStopTiles());
    }

    @Test
    void testPauseAndResume() {
        GameWorld world = new GameWorld("Test");

        world.pause();

        assertTrue(world.isPaused());
        assertEquals(0f, world.getTimeScale());

        world.resume();

        assertFalse(world.isPaused());
        assertEquals(1.0f, world.getTimeScale());
    }

    @Test
    void testSetTimeScale() {
        GameWorld world = new GameWorld("Test");

        world.setTimeScale(4f);

        assertEquals(4f, world.getTimeScale());
    }

    @Test
    void testChangePlayerBalance() {
        GameWorld world = new GameWorld("Test");

        world.changePlayerBalance(500f);

        assertEquals(5500f, world.getPlayerBalance());

        world.changePlayerBalance(-1000f);

        assertEquals(4500f, world.getPlayerBalance());
    }

    @Test
    void testBankruptcy() {
        GameWorld world = new GameWorld("Test");

        assertFalse(world.isBankrupt());

        world.setPlayerBalance(-1);

        assertTrue(world.isBankrupt());
    }

    @Test
    void testAddVehicle() {
        GameWorld world = new GameWorld("Test");

        Vehicle vehicle = new Bus("Bus", 1);

        world.addVehicle(vehicle);

        assertEquals(1, world.getUnassignedVehicles().size());
        assertTrue(world.getUnassignedVehicles().contains(vehicle));
    }

    @Test
    void testCreateRoute() {
        GameWorld world = new GameWorld("Test");

        Route route = world.createRoute();

        assertNotNull(route);
        assertEquals(1, world.getRoutes().size());
    }

    @Test
    void testGetFormattedGameTimeInitially() {
        GameWorld world = new GameWorld("Test");

        assertEquals("00:00:00", world.getFormattedGameTime());
    }

    @Test
    void testUpdateSimulationAdvancesTime() {
        GameWorld world = new GameWorld("Test");

        world.updateSimulation(10f);

        assertNotEquals("00:00:00", world.getFormattedGameTime());
    }

    @Test
    void testGetZoneAtInvalidCoordinates() {
        GameWorld world = new GameWorld("Test");

        assertNull(world.getZoneAt(-1, -1));
    }

    @Test
    void testDefaultConstructor() {
        GameWorld world = new GameWorld();

        assertNull(world.getTycoonName());
    }
}
