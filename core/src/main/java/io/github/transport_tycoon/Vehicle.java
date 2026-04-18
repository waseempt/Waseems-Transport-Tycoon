package io.github.transport_tycoon;


public abstract class Vehicle {

    private String name;
    private int capacity;
    private float speed;
    private GoodType cargoType;
    private Route assignedRoute;

    private float maintenanceTimer = 0f;
    private float maintenanceCost = 10f;
    private GameWorld world;

    public Vehicle(String name, int capacity, float speed, GoodType cargoType, GameWorld world) {
        this.name = name;
        this.capacity = capacity;
        this.speed = speed;
        this.cargoType = cargoType;
        this.assignedRoute = null;
        this.world = world;
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

    public void update(float delta) {
        maintenanceTimer += delta;

        while (maintenanceTimer >= 5f) {
            maintenanceTimer -= 5f;
            world.setPlayerBalance(world.getPlayerBalance() - maintenanceCost);
        }
    }
}
