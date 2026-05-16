package io.github.transport_tycoon.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TileTest {

    @Test
    void testConstructorAndDefaultValues() {
        Tile tile = new Tile(3, 4);

        assertEquals(3, tile.getGridX());
        assertEquals(4, tile.getGridY());
        assertFalse(tile.hasRoad());
        assertEquals(0, tile.getRoadMask());
        assertEquals(0, tile.getZoneConnectionMask());
        assertNull(tile.getParentZone());
        assertEquals(0, tile.getTreeCount());
        assertFalse(tile.hasForest());
        assertNull(tile.getIntersection());
        assertFalse(tile.hasIntersection());
    }

    @Test
    void testSetTreeCountNormalValue() {
        Tile tile = new Tile(0, 0);

        tile.setTreeCount(3);

        assertEquals(3, tile.getTreeCount());
        assertTrue(tile.hasForest());
    }

    @Test
    void testSetTreeCountBelowZeroClampsToZero() {
        Tile tile = new Tile(0, 0);

        tile.setTreeCount(-5);

        assertEquals(0, tile.getTreeCount());
        assertFalse(tile.hasForest());
    }

    @Test
    void testSetTreeCountAboveFourClampsToFour() {
        Tile tile = new Tile(0, 0);

        tile.setTreeCount(10);

        assertEquals(4, tile.getTreeCount());
        assertTrue(tile.hasForest());
    }

    @Test
    void testSetHasRoad() {
        Tile tile = new Tile(0, 0);

        tile.setHasRoad(true);

        assertTrue(tile.hasRoad());

        tile.setHasRoad(false);

        assertFalse(tile.hasRoad());
    }

    @Test
    void testSetRoadMask() {
        Tile tile = new Tile(0, 0);

        tile.setRoadMask(15);

        assertEquals(15, tile.getRoadMask());
    }

    @Test
    void testSetZoneConnection() {
        Tile tile = new Tile(0, 0);
        Zone zone = new Zone();

        tile.setZoneConnection(4, zone);

        assertEquals(4, tile.getZoneConnectionMask());
        assertEquals(zone, tile.getParentZone());
    }

    @Test
    void testSetParentZone() {
        Tile tile = new Tile(0, 0);
        Zone zone = new Zone();

        tile.setParentZone(zone);

        assertEquals(zone, tile.getParentZone());
    }

    @Test
    void testSetIntersection() {
        Tile tile = new Tile(0, 0);
        Intersection intersection = new Intersection(15);

        tile.setIntersection(intersection);

        assertEquals(intersection, tile.getIntersection());
        assertTrue(tile.hasIntersection());

        tile.setIntersection(null);

        assertNull(tile.getIntersection());
        assertFalse(tile.hasIntersection());
    }

    @Test
    void testDefaultConstructor() {
        Tile tile = new Tile();

        assertEquals(0, tile.getGridX());
        assertEquals(0, tile.getGridY());
        assertFalse(tile.hasRoad());
        assertEquals(0, tile.getTreeCount());
    }
}
