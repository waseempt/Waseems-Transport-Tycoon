package io.github.transport_tycoon;

public class Tile {
    private int gridX;
    private int gridY;

    public Tile(int x, int y) {
        this.gridX = x;
        this.gridY = y;
    }

    public int getGridX() { return gridX; }
    public int getGridY() { return gridY; }
}
