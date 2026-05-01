package io.github.transport_tycoon.control;

import io.github.transport_tycoon.model.Bus;
import io.github.transport_tycoon.model.StopTile;
import io.github.transport_tycoon.model.Tile;
import io.github.transport_tycoon.model.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RouteAssignmentModeTest {

    private RouteAssignmentMode mode;
    private StopTile stop;

    @BeforeEach
    void setUp() {
        Vehicle bus = new Bus("Test Bus");
        mode = new RouteAssignmentMode(bus);
        stop = new StopTile(new Tile(0, 0), null);
    }

    @Test
    void testToggleStopAddsIfNotPresent() {
        // add stop
        boolean result = mode.toggleStop(stop);

        // should be added
        assertTrue(result);
        assertTrue(mode.isSelected(stop));
    }

    @Test
    void testToggleStopRemovesIfAlreadyPresent() {
        // add first
        mode.toggleStop(stop);

        // click again, remove
        boolean result = mode.toggleStop(stop);

        assertFalse(result);
        assertFalse(mode.isSelected(stop));
    }

    @Test
    void testCanConfirmRequiresTwoStops() {
        // no stops, cannot confirm
        assertFalse(mode.canConfirm());

        // add 1 stop, still no
        mode.toggleStop(new StopTile(new Tile(1, 1), null));
        assertFalse(mode.canConfirm());

        // add 2 stops, now yes
        mode.toggleStop(new StopTile(new Tile(2, 2), null));
        assertTrue(mode.canConfirm());
    }
}
