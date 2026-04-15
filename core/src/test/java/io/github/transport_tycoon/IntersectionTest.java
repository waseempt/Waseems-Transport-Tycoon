package io.github.transport_tycoon;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IntersectionTest {

    @Test
    public void testIntersectionSpawnsWithoutLights() {
        // 1. Arrange: Create a brand new intersection
        Intersection intersection = new Intersection();

        // 2. Assert: It should default to NOT having traffic lights installed
        assertFalse(intersection.hasLights(), "A new intersection should not have traffic lights installed by default.");
    }

    @Test
    public void testInstallLightsActivatesSystem() {
        // 1. Arrange: Create the intersection
        Intersection intersection = new Intersection();

        // 2. Act: The player pays $200 and installs the lights
        intersection.installLights();

        // 3. Assert: The intersection should now flag as having lights
        assertTrue(intersection.hasLights(), "installLights() should set hasTrafficLights to true.");
    }
}
