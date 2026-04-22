package io.github.transport_tycoon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RouteTest {

    // Variables used in the test
    private Route route;
    private StopTile stop1;
    private StopTile stop2;

    @BeforeEach
    void setUp() {
        // This runs before each test

        route = new Route(); // create an empty route

        // create two stops with different positions
        stop1 = new StopTile(new Tile(5, 5), null);
        stop2 = new StopTile(new Tile(10, 10), null);
    }

    @Test
    void testAddStop() {

        // check that route is empty at the beginning
        assertEquals(0, route.getStopCount());

        // add one stop to the route
        route.addStop(stop1);

        // now the number of stops should be 1
        assertEquals(1, route.getStopCount());

        // check that the added stop is exactly stop1
        assertEquals(stop1, route.getStops().get(0));
    }
}
