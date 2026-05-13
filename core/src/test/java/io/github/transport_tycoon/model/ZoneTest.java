package io.github.transport_tycoon.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ZoneTest {

    @Test
    void testDefaultConstructor() {
        Zone zone = new Zone();

        assertNotNull(zone.getTiles());
        assertTrue(zone.getTiles().isEmpty());
    }

    @Test
    void testSetDimensions() {
        Zone zone = new Zone();

        zone.setDimensions(5, 7);

        assertEquals(5, zone.getGridWidth());
        assertEquals(7, zone.getGridHeight());
    }

    @Test
    void testGetAnchorTileReturnsFirstTile() {
        Zone zone = new Zone();

        Tile tile1 = new Tile(1, 1);
        Tile tile2 = new Tile(2, 2);

        zone.getTiles().add(tile1);
        zone.getTiles().add(tile2);

        assertEquals(tile1, zone.getAnchorTile());
    }

    @Test
    void testGetAnchorTileReturnsNullForEmptyZone() {
        Zone zone = new Zone();

        assertNull(zone.getAnchorTile());
    }
}
