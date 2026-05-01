package io.github.transport_tycoon.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IntersectionTest {
    private Intersection fourWay;

    @BeforeEach
    void setUp() {
        fourWay = new Intersection(15); // 4-way mask
        fourWay.installLights();
    }

    @Test
    void testClearanceBufferTriggersAllRed() {
        // Timer starts at 10.0f. The first 1.5s (down to 8.5) is the clearance buffer.
        // Fast forward 0.5 seconds. Timer is now 9.5f.
        fourWay.updateLights(0.5f);
        assertEquals("none", fourWay.getVisualState());
    }

    @Test
    void testInitialPhaseIsVertical() {
        // Fast forward past the 1.5s clearance buffer
        fourWay.updateLights(2.0f); // Timer is now 8.0f
        assertEquals("v", fourWay.getVisualState());
    }

    @Test
    void testPhaseSwitchesToHorizontal() {
        // Fast forward exactly 10.0 seconds to finish Phase 0
        fourWay.updateLights(10.0f);

        // We are now at the start of Phase 1.
        // Fast forward 2.0 seconds to get past the new clearance buffer
        fourWay.updateLights(2.0f);

        // Should now be phase 1 ("h")
        assertEquals("h", fourWay.getVisualState());
    }
}
