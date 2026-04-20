package io.github.transport_tycoon;


public abstract class Vehicle {

    private String name;
    private int capacity;
    private float speed;
    private GoodType cargoType;
    private Route assignedRoute;
    private float worldX = 0f;
    private float worldY = 0f;
    private int currentStopIndex = 0;
    private float movementTimer = 0f;
    private static final float TELEPORT_DELAY = 0.5f;

    private float maintenanceTimer = 0f;
    private float maintenanceCost = 10f;
    private GameWorld world;

    public Vehicle(String name, int capacity, float speed, GoodType cargoType) {
        this.name = name;
        this.capacity = capacity;
        this.speed = speed;
        this.cargoType = cargoType;
        this.assignedRoute = null;
    }

    public float getWorldX() { return worldX; }
    public float getWorldY() { return worldY; }

    public void setPosition(float x, float y) {
        this.worldX = x;
        this.worldY = y;
    }

    public String getName() { return name; }
    public int getCapacity() { return capacity; }
    public float getSpeed() { return speed; }
    public GoodType getCargoType() { return cargoType; }
    public Route getAssignedRoute() { return assignedRoute; }

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

    public void update(float delta) {
        if (world == null || assignedRoute == null || assignedRoute.getStopCount() < 2) return;

        maintenanceTimer += delta;
        while (maintenanceTimer >= 5f) {
            maintenanceTimer -= 5f;
            world.setPlayerBalance(world.getPlayerBalance() - maintenanceCost);
        }

        movementTimer += delta;
        if (movementTimer >= (TELEPORT_DELAY / speed)) {
            movementTimer = 0f;
            performTeleportMovement();
        }
    }

    private void performTeleportMovement() {
        StopTile targetStop = assignedRoute.getStops().get(currentStopIndex);
        float targetX = targetStop.getTile().getGridX() * 64f + 32f;
        float targetY = targetStop.getTile().getGridY() * 64f + 32f;

        // Check if we reached the target stop
        if (Math.abs(worldX - targetX) < 1f && Math.abs(worldY - targetY) < 1f) {
            currentStopIndex = (currentStopIndex + 1) % assignedRoute.getStopCount();
            return;
        }

        // Snap exactly one tile (64 pixels) toward the destination
        int gridX = (int)(worldX / 64f);
        int gridY = (int)(worldY / 64f);
        int destGridX = targetStop.getTile().getGridX();
        int destGridY = targetStop.getTile().getGridY();

        if (gridX < destGridX) worldX += 64f;
        else if (gridX > destGridX) worldX -= 64f;
        else if (gridY < destGridY) worldY += 64f;
        else if (gridY > destGridY) worldY -= 64f;
    }
}
