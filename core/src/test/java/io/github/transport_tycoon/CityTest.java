package io.github.transport_tycoon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CityTest {
    private City city;

    @BeforeEach
    void setUp() {
        city = new City("Budapest");
    }

    @Test
    void testInitialDemands() {
        assertEquals(50, city.getDemands().get(GoodType.PASSENGERS));
        assertEquals(20, city.getDemands().get(GoodType.WOOD));
    }

    @Test
    void testConsumeGoodsReducesDemand() {
        city.consumeGoods(GoodType.WOOD, 5);
        assertEquals(15, city.getDemands().get(GoodType.WOOD));
    }

    @Test
    void testConsumeGoodsDoesNotDropBelowZero() {
        city.consumeGoods(GoodType.IRON, 50); // Initial is 10
        assertEquals(0, city.getDemands().get(GoodType.IRON));
    }

    @Test
    void testConsumeGoodsIgnoresNegativeAmounts() {
        city.consumeGoods(GoodType.STEEL, -10);
        assertEquals(30, city.getDemands().get(GoodType.STEEL)); // Should remain unchanged
    }
}
