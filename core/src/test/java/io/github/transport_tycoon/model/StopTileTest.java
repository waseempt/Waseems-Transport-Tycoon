package io.github.transport_tycoon.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StopTileTest {

    @Test
    void testConstructorAndGetters() {
        Tile tile = new Tile(5, 10);
        Zone zone = new Zone();

        StopTile stopTile = new StopTile(tile, zone);

        assertEquals(tile, stopTile.getTile());
        assertEquals(zone, stopTile.getLinkedZone());
    }

    @Test
    void testSetTile() {
        Tile oldTile = new Tile(1, 1);
        Tile newTile = new Tile(2, 2);

        StopTile stopTile = new StopTile(oldTile, null);

        stopTile.setTile(newTile);

        assertEquals(newTile, stopTile.getTile());
    }

    @Test
    void testSetLinkedZone() {
        Zone oldZone = new Zone();
        Zone newZone = new Zone();

        StopTile stopTile = new StopTile(null, oldZone);

        stopTile.setLinkedZone(newZone);

        assertEquals(newZone, stopTile.getLinkedZone());
    }

    @Test
    void testDefaultConstructor() {
        StopTile stopTile = new StopTile();

        assertNull(stopTile.getTile());
        assertNull(stopTile.getLinkedZone());
    }
}
