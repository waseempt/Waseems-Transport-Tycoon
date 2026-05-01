package io.github.transport_tycoon.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PathFinderTest {
    private GameMap map;
    private Tile start;
    private Tile end;

    @BeforeEach
    void setUp() {
        map = new GameMap(10, 10);
        start = map.getTile(0, 0);
        end = map.getTile(3, 0);

        // Build a straight road from (0,0) to (3,0)
        map.getTile(1, 0).setHasRoad(true);
        map.getTile(2, 0).setHasRoad(true);
    }

    @Test
    void testFindPathStraightLine() {
        List<Tile> path = PathFinder.findPath(map, start, end);

        // Path should contain: (0,0), (1,0), (2,0), (3,0)
        assertEquals(4, path.size());
        assertEquals(map.getTile(1, 0), path.get(1));
        assertEquals(end, path.get(3));
    }

    @Test
    void testFindPathFailsWhenBlocked() {
        // Break the road
        map.getTile(1, 0).setHasRoad(false);

        List<Tile> path = PathFinder.findPath(map, start, end);

        // Should return empty list because no valid road connects them
        assertTrue(path.isEmpty());
    }
}
