package io.github.transport_tycoon.model;


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

    private boolean isTurning = false;
    private float controlX = 0f;
    private float controlY = 0f;
    private float startRotation = 0f;
    private float targetRotation = 0f;

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

    public int getCurrentLoad() {
        return currentLoad;
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
            // Apply realistic deceleration when cornering
            if (isTurning) {
                movementProgress += delta * (speed * 0.55f);
            } else {
                movementProgress += delta * speed;
            }

            if (movementProgress >= 1.0f) {
                movementProgress = 1.0f;
                worldX = targetX;
                worldY = targetY;
                rotation = targetRotation;
                isMoving = false;

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
                if (isTurning) {
                    float t = movementProgress;
                    float u = 1f - t;
                    worldX = (u * u * sourceX) + (2 * u * t * controlX) + (t * t * targetX);
                    worldY = (u * u * sourceY) + (2 * u * t * controlY) + (t * t * targetY);

                    float rotDiff = targetRotation - startRotation;
                    if (rotDiff > 180f) rotDiff -= 360f;
                    if (rotDiff < -180f) rotDiff += 360f;

                    rotation = startRotation + (rotDiff * t);
                    rotation = rotation % 360f;
                    if (rotation < 0) rotation += 360f;

                } else {
                    worldX = sourceX + (targetX - sourceX) * movementProgress;
                    worldY = sourceY + (targetY - sourceY) * movementProgress;
                }
            }
        } else {
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
            if (path.isEmpty()) return;
            if (path.get(0) == startTile) path.remove(0);
            currentPath.addAll(path);
        }

        Tile nextTile = currentPath.peek();
        if (nextTile == null) return;

        float nextTargetX = nextTile.getGridX() * 64f + 32f;
        float nextTargetY = nextTile.getGridY() * 64f + 32f;
        float laneOffset = 14f;

        float moveDirX = nextTile.getGridX() - currentTile.getGridX();
        float moveDirY = nextTile.getGridY() - currentTile.getGridY();

        float newRotation = rotation;
        if (moveDirX > 0) newRotation = 90f;
        else if (moveDirX < 0) newRotation = 270f;
        else if (moveDirY > 0) newRotation = 180f;
        else if (moveDirY < 0) newRotation = 0f;

        boolean isDestination = (nextTile == targetStop.getTile());

        if (!isDestination) {
            // Apply standard right-hand lane offset
            if (moveDirY > 0) nextTargetX += laneOffset;
            else if (moveDirY < 0) nextTargetX -= laneOffset;
            else if (moveDirX > 0) nextTargetY -= laneOffset;
            else if (moveDirX < 0) nextTargetY += laneOffset;

            // Curb lookahead logic
            // We use LinkedList casting to safely peek at the 2nd tile in the queue
            Tile nextNextTile = ((LinkedList<Tile>) currentPath).size() > 1 ? ((LinkedList<Tile>) currentPath).get(1) : null;
            if (nextNextTile != null) {
                float nextDirX = nextNextTile.getGridX() - nextTile.getGridX();
                float nextDirY = nextNextTile.getGridY() - nextTile.getGridY();

                // If the next move changes direction, we are approaching a corner.
                if (moveDirX != nextDirX || moveDirY != nextDirY) {
                    // Pull the target point backwards to the edge of the intersection tile
                    nextTargetX -= moveDirX * 32f;
                    nextTargetY -= moveDirY * 32f;
                }
            }
        }

        if (isTileOccupied(nextTile)) return;
        if (isRedLight(nextTile)) return;

        currentPath.poll();
        currentTile = nextTile;

        sourceX = worldX;
        sourceY = worldY;
        targetX = nextTargetX;
        targetY = nextTargetY;

        startRotation = rotation;
        targetRotation = newRotation;

        if (startRotation != targetRotation || (Math.abs(sourceX - targetX) > 1f && Math.abs(sourceY - targetY) > 1f)) {
            isTurning = true;
            if (startRotation == 0f || startRotation == 180f) {
                controlX = sourceX;
                controlY = targetY;
            } else {
                controlX = targetX;
                controlY = sourceY;
            }
        } else {
            isTurning = false;
        }

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

        // Does our approach road currently have the green light
        if (state.equals(ourApproach)) {
            return false; // Light is green, keep driving
        }

        // If it doesn't match, we have to stop.
        return true;
    }
}
