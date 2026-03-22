package io.github.transport_tycoon;

public class Tile {
    private int gridX;
    private int gridY;
    private boolean hasRoad = false;

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


    public void setTreeCount(int treeCount) {
        if (treeCount < 0) treeCount = 0;
        if (treeCount > 4) treeCount = 4;
        this.treeCount = treeCount;
    }

    public void setHasRoad(boolean hasRoad) {
        this.hasRoad = hasRoad;
    }
}
