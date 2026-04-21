package io.github.transport_tycoon;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TileTest {

    @Test
    void testTreeCountConstraints() {
        Tile tile = new Tile(5, 5);

        tile.setTreeCount(5); // Attempt to set above max
        assertEquals(4, tile.getTreeCount()); // Should cap at 4

        tile.setTreeCount(-2); // Attempt to set below min
        assertEquals(0, tile.getTreeCount()); // Should floor at 0
    }

    @Test
    void testHasForest() {
        Tile tile = new Tile(0, 0);
        assertFalse(tile.hasForest());

        tile.setTreeCount(2);
        assertTrue(tile.hasForest());
    }
}
