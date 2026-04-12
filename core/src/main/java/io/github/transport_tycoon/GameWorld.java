package io.github.transport_tycoon;

import java.util.ArrayList;

public class GameWorld {
    private GameMap gameMap;
    private float timeScale = 1.0f; // Default 1x speed
    private float forestGrowthTimer = 0f;
    //speed for tree
    private static final float FOREST_GROWTH_INTERVAL = 60f;
    //game time
    private float elapsedGameTime = 0f;
    //
    public void setTimeScale(float timeScale) {
        this.timeScale = timeScale;
    }

    public boolean isPaused() {
        return timeScale == 0f;
    }

    //

    //Building costs
    private static final float ROAD_COST = 100f;
    private static final float ROAD_REFUND = 50f;
    private static final float TREE_CLEAR_COST = 20f;

    private ArrayList<City> cities;
    private ArrayList<Facility> facilities;
    private ArrayList<Vehicle> unassignedVehicles = new ArrayList<>();
    //the tycoon name is entered on the SetupScreen
    private String tycoonName;

    private float playerBalance;

    //Indicates when balance is changed
    public interface BalanceChangeListener {
        void onBalanceChanged(float changeAmount);
    }

    private BalanceChangeListener balanceListener;

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


    // Whether the player is currently in stop-building mode
    private boolean buildStopMode = false;

    // All stop tiles placed on the map
    private ArrayList<StopTile> stopTiles = new ArrayList<>();
    // All routes currently defined in the game
    private ArrayList<Route> routes = new ArrayList<>();

    ArrayList<Tile> newForests = new ArrayList<>();



    public boolean isBuildStopMode() { return buildStopMode; }
    public void setBuildStopMode(boolean mode) { this.buildStopMode = mode; }
    public ArrayList<StopTile> getStopTiles() { return stopTiles; }
    public ArrayList<Route> getRoutes() { return routes; }


    //place a stop tile at the given grid coordinates.
    public boolean tryPlaceStop(int gridX, int gridY) {
        Tile tile = gameMap.getTile(gridX, gridY);
        if (tile == null) return false;

        // Check the tile is not already a stop
        for (StopTile stop : stopTiles) {
            if (stop.getTile() == tile) {
                System.out.println("Model: Tile already has a stop.");
                return false;
            }
        }

        // Must be adjacent to a road
        if (!isAdjacentToRoad(gridX, gridY)) {
            System.out.println("Model: Stop must be adjacent to a road.");
            return false;
        }

        // Must be adjacent to a zone
        Zone adjacentZone = findAdjacentZone(gridX, gridY);
        if (adjacentZone == null) {
            System.out.println("Model: Stop must be adjacent to a zone.");
            return false;
        }

        // Check funds
        if (playerBalance < 60) {
            System.out.println("Model: Not enough money to build a stop.");
            return false;
        }

        // Deduct cost
        playerBalance -= 60;
        if (balanceListener != null) balanceListener.onBalanceChanged(-60);

        // Place the stop
        StopTile stop = new StopTile(tile, adjacentZone);
        stopTiles.add(stop);
        buildStopMode = false;
        System.out.println("Model: Stop placed at (" + gridX + ", " + gridY + ") linked to " + adjacentZone.getClass().getSimpleName());
        return true;
    }

    // Checks if any of the 4 neighboring tiles has a road.
    private boolean isAdjacentToRoad(int x, int y) {
        Tile north = gameMap.getTile(x, y + 1);
        Tile south = gameMap.getTile(x, y - 1);
        Tile east  = gameMap.getTile(x + 1, y);
        Tile west  = gameMap.getTile(x - 1, y);

        return (north != null && north.hasRoad()) ||
            (south != null && south.hasRoad()) ||
            (east  != null && east.hasRoad())  ||
            (west  != null && west.hasRoad());
    }

    // Searches the 4 neighboring tiles for one that belongs to a city or facility zone.
    private Zone findAdjacentZone(int x, int y) {
        int[][] neighbors = {{x+1,y},{x-1,y},{x,y+1},{x,y-1}};
        for (int[] n : neighbors) {
            Tile neighbor = gameMap.getTile(n[0], n[1]);
            if (neighbor == null) continue;
            for (City city : cities) {
                if (city.getTiles().contains(neighbor)) return city;
            }
            for (Facility facility : facilities) {
                if (facility.getTiles().contains(neighbor)) return facility;
            }
        }
        return null;
    }

