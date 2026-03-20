package io.github.transport_tycoon;

import java.util.ArrayList;

public class GameWorld {
    private GameMap gameMap;
    private float timeScale = 1.0f; // Default 1x speed

    private ArrayList<City> cities;
    private ArrayList<Facility> facilities;

    public GameWorld() {
        // Initialize a 50x50 map
        this.gameMap = new GameMap(50, 50);
        this.cities = new ArrayList<>();
        this.facilities = new ArrayList<>();

        defineZones();
    }

    private void defineZones() {
        // instantiate 4 cities
        City budapest = new City("Budapest");
        assignZoneTiles(budapest, 22, 38, 5, 5); // Massive 5x5 city
        cities.add(budapest);

        City debrecen = new City("Debrecen");
        assignZoneTiles(debrecen, 8, 24, 3, 3);   // 3x3 city
        cities.add(debrecen);

        City szentendre = new City("Szentendre");
        assignZoneTiles(szentendre, 38, 12, 3, 3); // 3x3 city
        cities.add(szentendre);

        City pecs = new City("Pecs");
        assignZoneTiles(pecs, 42, 36, 4, 4); // 4x4 city
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
        assignZoneTiles(ironMine2, 28, 8, 2, 2);   // 3x3 Iron Mine
        facilities.add(ironMine2);

        System.out.println("Model: Organic map layout generated.");
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

    public void updateSimulation(float delta) {
        // tree growth, vehicle movement, etc..
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
}
