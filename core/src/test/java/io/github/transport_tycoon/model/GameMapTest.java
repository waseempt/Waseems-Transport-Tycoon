package io.github.transport_tycoon.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameMapTest {

    @Test
    void testConstructorCreatesTiles() {
        GameMap map = new GameMap(5, 5);

        assertNotNull(map.getTile(0, 0));
        assertNotNull(map.getTile(4, 4));
    }

    @Test
    void testGetTileCoordinates() {
        GameMap map = new GameMap(3, 3);

        Tile tile = map.getTile(1, 2);

        assertEquals(1, tile.getGridX());
        assertEquals(2, tile.getGridY());
    }

    @Test
    void testGetTileOutOfBoundsNegative() {
        GameMap map = new GameMap(3, 3);

        assertNull(map.getTile(-1, 0));
        assertNull(map.getTile(0, -1));
    }

    @Test
    void testGetTileOutOfBoundsTooLarge() {
        GameMap map = new GameMap(3, 3);

        assertNull(map.getTile(3, 0));
        assertNull(map.getTile(0, 3));
        assertNull(map.getTile(100, 100));
    }

    @Test
    void testDifferentTilesAreDifferentObjects() {
        GameMap map = new GameMap(3, 3);

        Tile tile1 = map.getTile(0, 0);
        Tile tile2 = map.getTile(1, 1);

        assertNotEquals(tile1, tile2);
    }

    @Test
    void testDefaultConstructor() {
        GameMap map = new GameMap();

        assertNull(map.getTile(0, 0));
    }
}
