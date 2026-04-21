package io.github.transport_tycoon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IntersectionTest {

    // Intersection with traffic lights (4-way)
    private Intersection fourWay;

    @BeforeEach
    void setUp() {
        // Create a 4-way intersection (mask = 15)
        fourWay = new Intersection(15);

        // Install traffic lights
        fourWay.installLights();
    }

    @Test
    void testInitialPhaseIsVertical() {

        // Update a very small time step
        // This keeps the initial phase
        fourWay.updateLights(0.1f);

        // Expect vertical direction ("v")
        assertEquals("v", fourWay.getVisualState());
    }

    @Test
    void testClearanceBufferTriggersAllRed() {

        // Move time forward close to phase change
        // Now we are inside the safety buffer (all red)
        fourWay.updateLights(8.6f);

        // Expect no green → all directions red
        assertEquals("none", fourWay.getVisualState());
    }

    @Test
    void testPhaseSwitchesToHorizontal() {

        // Move time enough to switch phase
        fourWay.updateLights(10.1f);

        // Expect horizontal direction ("h")
        assertEquals("h", fourWay.getVisualState());
    }
}
