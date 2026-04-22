package io.github.transport_tycoon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameWorldTest {
    private GameWorld world;
    private City origin;
    private City destination;

    @BeforeEach
    void setUp() {
        world = new GameWorld("TestCorp");

        // Setup mock cities for distance calculation
        origin = new City("City A");
        origin.getTiles().add(new Tile(0, 0)); // Anchor at 0,0

        destination = new City("City B");
        destination.getTiles().add(new Tile(10, 0)); // Anchor at 10,0. Distance = 10.
    }

    @Test
    void testInitialBalanceAndBankruptcy() {
        assertFalse(world.isBankrupt());

        world.setPlayerBalance(-100f);
        assertTrue(world.isBankrupt());
    }

    @Test
    void testCalculateDeliveryProfit() {
        float initialBalance = world.getPlayerBalance();

        // Distance is 10. Passenger multiplier is 1.5. Amount is 5.
        // Profit = 10 * 1.5 * 5 = 75.0
        world.calculateDeliveryProfit(origin, destination, GoodType.PASSENGERS, 5);

        assertEquals(initialBalance + 75.0f, world.getPlayerBalance());
    }

    @Test
    void testCalculateDeliveryProfitHighValueCargo() {
        float initialBalance = world.getPlayerBalance();

        // Distance is 10. Steel multiplier is 5.0. Amount is 2.
        // Profit = 10 * 5.0 * 2 = 100.0
        world.calculateDeliveryProfit(origin, destination, GoodType.STEEL, 2);

        assertEquals(initialBalance + 100.0f, world.getPlayerBalance());
    }

    @Test
    void testTimeScaleControls() {
        // it should not be paused
        assertFalse(world.isPaused());

        world.setTimeScale(0f);
        assertTrue(world.isPaused());
    }

    @Test
    void testCreateRoute() {
        Route newRoute = world.createRoute();
        assertNotNull(newRoute);
        // it should have 0 stops when first created
        assertEquals(0, newRoute.getStopCount());
    }
}