    private void defineZones() {

        City budapest = new City("Budapest");
        assignZoneTiles(budapest, 22, 38, 5, 5); // Massive 5x5 city

        Tile budapestSouth = gameMap.getTile(22 + 2, 38 + 0);
        if (budapestSouth != null) {
            budapestSouth.setZoneConnection(4, budapest);
        }

        Tile budapestEast = gameMap.getTile(22 + 4, 38 + 1);
        if (budapestEast != null) {
            budapestEast.setZoneConnection(2, budapest);
        }

        Tile budapestEast2 = gameMap.getTile(22 + 4, 38 + 3);
        if (budapestEast2 != null) {
            budapestEast2.setZoneConnection(2, budapest);
        }

        Tile budapestWest = gameMap.getTile(22, 38 + 1);
        if (budapestWest != null) {
            budapestWest.setZoneConnection(8, budapest);
        }

        Tile budapestWest2 = gameMap.getTile(22, 38 + 3);
        if (budapestWest2 != null) {
            budapestWest2.setZoneConnection(8, budapest);
        }
        cities.add(budapest);

        City debrecen = new City("Debrecen");
        assignZoneTiles(debrecen, 8, 24, 4, 4);   // 4x4 city

        Tile debrecenEast = gameMap.getTile(8 + 3, 24 + 3);
        if (debrecenEast != null) {
            debrecenEast.setZoneConnection(2, debrecen);
        }

        Tile debrecenEast2 = gameMap.getTile(8 + 3, 24);
        if (debrecenEast2 != null) {
            debrecenEast2.setZoneConnection(2, debrecen);
        }
        cities.add(debrecen);

        City szentendre = new City("Szentendre");
        assignZoneTiles(szentendre, 38, 12, 3, 3); // 3x3 city

        Tile szentendreWest = gameMap.getTile(38 + 0, 12 + 1);
        if (szentendreWest != null) {
            szentendreWest.setZoneConnection(8, szentendre);
        }

        Tile szentendreSouth = gameMap.getTile(38 + 2, 12 + 0);
        if (szentendreSouth != null) {
            szentendreSouth.setZoneConnection(4, szentendre);
        }
        cities.add(szentendre);

        City pecs = new City("Pecs");
        assignZoneTiles(pecs, 42, 36, 3, 3); // 3x3 city

        Tile pecsWest = gameMap.getTile(42 + 0, 36 + 1);
        if (pecsWest != null) {
            pecsWest.setZoneConnection(8, pecs);
        }

        Tile pecsSouth = gameMap.getTile(42 + 2, 36 + 0);
        if (pecsSouth != null) {
            pecsSouth.setZoneConnection(4, pecs);
        }
        cities.add(pecs);

        // Instantiate 5 Facilities
        Facility coalMine = new Facility("Coal Mine");
        coalMine.setProduces(GoodType.COAL);
        coalMine.setConsumes(null);
        assignZoneTiles(coalMine, 5, 42, 3, 3);    // 3x3 Coal Mine

        Tile coalMineSouth = gameMap.getTile(5 + 2, 42 + 0);
        if (coalMineSouth != null) {
            coalMineSouth.setZoneConnection(4, coalMine);
        }
        facilities.add(coalMine);

        Facility ironMine = new Facility("Iron Mine");
        ironMine.setProduces(GoodType.IRON);
        ironMine.setConsumes(null);
        assignZoneTiles(ironMine, 38, 26, 3, 3);   // 3x3 Iron Mine

        Tile ironMineSouth = gameMap.getTile(38 + 2, 26 + 0);
        if (ironMineSouth != null) {
            ironMineSouth.setZoneConnection(4, ironMine);
        }
        facilities.add(ironMine);

        Facility steelMill = new Facility("Steel Mill");
        steelMill.setProduces(GoodType.STEEL);
        steelMill.setConsumes(GoodType.IRON);
        assignZoneTiles(steelMill, 20, 18, 4, 4);  // 4x4 Steel Mill

        Tile steelMillWest = gameMap.getTile(20, 18);
        if (steelMillWest != null) {
            steelMillWest.setZoneConnection(8, steelMill);
        }
        facilities.add(steelMill);

        Facility lumberCamp = new Facility("Lumber Camp");
        lumberCamp.setProduces(GoodType.WOOD);
        lumberCamp.setConsumes(null);
        assignZoneTiles(lumberCamp, 12, 5, 4, 4);  // 4x4 Lumber Camp

        Tile lumberCampSouth = gameMap.getTile(12, 5);
        if (lumberCampSouth != null) {
            lumberCampSouth.setZoneConnection(4, lumberCamp);
        }
        facilities.add(lumberCamp);

        Facility ironMine2 = new Facility("Iron Mine");
        ironMine2.setProduces(GoodType.IRON);
        ironMine2.setConsumes(null);
        assignZoneTiles(ironMine2, 28, 8, 3, 3);   // 3x3 Iron Mine

        Tile ironMine2South = gameMap.getTile(28 + 2, 8 + 0);
        if (ironMine2South != null) {
            ironMine2South.setZoneConnection(4, ironMine2);
        }
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
        float scaledDelta = delta * timeScale;
        elapsedGameTime += scaledDelta;
        System.out.println("speed: " + timeScale + " | scaledDelta: " + scaledDelta);

        forestGrowthTimer += scaledDelta;

        if (forestGrowthTimer >= FOREST_GROWTH_INTERVAL) {
            forestGrowthTimer = 0f;
            growForests();
        }
    }
    // grows all forest tiles by +1 (max 4)...that it... and now going around
    private void growForests() {

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

    // Enforces building a road only if it connects to another road tile
    private boolean isValidBuildLocation(int x, int y) {
        Tile north = gameMap.getTile(x, y + 1);
        Tile south = gameMap.getTile(x, y - 1);
        Tile east = gameMap.getTile(x + 1, y);
        Tile west = gameMap.getTile(x - 1, y);

        if (north != null && (north.hasRoad() || north.getZoneConnectionMask() == 4)) return true;

        if (east != null && (east.hasRoad() || east.getZoneConnectionMask() == 8)) return true;

        if (south != null && (south.hasRoad() || south.getZoneConnectionMask() == 1)) return true;

        if (west != null && (west.hasRoad() || west.getZoneConnectionMask() == 2)) return true;

        // If we check all 4 sides and find nothing, building is blocked
        return false;
    }

    public void buildRoad(int x, int y) {
        Tile tile = gameMap.getTile(x, y);

        if (!isValidBuildLocation(x, y)) {
            System.out.println("Cannot build here, roads connect to an existing road, city or facility.");
            return; // Stop the method instantly
        }

        float totalCost = ROAD_COST + (tile.getTreeCount() * TREE_CLEAR_COST);

        // Ensure the tile exists and doesn't ALREADY have a road
        if (tile != null && !tile.hasRoad()) {
            if (playerBalance >= totalCost) {
                playerBalance -= totalCost;
                tile.setHasRoad(true);
                tile.setTreeCount(0);

                updateTileAndNeighbors(x, y);

                if (balanceListener != null) balanceListener.onBalanceChanged(-totalCost);

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

            tile.setRoadMask(0);
            updateTileAndNeighbors(x, y);

            if (balanceListener != null) balanceListener.onBalanceChanged(ROAD_REFUND);

            // logging
            System.out.println("Bulldozed road at " + x + ", " + y + " | Refunded $50 | Balance: $" + playerBalance);
        } else {
            System.out.println("Bulldoze failed");
        }
    }

    // Neighbor-finding algorithm
    private void updateRoadTile(int x, int y) {
        Tile tile = gameMap.getTile(x, y);
        if (tile == null || !tile.hasRoad()) return;

        int mask = 0;

        // Check all 4 neighbors
        Tile north = gameMap.getTile(x, y + 1);
        Tile south = gameMap.getTile(x, y - 1);
        Tile east = gameMap.getTile(x + 1, y);
        Tile west = gameMap.getTile(x - 1, y);

        // Each type and orientation of road has a different mask
        // Check North: Is it a road? OR Is it a Zone Entry facing South (4)?
        if (north != null && (north.hasRoad() || north.getZoneConnectionMask() == 4)) mask += 1;

        // Check East: Is it a road? OR Is it a Zone Entry facing West (8)?
        if (east != null && (east.hasRoad() || east.getZoneConnectionMask() == 8)) mask += 2;

        // Check South: Is it a road? OR Is it a Zone Entry facing North (1)?
        if (south != null && (south.hasRoad() || south.getZoneConnectionMask() == 1)) mask += 4;

        // Check West: Is it a road? OR Is it a Zone Entry facing East (2)?
        if (west != null && (west.hasRoad() || west.getZoneConnectionMask() == 2)) mask += 8;

        tile.setRoadMask(mask);

        // Update whether this tile is an intersection tile or not
        if (mask == 7 || mask == 11 || mask == 13 || mask == 14 || mask == 15) {
            tile.setIntersection(new Intersection());
        } else {
            tile.setIntersection(null);
        }
    }

    // updates a tile and it's neighbors, called on build or remove
    private void updateTileAndNeighbors(int x, int y) {

        updateRoadTile(x, y);

        updateRoadTile(x, y + 1);
        updateRoadTile(x, y - 1);
        updateRoadTile(x + 1, y);
        updateRoadTile(x - 1, y);
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

    public String getFormattedGameTime() {
        int totalSeconds = (int) elapsedGameTime;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = (totalSeconds / 3600) % 24;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public GameMap getMap() {
        return gameMap;
    }

    public float getPlayerBalance() {
        return playerBalance;
    }

    public ArrayList<Vehicle> getUnassignedVehicles() {
        return unassignedVehicles;
    }

    // Adds a newly purchased vehicle to the unassigned vehicles list.
    public void addVehicle(Vehicle vehicle) {
        unassignedVehicles.add(vehicle);
        System.out.println("Model of Vehicle added - " + vehicle.getName() + " , Total unassigned: " + unassignedVehicles.size());
    }

    public void setPlayerBalance(float b) {
        playerBalance = b;
    }
    public void setBalanceChangeListener(BalanceChangeListener listener) {
        this.balanceListener = listener;
    }

    public boolean isBankrupt () {
        if (playerBalance < 0 )
            return true;

        return false;
    }

    public Route createRoute() {
        Route route = new Route();
        routes.add(route);
        System.out.println("Model: Route registered. Total routes: " + routes.size());
        return route;
    }
}
