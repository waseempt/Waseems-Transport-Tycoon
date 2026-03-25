package io.github.transport_tycoon;

public class Tile {
    private int gridX;
    private int gridY;

    private boolean hasRoad = false;

    // A road tile has a roadMask which acts as a unique code depending on it's neighboring roads and their orientations.
    // This allows us to identify a road's orientation and type when drawing.
    private int roadMask = 0;

    // Similar to roadMask, identifies if a tile is an entry point for a city or facility
    private int zoneConnectionMask = 0;
    private Zone parentZone = null;

    private int treeCount;

    public Tile(int x, int y) {
        this.gridX = x;
        this.gridY = y;
        this.treeCount = 0;
    }

    public int getGridX() { return gridX; }
    public int getGridY() { return gridY; }

    public int getTreeCount() {
        return treeCount;
    }

    public boolean hasForest() {
        return treeCount > 0;
    }

    public boolean hasRoad() { return hasRoad; }
    public int getRoadMask() { return roadMask; }
    public int getZoneConnectionMask() { return zoneConnectionMask; }
    public Zone getParentZone() { return parentZone; }

    public void setTreeCount(int treeCount) {
        if (treeCount < 0) treeCount = 0;
        if (treeCount > 4) treeCount = 4;
        this.treeCount = treeCount;
    }

    public void setHasRoad(boolean hasRoad) {
        this.hasRoad = hasRoad;
    }

    public void setRoadMask(int mask) {
        this.roadMask = mask;
    }

    public void setZoneConnection(int mask, Zone zone) {
        this.zoneConnectionMask = mask;
        this.parentZone = zone;
    }
}
