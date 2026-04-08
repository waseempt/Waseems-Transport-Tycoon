package io.github.transport_tycoon;

//A truck vehicle that has high capacity and lower speed.

public class Truck extends Vehicle {

    public Truck(String name, GoodType cargoType) {
        super(name, 60, 1.0f, cargoType);
    }
}
