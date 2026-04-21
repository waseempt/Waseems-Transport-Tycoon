package io.github.transport_tycoon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameMapTest {
    private GameMap map;

    @BeforeEach
    void setUp() {
        map = new GameMap(50, 50);
    }

    @Test
    void testMapInitialization() {
        Tile tile = map.getTile(10, 10);
        assertNotNull(tile);
        assertEquals(10, tile.getGridX());
        assertEquals(10, tile.getGridY());
    }

    @Test
    void testOutOfBoundsReturnsNull() {
        assertNull(map.getTile(-1, 0));
        assertNull(map.getTile(0, -1));
        assertNull(map.getTile(50, 50)); // Max index is 49
    }
}
