package io.github.transport_tycoon;

import java.util.ArrayList;

public class GameWorld {
    private GameMap gameMap;
    private float timeScale = 1.0f; // Default 1x speed
    private float forestGrowthTimer = 0f;
    private static final float FOREST_GROWTH_INTERVAL = 60f;
    //speed for tree

    //Cost of building a road
    private static final float ROAD_COST = 100f;
    private static final float ROAD_REFUND = 50f;

    private ArrayList<City> cities;
    private ArrayList<Facility> facilities;

    //the tycoon name is entered on the SetupScreen
    private String tycoonName;

    private float playerBalance;

    public GameWorld(String tycoonName) {
        this.tycoonName = tycoonName;
        this.playerBalance = 5000;

        this.gameMap = new GameMap(50, 50);
        this.cities = new ArrayList<>();
        this.facilities = new ArrayList<>();

        defineZones();
        generateInitialForests();
    }

    //pauses the simulation and sets the time to 0
    public void pause() {
        timeScale = 0f;
        System.out.println("Model: Simulation paused.");
    }

    //resumes the game and restores the time scale to x1
    public void resume() {
        timeScale = 1.0f;
    }

    public String getTycoonName() {
        return tycoonName; }


    private void defineZones() {

        City budapest = new City("Budapest");
        assignZoneTiles(budapest, 22, 38, 5, 5); // Massive 5x5 city
        cities.add(budapest);

        City debrecen = new City("Debrecen");
        assignZoneTiles(debrecen, 8, 24, 4, 4);   // 4x4 city
        cities.add(debrecen);

        City szentendre = new City("Szentendre");
        assignZoneTiles(szentendre, 38, 12, 3, 3); // 3x3 city
        cities.add(szentendre);

        City pecs = new City("Pecs");
        assignZoneTiles(pecs, 42, 36, 3, 3); // 3x3 city
        cities.add(pecs);

        // Instantiate 5 Facilities
        Facility coalMine = new Facility("Coal Mine");
        assignZoneTiles(coalMine, 5, 42, 3, 3);    // 3x3 Coal Mine
        facilities.add(coalMine);

        Facility ironMine = new Facility("Iron Mine");
        assignZoneTiles(ironMine, 38, 26, 3, 3);   // 3x3 Iron Mine
        facilities.add(ironMine);

        Facility steelMill = new Facility("Steel Mill");
        assignZoneTiles(steelMill, 20, 18, 4, 4);  // 4x4 Steel Mill
        facilities.add(steelMill);

        Facility lumberCamp = new Facility("Lumber Camp");
        assignZoneTiles(lumberCamp, 12, 5, 4, 4);  // 4x4 Lumber Camp
        facilities.add(lumberCamp);

        Facility ironMine2 = new Facility("Iron Mine");
        assignZoneTiles(ironMine2, 28, 8, 3, 3);   // 3x3 Iron Mine
        facilities.add(ironMine2);

        System.out.println("Model: Organic map layout generated.");
    }

    private void generateInitialForests() {
        for (int x = 0; x < 50; x++) {
            for (int y = 0; y < 50; y++) {

                Tile tile = gameMap.getTile(x, y);
                if (tile == null) continue;
                //the first forest
                if (Math.random() < 0.05) {
                    int trees = 1 + (int)(Math.random() * 4);
                    tile.setTreeCount(trees);
                }
            }
        }
    }

    private void assignZoneTiles(Zone zone, int startX, int startY, int width, int height) {
        zone.setDimensions(width, height);

        for (int x = startX; x < startX + width; x++) {
            for (int y = startY; y < startY + height; y++) {
                Tile tile = gameMap.getTile(x, y);
                if (tile != null) {
                    zone.getTiles().add(tile);
                }
            }
        }
    }
    // with time is gonna be more with this part...
    public void updateSimulation(float delta) {
        forestGrowthTimer += delta;

        if (forestGrowthTimer >= FOREST_GROWTH_INTERVAL) {
            forestGrowthTimer = 0f;
            growForests();
        }
    }
    // grows all forest tiles by +1 (max 4)...that it... and now going around
    private void growForests() {

        ArrayList<Tile> newForests = new ArrayList<>();

        for (int x = 0; x < 50; x++) {
            for (int y = 0; y < 50; y++) {

                Tile tile = gameMap.getTile(x, y);
                if (tile == null) continue;

                if (tile.hasForest()) {

                    if (tile.getTreeCount() < 4) {
                        tile.setTreeCount(tile.getTreeCount() + 1);
                    }

                    if (tile.getTreeCount() == 4) {
                        addNeighbors(x, y, newForests);
                    }
                }
            }
        }

        for (Tile t : newForests) {
            if (!t.hasForest()) {
                t.setTreeCount(1);
            }
        }
    }
    //spreads trees to neighboring tiles if there are 4 trees on that tile
    private void addNeighbors(int x, int y, ArrayList<Tile> list) {

        addIfValid(x + 1, y, list);
        addIfValid(x - 1, y, list);
        addIfValid(x, y + 1, list);
        addIfValid(x, y - 1, list);
    }

    private void addIfValid(int x, int y, ArrayList<Tile> list) {
        Tile tile = gameMap.getTile(x, y);
        if (tile != null) {
            list.add(tile);
        }
    }

    public void buildRoad(int x, int y) {
        Tile tile = gameMap.getTile(x, y);

        // Ensure the tile exists and doesn't ALREADY have a road
        if (tile != null && !tile.hasRoad()) {
            if (playerBalance >= ROAD_COST) {
                playerBalance -= ROAD_COST;
                tile.setHasRoad(true);
                System.out.println("Built road at " + x + ", " + y + " | Balance: $" + playerBalance);
            } else {
                System.out.println("Not enough money to build a road!");
            }
        }
    }

    public void removeRoad(int x, int y) {
        Tile tile = gameMap.getTile(x, y);

        // check that tile is actually a road tile
        if (tile != null && tile.hasRoad()) {

            // Remove the road
            tile.setHasRoad(false);

            // Give the refund
            playerBalance += ROAD_REFUND;

            // logging
            System.out.println("Bulldozed road at " + x + ", " + y + " | Refunded $50 | Balance: $" + playerBalance);
        } else {
            System.out.println("Bulldoze failed");
        }
    }

    public ArrayList<City> getCities() {
        return cities;
    }

    public ArrayList<Facility> getFacilities() {
        return facilities;
    }

    public float getTimeScale() {
        return timeScale;
    }

    public GameMap getMap() {
        return gameMap;
    }

    public float getPlayerBalance() {
        return playerBalance;
    }

    public void setPlayerBalance(float b) {
        playerBalance = b;
    }

    public boolean isBankrupt () {
        if (playerBalance < 0 )
            return true;

        return false;
    }
}
