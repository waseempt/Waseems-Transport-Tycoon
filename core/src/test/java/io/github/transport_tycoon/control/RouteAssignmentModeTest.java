package io.github.transport_tycoon.control;

import io.github.transport_tycoon.model.StopTile;
import io.github.transport_tycoon.model.Vehicle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RouteAssignmentModeTest {

    @Test
    void testConstructorProperties() {
        Vehicle vehicle = mock(Vehicle.class);
        RouteAssignmentMode mode = new RouteAssignmentMode(vehicle);

        assertSame(vehicle, mode.getVehicle());
        assertTrue(mode.getSelectedStops().isEmpty());
        assertFalse(mode.canConfirm());
    }

    @Test
    void testToggleStopAddsAndRemovesStop() {
        RouteAssignmentMode mode = new RouteAssignmentMode(mock(Vehicle.class));
        StopTile stop = mock(StopTile.class);

        assertTrue(mode.toggleStop(stop));
        assertTrue(mode.isSelected(stop));
        assertEquals(1, mode.getSelectedStops().size());

        assertFalse(mode.toggleStop(stop));
        assertFalse(mode.isSelected(stop));
        assertTrue(mode.getSelectedStops().isEmpty());
    }

    @Test
    void testMultipleStopsCanConfirm() {
        RouteAssignmentMode mode = new RouteAssignmentMode(mock(Vehicle.class));
        StopTile first = mock(StopTile.class);
        StopTile second = mock(StopTile.class);
        StopTile third = mock(StopTile.class);

        mode.toggleStop(first);
        assertFalse(mode.canConfirm());

        mode.toggleStop(second);
        assertTrue(mode.canConfirm());

        mode.toggleStop(third);
        assertEquals(3, mode.getSelectedStops().size());
        assertTrue(mode.canConfirm());
    }

    @Test
    void testRemovingOneStopKeepsOtherStopsSelected() {
        RouteAssignmentMode mode = new RouteAssignmentMode(mock(Vehicle.class));
        StopTile first = mock(StopTile.class);
        StopTile second = mock(StopTile.class);

        mode.toggleStop(first);
        mode.toggleStop(second);
        mode.toggleStop(first);

        assertFalse(mode.isSelected(first));
        assertTrue(mode.isSelected(second));
        assertFalse(mode.canConfirm());
    }
}
