package io.github.transport_tycoon.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BasicModelTest {

    @Test
    void testBusInitialization() {
        Bus bus = new Bus("City Line", 1);
        assertEquals("City Line", bus.getName());
        assertEquals(40, bus.getCapacity());
        assertEquals(1.5f, bus.getSpeed());
        assertEquals(GoodType.PASSENGERS, bus.getCargoType());
    }

    @Test
    void testTruckInitialization() {
        Truck truck = new Truck("Heavy Hauler", 1,GoodType.STEEL);
        assertEquals("Heavy Hauler", truck.getName());
        assertEquals(60, truck.getCapacity());
        assertEquals(1.0f, truck.getSpeed());
        assertEquals(GoodType.STEEL, truck.getCargoType());
    }

    @Test
    void testStopTile() { // Ensures a stop tile links the tile to the zone properly
        Tile tile = new Tile(5, 5);
        Zone zone = new Zone();
        StopTile stop = new StopTile(tile, zone);

        assertEquals(tile, stop.getTile());
        assertEquals(zone, stop.getLinkedZone());
    }

    @Test
    void testZoneDimensionsAndAnchor() {  // Tests zone dimensions and anchor tile
        Zone zone = new Zone();
        zone.setDimensions(3, 4);

        assertEquals(3, zone.getGridWidth());
        assertEquals(4, zone.getGridHeight());
        assertNull(zone.getAnchorTile()); // Empty initially

        Tile tile = new Tile(1, 1);
        zone.getTiles().add(tile);
        assertEquals(tile, zone.getAnchorTile());
    }

    @Test
    void testGoodTypeEnum() { // Ensures GoodType in implemented correctly
        assertNotNull(GoodType.valueOf("PASSENGERS"));
        assertNotNull(GoodType.valueOf("STEEL"));
        assertEquals(5, GoodType.values().length);
    }
}
