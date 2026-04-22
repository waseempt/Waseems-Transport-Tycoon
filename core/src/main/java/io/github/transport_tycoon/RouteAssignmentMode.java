package io.github.transport_tycoon;

import java.util.ArrayList;

public class RouteAssignmentMode {

    private final Vehicle vehicle;
    private final ArrayList<StopTile> selectedStops = new ArrayList<>();

    public RouteAssignmentMode(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ArrayList<StopTile> getSelectedStops() {
        return selectedStops;
    }

    public boolean toggleStop(StopTile stop) {
        if (selectedStops.contains(stop)) {
            selectedStops.remove(stop);
            return false;
        } else {
            selectedStops.add(stop);
            return true;
        }
    }

    public boolean isSelected(StopTile stop) {
        return selectedStops.contains(stop);
    }

    public boolean canConfirm() {
        return selectedStops.size() >= 2;
    }
}
