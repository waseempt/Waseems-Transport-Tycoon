package io.github.transport_tycoon.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PathFindTest {

    @Test
    void testSimpleStraightPath() {
        GameMap map = new GameMap(5, 5);

        Tile start = map.getTile(1, 1);
        Tile middle = map.getTile(2, 1);
        Tile end = map.getTile(3, 1);

        start.setHasRoad(true);
        middle.setHasRoad(true);
        end.setHasRoad(true);

        List<Tile> path = PathFinder.findPath(map, start, end);

        assertFalse(path.isEmpty());
        assertEquals(start, path.get(0));
        assertEquals(end, path.get(path.size() - 1));
    }

    @Test
    void testPathAroundCorner() {
        GameMap map = new GameMap(5, 5);

        Tile start = map.getTile(1, 1);
        Tile turn = map.getTile(1, 2);
        Tile end = map.getTile(2, 2);

        start.setHasRoad(true);
        turn.setHasRoad(true);
        end.setHasRoad(true);

        List<Tile> path = PathFinder.findPath(map, start, end);

        assertFalse(path.isEmpty());
        assertEquals(3, path.size());
    }

    @Test
    void testNoPathFound() {
        GameMap map = new GameMap(5, 5);

        Tile start = map.getTile(0, 0);
        Tile end = map.getTile(4, 4);

        start.setHasRoad(true);

        List<Tile> path = PathFinder.findPath(map, start, end);

        assertTrue(path.isEmpty());
    }

    @Test
    void testStartEqualsEnd() {
        GameMap map = new GameMap(5, 5);

        Tile start = map.getTile(2, 2);
        start.setHasRoad(true);

        List<Tile> path = PathFinder.findPath(map, start, start);

        assertEquals(1, path.size());
        assertEquals(start, path.get(0));
    }

    @Test
    void testEndTileCanBeReachedWithoutRoad() {
        GameMap map = new GameMap(5, 5);

        Tile start = map.getTile(1, 1);
        Tile middle = map.getTile(2, 1);
        Tile end = map.getTile(3, 1);

        start.setHasRoad(true);
        middle.setHasRoad(true);

        List<Tile> path = PathFinder.findPath(map, start, end);

        assertFalse(path.isEmpty());
        assertEquals(end, path.get(path.size() - 1));
    }
}
