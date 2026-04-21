package io.github.transport_tycoon;


import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class Vehicle {

    private String name;
    private int capacity;
    private float speed;
    private GoodType cargoType;
    private Route assignedRoute;
    private float worldX = 0f;
    private float worldY = 0f;
    private int currentStopIndex = 0;

    private boolean isMoving = false;
    private float movementProgress = 0f; // Goes from 0.0 to 1.0
    private float sourceX = 0f;
    private float sourceY = 0f;
    private float targetX = 0f;
    private float targetY = 0f;

    private int currentLoad = 0;
    private Zone lastLoadedZone = null;
    private float maintenanceTimer = 0f;
    private float maintenanceCost = 10f;
    private Queue<Tile> currentPath = new LinkedList<>();
    private float rotation = 0f;
    private Tile currentTile = null;
    private GameWorld world;

    public Vehicle(String name, int capacity, float speed, GoodType cargoType) {
        this.name = name;
        this.capacity = capacity;
        this.speed = speed;
        this.cargoType = cargoType;
        this.assignedRoute = null;
    }

    public float getWorldX() {
        return worldX;
    }

    public float getWorldY() {
        return worldY;
    }

    public void setPosition(float x, float y) {
        this.worldX = x;
        this.worldY = y;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public float getSpeed() {
        return speed;
    }

    public GoodType getCargoType() {
        return cargoType;
    }

    public Route getAssignedRoute() {
        return assignedRoute;
    }

    public float getRotation() {
        return rotation;
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    public void assignRoute(Route route) {
        this.assignedRoute = route;
        System.out.println("Vehicle: " + name + " assigned to route with " + route.getStopCount() + " stops.");
    }

    public boolean hasRoute() {
        return assignedRoute != null;
    }

    public void setWorld(GameWorld world) {
        this.world = world;
    }

    protected void loadCargo(Zone zone) {
        if (currentLoad < capacity) {
            int amountToLoad = 0;

            if (zone instanceof Facility) {
                Facility f = (Facility) zone;
                if (f.getProduces() == cargoType) {
                    amountToLoad = Math.min(capacity - currentLoad, f.getStoredOutput());
                    f.setStoredOutput(f.getStoredOutput() - amountToLoad);
                }
            } else if (zone instanceof City && cargoType == GoodType.PASSENGERS) {
                // Cities automatically generate passengers to fill the bus
                amountToLoad = capacity - currentLoad;
            }

            if (amountToLoad > 0) {
                currentLoad += amountToLoad;
                lastLoadedZone = zone;
            }
        }
    }

    protected void unloadCargo(Zone destination) {
        if (currentLoad > 0) {
            // Keep track of how much we are dropping off for the calculation
            int amountUnloaded = currentLoad;

            if (destination instanceof City) {
                ((City) destination).consumeGoods(cargoType, amountUnloaded);
            } else if (destination instanceof Facility) {
                Facility f = (Facility) destination;
                if (f.getConsumes() == cargoType) {
                    f.setStoredInput(f.getStoredInput() + amountUnloaded);
                }
            }


            if (lastLoadedZone != null && lastLoadedZone != destination) {
                world.calculateDeliveryProfit(lastLoadedZone, destination, cargoType, amountUnloaded);
            }

            // Empty the truck
            currentLoad = 0;
        }
    }

    public void update(float delta) {
        maintenanceTimer += delta;
        while (maintenanceTimer >= 5f) {
            maintenanceTimer -= 5f;
            world.setPlayerBalance(world.getPlayerBalance() - maintenanceCost);
        }

        if (assignedRoute == null || assignedRoute.getStopCount() == 0) return;

        if (isMoving) {
            // Speed acts as a multiplier. If speed is 1.0, it takes 1 second to cross a tile.
            movementProgress += delta * speed;

            if (movementProgress >= 1.0f) {
                // We reached the destination tile
                movementProgress = 1.0f;
                worldX = targetX;
                worldY = targetY;
                isMoving = false;

                // Check if we arrived at our target stop
                StopTile targetStop = assignedRoute.getStops().get(currentStopIndex);
                if (currentTile == targetStop.getTile()) {
                    Zone zone = targetStop.getLinkedZone();
                    if (zone != null) {
                        unloadCargo(zone);
                        loadCargo(zone);
                    }
                    currentStopIndex = (currentStopIndex + 1) % assignedRoute.getStopCount();
                }
            } else {
                // Interpolate (Glide smoothly between source and target)
                worldX = sourceX + (targetX - sourceX) * movementProgress;
                worldY = sourceY + (targetY - sourceY) * movementProgress;
            }
        } else {
            // We aren't moving, so try to calculate and start the next segment
            startMovingToNextTile();
        }
    }

    private void startMovingToNextTile() {
        StopTile targetStop = assignedRoute.getStops().get(currentStopIndex);
        Tile startTile = (currentTile != null) ? currentTile : assignedRoute.getStops().get(0).getTile();

        if (startTile == targetStop.getTile() && currentPath.isEmpty()) {
            Zone zone = targetStop.getLinkedZone();
            if (zone != null) {
                unloadCargo(zone);
                loadCargo(zone);
            }
            currentStopIndex = (currentStopIndex + 1) % assignedRoute.getStopCount();
            currentTile = startTile;
            return;
        }

        if (currentPath.isEmpty()) {
            List<Tile> path = PathFinder.findPath(world.getMap(), startTile, targetStop.getTile());

            if (path.isEmpty()) {
                System.out.println(getName() + ": No valid route found to target!");
                return;
            }

            if (path.get(0) == startTile) path.remove(0);
            currentPath.addAll(path);
        }

        Tile nextTile = currentPath.peek();
        if (nextTile == null) return;

        // --- PRE-CALCULATE TARGET COORDINATES & LANE OFFSET ---
        float nextTargetX = nextTile.getGridX() * 64f + 32f;
        float nextTargetY = nextTile.getGridY() * 64f + 32f;
        float laneOffset = 14f;

        if (nextTile.getGridX() > currentTile.getGridX()) {
            rotation = 90f;
            nextTargetY -= laneOffset;
        } else if (nextTile.getGridX() < currentTile.getGridX()) {
            rotation = 270f;
            nextTargetY += laneOffset;
        } else if (nextTile.getGridY() > currentTile.getGridY()) {
            rotation = 180f;
            nextTargetX += laneOffset;
        } else if (nextTile.getGridY() < currentTile.getGridY()) {
            rotation = 0f;
            nextTargetX -= laneOffset;
        }

        // --- SAFETY CHECKS ---
        // These happen BEFORE we commit to moving. If a vehicle is blocked, it stays parked perfectly in its current lane.
        if (isTileOccupied(nextTile)) return;
        if (isRedLight(nextTile)) return;

        // --- LOCK IN THE MOVE ---
        currentPath.poll();
        currentTile = nextTile;

        // Save where we are starting from
        sourceX = worldX;
        sourceY = worldY;

        // Save where we are gliding to
        targetX = nextTargetX;
        targetY = nextTargetY;

        // Reset the progress and flip the switch!
        movementProgress = 0f;
        isMoving = true;
    }

    private boolean isTileOccupied(Tile target) {
        for (Vehicle v : world.getActiveVehicles()) {
            if (v != this && v.getCurrentTile() == target) {
                // If the vehicles are facing exactly opposite directions, they are in different lanes
                float diff = Math.abs(v.getRotation() - this.rotation);
                if (diff == 180f) {
                    continue; // Safe to share the tile
                }
                return true; // Blocked (Same direction or intersecting)
            }
        }
        return false;
    }

    private boolean isRedLight(Tile target) {
        if (!target.hasIntersection() || !target.getIntersection().hasLights()) return false;

        String state = target.getIntersection().getVisualState();

        // The Clearance Phase ("none"), vehicles must stop to clear the intersection
        if (state.equals("none")) return true;

        int mask = target.getRoadMask();

        // 4-Way Intersections
        if (mask == 15) {
            // Vertical is green. Vehicles moving East (90) or West (270) must stop.
            if (state.equals("v") && (rotation == 90f || rotation == 270f)) return true;
            // Horizontal is green. Vehicles moving North (180) or South (0) must stop.
            if (state.equals("h") && (rotation == 0f || rotation == 180f)) return true;

            return false; // Light is green for our direction
        }

        // T-Junctions (3-Way Intersections)
        // We need to match the vehicle's direction to how the intersection is drawn on screen.
        // "b" is the bottom stem of the T, "l" is the left arm, and "r" is the right arm.
        String ourApproach = "";

        if (mask == 14) { // The 'T' is standing upright (Stem points South)
            if (rotation == 180f) ourApproach = "b";      // Driving up from the bottom stem
            else if (rotation == 90f) ourApproach = "l";  // Driving in from the left arm
            else if (rotation == 270f) ourApproach = "r"; // Driving in from the right arm

        } else if (mask == 7) { // The 'T' is laying on its right side (Stem points East)
            if (rotation == 270f) ourApproach = "b";      // Driving in from the stem
            else if (rotation == 180f) ourApproach = "l"; // Driving up from the bottom
            else if (rotation == 0f) ourApproach = "r";   // Driving down from the top

        } else if (mask == 11) { // The 'T' is completely upside down (Stem points North)
            if (rotation == 0f) ourApproach = "b";        // Driving down from the top stem
            else if (rotation == 270f) ourApproach = "l"; // Driving in from the right arm
            else if (rotation == 90f) ourApproach = "r";  // Driving in from the left arm

        } else if (mask == 13) { // The 'T' is laying on its left side (Stem points West)
            if (rotation == 90f) ourApproach = "b";       // Driving in from the stem
            else if (rotation == 0f) ourApproach = "l";   // Driving down from the top
            else if (rotation == 180f) ourApproach = "r"; // Driving up from the bottom
        }

        // Does our approach road currently have the green light?
        if (state.equals(ourApproach)) {
            return false; // Light is green, keep driving!
        }

        // If it doesn't match, we have to stop.
        return true;
    }
}
