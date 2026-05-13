package io.github.transport_tycoon.model;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CityTest {

    @Test
    void testConstructorInitialValues() {
        City city = new City("Budapest");

        assertEquals("Budapest", city.getName());

        Map<GoodType, Integer> demands = city.getDemands();

        assertEquals(50, demands.get(GoodType.PASSENGERS));
        assertEquals(20, demands.get(GoodType.WOOD));
        assertEquals(10, demands.get(GoodType.IRON));
        assertEquals(30, demands.get(GoodType.STEEL));
        assertEquals(40, demands.get(GoodType.COAL));
    }

    @Test
    void testConsumeGoodsNormally() {
        City city = new City("Test");

        city.consumeGoods(GoodType.WOOD, 5);

        assertEquals(15, city.getDemands().get(GoodType.WOOD));
    }

    @Test
    void testConsumeGoodsCannotGoBelowZero() {
        City city = new City("Test");

        city.consumeGoods(GoodType.IRON, 999);

        assertEquals(0, city.getDemands().get(GoodType.IRON));
    }

    @Test
    void testConsumeGoodsWithNullType() {
        City city = new City("Test");

        city.consumeGoods(null, 10);

        assertEquals(50, city.getDemands().get(GoodType.PASSENGERS));
    }

    @Test
    void testConsumeGoodsWithZeroAmount() {
        City city = new City("Test");

        city.consumeGoods(GoodType.STEEL, 0);

        assertEquals(30, city.getDemands().get(GoodType.STEEL));
    }

    @Test
    void testConsumeGoodsWithNegativeAmount() {
        City city = new City("Test");

        city.consumeGoods(GoodType.COAL, -5);

        assertEquals(40, city.getDemands().get(GoodType.COAL));
    }

    @Test
    void testUpdateDemandsBeforeInterval() {
        City city = new City("Test");

        city.updateDemands(5f);

        assertEquals(50, city.getDemands().get(GoodType.PASSENGERS));
        assertEquals(20, city.getDemands().get(GoodType.WOOD));
    }

    @Test
    void testUpdateDemandsAfterInterval() {
        City city = new City("Test");

        city.updateDemands(10f);

        assertEquals(55, city.getDemands().get(GoodType.PASSENGERS));
        assertEquals(22, city.getDemands().get(GoodType.WOOD));
        assertEquals(11, city.getDemands().get(GoodType.IRON));
        assertEquals(32, city.getDemands().get(GoodType.STEEL));
        assertEquals(42, city.getDemands().get(GoodType.COAL));
    }

    @Test
    void testDefaultConstructor() {
        City city = new City();

        assertNull(city.getName());
    }
}
