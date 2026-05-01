package io.github.transport_tycoon.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FacilityTest {
    private Facility lumberCamp;
    private Facility steelMill;

    @BeforeEach
    void setUp() {
        // A producer only (doesn't consume anything)
        lumberCamp = new Facility("Lumber Camp");
        lumberCamp.setProduces(GoodType.WOOD);

        // A converter (consumes Iron to make Steel)
        steelMill = new Facility("Steel Mill");
        steelMill.setConsumes(GoodType.IRON);
        steelMill.setProduces(GoodType.STEEL);
    }

    @Test
    void testProducerGeneratesOutput() {
        assertEquals(0, lumberCamp.getStoredOutput());

        // Process for 5 seconds (triggers production)
        lumberCamp.processGoods(5.0f);
        assertEquals(1, lumberCamp.getStoredOutput());
    }

    @Test
    void testConverterRequiresInput() {
        steelMill.setStoredInput(0);

        // Process for 5 seconds (should fail because no input)
        steelMill.processGoods(5.0f);
        assertEquals(0, steelMill.getStoredOutput());
    }

    @Test
    void testConverterTransformsInputToOutput() {
        steelMill.setStoredInput(2);

        // Process for 5 seconds
        steelMill.processGoods(5.0f);
        assertEquals(1, steelMill.getStoredInput());
        assertEquals(1, steelMill.getStoredOutput());
    }
}
