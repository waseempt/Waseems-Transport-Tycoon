package io.github.transport_tycoon;

// A bus vehicle that has medium capacity and medium speed.

public class Bus extends Vehicle {

    public Bus(String name) {
        super(name, 40, 1.5f, GoodType.PASSENGERS);
    }
}
