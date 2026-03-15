package io.github.transport_tycoon;

public class GameMap {
    private final int width;
    private final int height;
    private Tile[][] grid;

    public GameMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Tile[width][height];
        initializeMap();
    }

    private void initializeMap() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = new Tile(x, y);
            }
        }
        System.out.println("Model: GameMap generated with " + (width * height) + " tiles.");
    }

    public Tile getTile(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return grid[x][y];
        }
        return null;
    }
}
