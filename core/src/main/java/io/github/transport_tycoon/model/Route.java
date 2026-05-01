package io.github.transport_tycoon.model;

import java.util.ArrayList;


public class Route {

    private ArrayList<StopTile> stops;


    public Route() {
        this.stops = new ArrayList<>();
        System.out.println("Model: New route created.");
    }

    // Adds a stop to the end of this route.

    public void addStop(StopTile stop) {
        stops.add(stop);
        System.out.println("Route: Stop added. Total stops: " + stops.size());
    }


    public ArrayList<StopTile> getStops() {
        return stops;
    }

    public int getStopCount() {
        return stops.size();
    }
}
