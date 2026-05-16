package io.github.transport_tycoon.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GoodTypeTest {

    @Test
    void testEnumValues() {
        GoodType[] values = GoodType.values();

        assertEquals(5, values.length);

        assertEquals(GoodType.PASSENGERS, values[0]);
        assertEquals(GoodType.WOOD, values[1]);
        assertEquals(GoodType.IRON, values[2]);
        assertEquals(GoodType.STEEL, values[3]);
        assertEquals(GoodType.COAL, values[4]);
    }

    @Test
    void testValueOf() {
        assertEquals(GoodType.PASSENGERS, GoodType.valueOf("PASSENGERS"));
        assertEquals(GoodType.WOOD, GoodType.valueOf("WOOD"));
        assertEquals(GoodType.IRON, GoodType.valueOf("IRON"));
        assertEquals(GoodType.STEEL, GoodType.valueOf("STEEL"));
        assertEquals(GoodType.COAL, GoodType.valueOf("COAL"));
    }
}
