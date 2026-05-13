package io.github.transport_tycoon.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntersectionTest {

    @Test
    void testFourWayIntersectionConstructor() {
        Intersection intersection = new Intersection(15);

        assertEquals(15, intersection.getRoadMask());
        assertEquals(2, intersection.getPhaseCount());
        assertFalse(intersection.hasLights());
    }

    @Test
    void testThreeWayIntersectionConstructor() {
        Intersection intersection = new Intersection(14);

        assertEquals(14, intersection.getRoadMask());
        assertEquals(3, intersection.getPhaseCount());
    }

    @Test
    void testInstallLights() {
        Intersection intersection = new Intersection(15);

        intersection.installLights();

        assertTrue(intersection.hasLights());
    }

    @Test
    void testSetAndGetPhaseDuration() {
        Intersection intersection = new Intersection(15);

        intersection.setPhaseDuration(0, 20f);

        assertEquals(20f, intersection.getPhaseDuration(0));
    }

    @Test
    void testInvalidPhaseReturnsDefaultValue() {
        Intersection intersection = new Intersection(15);

        assertEquals(10f, intersection.getPhaseDuration(99));
    }

    @Test
    void testUpdateLightsChangesPhase() {
        Intersection intersection = new Intersection(15);

        intersection.installLights();

        intersection.updateLights(11f);
        intersection.updateLights(2f);

        assertEquals("h", intersection.getVisualState());
    }

    @Test
    void testVisualStateWithoutLights() {
        Intersection intersection = new Intersection(15);

        assertEquals("none", intersection.getVisualState());
    }

    @Test
    void testVisualStateDuringClearanceBuffer() {
        Intersection intersection = new Intersection(15);

        intersection.installLights();

        intersection.updateLights(0.5f);

        assertEquals("none", intersection.getVisualState());
    }

    @Test
    void testFourWayVerticalState() {
        Intersection intersection = new Intersection(15);

        intersection.installLights();

        intersection.updateLights(2f);

        assertEquals("v", intersection.getVisualState());
    }

    @Test
    void testThreeWayStates() {
        Intersection intersection = new Intersection(14);

        intersection.installLights();

        intersection.updateLights(2f);
        assertEquals("l", intersection.getVisualState());

        intersection.updateLights(11f);
        intersection.updateLights(2f);
        assertEquals("r", intersection.getVisualState());

        intersection.updateLights(11f);
        intersection.updateLights(2f);
        assertEquals("b", intersection.getVisualState());
    }

    @Test
    void testDefaultConstructor() {
        Intersection intersection = new Intersection();

        assertFalse(intersection.hasLights());
        assertEquals(0, intersection.getRoadMask());
    }
}
