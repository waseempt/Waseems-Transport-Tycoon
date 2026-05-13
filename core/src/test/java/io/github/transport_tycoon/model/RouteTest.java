package io.github.transport_tycoon.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RouteTest {

    @Test
    void testConstructorInitialState() {
        Route route = new Route();

        assertNotNull(route.getStops());
        assertTrue(route.getStops().isEmpty());
        assertEquals(0, route.getStopCount());
    }

    @Test
    void testAddSingleStop() {
        Route route = new Route();

        StopTile stop = new StopTile(
            new Tile(1, 1),
            new City("Budapest")
        );

        route.addStop(stop);

        assertEquals(1, route.getStopCount());
        assertEquals(stop, route.getStops().get(0));
    }

    @Test
    void testAddMultipleStops() {
        Route route = new Route();

        StopTile stop1 = new StopTile(
            new Tile(1, 1),
            new City("City1")
        );

        StopTile stop2 = new StopTile(
            new Tile(2, 2),
            new City("City2")
        );

        route.addStop(stop1);
        route.addStop(stop2);

        assertEquals(2, route.getStopCount());

        assertEquals(stop1, route.getStops().get(0));
        assertEquals(stop2, route.getStops().get(1));
    }

    @Test
    void testStopsListReference() {
        Route route = new Route();

        assertSame(route.getStops(), route.getStops());
    }
}
