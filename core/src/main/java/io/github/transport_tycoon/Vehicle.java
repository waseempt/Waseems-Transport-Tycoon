package io.github.transport_tycoon;


public abstract class Vehicle {

    private String name;
    private int capacity;
    private float speed;
    private GoodType cargoType;
    private Route assignedRoute;
    private float worldX = 0f;
    private float worldY = 0f;

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
        if (world == null) return;

        maintenanceTimer += delta;

        while (maintenanceTimer >= 5f) {
            maintenanceTimer -= 5f;
            world.setPlayerBalance(world.getPlayerBalance() - maintenanceCost);
        }
    }
}
