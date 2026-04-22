package io.github.transport_tycoon;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TileTest {


    @Test
    void testRoadProperties() {
        Tile tile = new Tile(2, 3);
        assertFalse(tile.hasRoad());
        assertEquals(0, tile.getRoadMask());

        tile.setHasRoad(true);
        tile.setRoadMask(15);

        assertTrue(tile.hasRoad());
        assertEquals(15, tile.getRoadMask());
    }

    @Test
    void testZoneConnections() {
        Tile tile = new Tile(2, 3);
        assertEquals(0, tile.getZoneConnectionMask());
        assertNull(tile.getParentZone());

        Zone mockZone = new Zone();
        tile.setZoneConnection(5, mockZone);

        assertEquals(5, tile.getZoneConnectionMask());
        assertEquals(mockZone, tile.getParentZone());
    }

    @Test
    void testIntersectionLinking() {
        Tile tile = new Tile(2, 3);
        assertFalse(tile.hasIntersection());
        assertNull(tile.getIntersection());

        Intersection mockIntersection = new Intersection(15);
        tile.setIntersection(mockIntersection);

        assertTrue(tile.hasIntersection());
        assertEquals(mockIntersection, tile.getIntersection());
    }

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
