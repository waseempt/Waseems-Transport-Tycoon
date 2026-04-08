package io.github.transport_tycoon;


public abstract class Vehicle {

    private String name;
    private int capacity;
    private float speed;
    private GoodType cargoType;
    private Route assignedRoute;

    public Vehicle(String name, int capacity, float speed, GoodType cargoType) {
        this.name = name;
        this.capacity = capacity;
        this.speed = speed;
        this.cargoType = cargoType;
        this.assignedRoute = null;
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
}
