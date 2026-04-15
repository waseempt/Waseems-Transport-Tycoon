package io.github.transport_tycoon;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IntersectionTest {

    @Test
    void testIntersectionInitialization() {
        // Test a 4-way intersection (mask 15)
        Intersection fourWay = new Intersection(15);
        assertFalse(fourWay.hasLights());
        assertEquals(2, fourWay.getPhaseCount());
        assertEquals(10f, fourWay.getPhaseDuration(0));
        assertEquals("none", fourWay.getVisualState());

        // Test a 3-way intersection (e.g., mask 14)
        Intersection threeWay = new Intersection(14);
        assertFalse(threeWay.hasLights());
        assertEquals(3, threeWay.getPhaseCount());
    }

    @Test
    void testInstallLightsAndConfiguration() {
        Intersection intersection = new Intersection(15);
        intersection.installLights();

        assertTrue(intersection.hasLights());

        intersection.setPhaseDuration(0, 20f);
        intersection.setPhaseDuration(1, 15f);

        assertEquals(20f, intersection.getPhaseDuration(0));
        assertEquals(15f, intersection.getPhaseDuration(1));
    }

    @Test
    void testClearanceBufferVisualState() {
        Intersection intersection = new Intersection(15);
        intersection.installLights();

        // Upon installation, timer is 10.0s.
        // The clearance buffer is 1.5s, meaning anything > 8.5s is in the buffer.
        assertEquals("none", intersection.getVisualState(), "Should be in all-red clearance buffer initially");

        // Simulate 2 seconds passing. Timer drops to 8.0s, clearing the buffer.
        intersection.updateLights(2.0f);
        assertEquals("v", intersection.getVisualState(), "Should display vertical green after buffer clears");
    }

    @Test
    void testFourWayPhaseTransitions() {
        Intersection intersection = new Intersection(15);
        intersection.installLights();

        // Clear initial buffer
        intersection.updateLights(2.0f);
        assertEquals("v", intersection.getVisualState());

        // Drain the remaining 8.0 seconds of Phase 0
        intersection.updateLights(8.0f);

        // Timer hits 0, resets to 10.0s, Phase 1 begins.
        // It should instantly hit the clearance buffer again.
        assertEquals("none", intersection.getVisualState(), "Should be in clearance buffer for Phase 1");

        // Clear buffer for Phase 1
        intersection.updateLights(2.0f);
        assertEquals("h", intersection.getVisualState(), "Should display horizontal green");
    }

    @Test
    void testThreeWayPhaseTransitions() {
        Intersection intersection = new Intersection(7); // 3-way
        intersection.installLights();

        // Phase 0: Left
        intersection.updateLights(2.0f);
        assertEquals("l", intersection.getVisualState());

        // Phase 1: Right
        intersection.updateLights(8.0f); // Drain Phase 0
        assertEquals("none", intersection.getVisualState()); // Buffer
        intersection.updateLights(2.0f); // Clear buffer
        assertEquals("r", intersection.getVisualState());

        // Phase 2: Bottom
        intersection.updateLights(8.0f); // Drain Phase 1
        intersection.updateLights(2.0f); // Clear buffer
        assertEquals("b", intersection.getVisualState());

        // Loop back to Phase 0: Left
        intersection.updateLights(8.0f); // Drain Phase 2
        intersection.updateLights(2.0f); // Clear buffer
        assertEquals("l", intersection.getVisualState(), "Should loop back to left phase");
    }
}
