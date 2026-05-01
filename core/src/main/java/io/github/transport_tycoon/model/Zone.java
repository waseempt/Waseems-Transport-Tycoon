package io.github.transport_tycoon.model;

import java.util.ArrayList;

public class Zone {
    protected ArrayList<Tile> tiles;
    protected int gridWidth;
    protected int gridHeight;

    public Zone() {
        this.tiles = new ArrayList<>();
    }

    public void setDimensions(int width, int height) {
        this.gridWidth = width;
        this.gridHeight = height;
    }

    public int getGridWidth() {
        return gridWidth;
    }
    public int getGridHeight() {
        return gridHeight;
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public Tile getAnchorTile() {
        if (!tiles.isEmpty()) {
            return tiles.get(0);
        }
        return null;
    }
}
