package io.github.transport_tycoon.model;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VehicleTest {

    @Test
    void testBusVehicleBasicProperties() {
        Vehicle vehicle = new Bus("City Bus", 1);
        assertEquals("City Bus", vehicle.getName());
        assertEquals(30, vehicle.getCapacity());
        assertEquals(1.5f, vehicle.getSpeed());
        assertEquals(GoodType.PASSENGERS, vehicle.getCargoType());
    }

    @Test
    void testTruckVehicleBasicProperties() {
        Vehicle vehicle = new Truck("Cargo Truck", 2, GoodType.STEEL);
        assertEquals("Cargo Truck", vehicle.getName());
        assertEquals(80, vehicle.getCapacity());
        assertEquals(0.8f, vehicle.getSpeed());
        assertEquals(GoodType.STEEL, vehicle.getCargoType());
    }

    @Test
    void testSetPosition() {
        Vehicle vehicle = new Bus("Bus", 1);
        vehicle.setPosition(100f, 200f);
        assertEquals(100f, vehicle.getWorldX());
        assertEquals(200f, vehicle.getWorldY());
    }

    @Test
    void testAssignRoute() {
        Vehicle vehicle = new Bus("Bus", 1);
        Route route = new Route();
        vehicle.assignRoute(route);
        assertTrue(vehicle.hasRoute());
        assertEquals(route, vehicle.getAssignedRoute());
    }

    @Test
    void testAssignNullRoute() {
        Vehicle vehicle = new Bus("Bus", 1);
        vehicle.assignRoute(null);
        assertFalse(vehicle.hasRoute());
        assertNull(vehicle.getAssignedRoute());
    }

    @Test
    void testPurchasePrice() {
        Vehicle vehicle = new Truck("Truck", 1, GoodType.WOOD);
        vehicle.setPurchasePrice(500f);
        assertEquals(500f, vehicle.getPurchasePrice());
    }

    @Test
    void testPendingSale() {
        Vehicle vehicle = new Bus("Bus", 1);
        assertFalse(vehicle.isPendingSale());
        vehicle.sellAfterDelivery();
        assertTrue(vehicle.isPendingSale());
    }

    @Test
    void testPendingRetirement() {
        Vehicle vehicle = new Bus("Bus", 1);
        assertFalse(vehicle.isPendingRetirement());
        vehicle.retireAfterDelivery();
        assertTrue(vehicle.isPendingRetirement());
    }

    @Test
    void testSetCurrentTile() {
        Vehicle vehicle = new Bus("Bus", 1);
        Tile tile = new Tile(2, 3);
        vehicle.setCurrentTile(tile);
        assertEquals(tile, vehicle.getCurrentTile());
    }

    @Test
    void testClearPathDoesNotCrash() {
        Vehicle vehicle = new Bus("Bus", 1);
        assertDoesNotThrow(vehicle::clearPath);
    }

    @Test
    void testDefaultConstructorBus() {
        Vehicle vehicle = new Bus();
        assertNull(vehicle.getName());
        assertEquals(0, vehicle.getCapacity());
        assertEquals(0.0f, vehicle.getSpeed());
        assertNull(vehicle.getCargoType());
    }

    @Test
    void testDefaultConstructorTruck() {
        Vehicle vehicle = new Truck();
        assertNull(vehicle.getName());
        assertEquals(0, vehicle.getCapacity());
        assertEquals(0.0f, vehicle.getSpeed());
        assertNull(vehicle.getCargoType());
    }




    @Test
    void testSimpleGettersAndSetters() {
        Vehicle bus = new Bus("Bus", 1);
        GameWorld mockWorld = mock(GameWorld.class);

        bus.setWorld(mockWorld);

        assertEquals(0, bus.getCurrentLoad());
        assertEquals(0f, bus.getRotation());
        assertEquals(1, bus.getModelVariant());
    }

    @Test
    void testLoadCargoFromFacility() {
        Vehicle truck = new Truck("Cargo Truck", 2, GoodType.STEEL);
        Facility mockFacility = mock(Facility.class);

        when(mockFacility.getProduces()).thenReturn(GoodType.STEEL);
        when(mockFacility.getStoredOutput()).thenReturn(50);

        truck.loadCargo(mockFacility);

        assertEquals(50, truck.getCurrentLoad());
        verify(mockFacility).setStoredOutput(0);
    }

    @Test
    void testLoadCargoPassengersFromCity() {
        Vehicle bus = new Bus("City Bus", 1);
        City mockCity = mock(City.class);

        bus.loadCargo(mockCity);

        assertEquals(30, bus.getCurrentLoad());
    }

    @Test
    void testUnloadCargoToFacility() {
        Vehicle truck = new Truck("Cargo Truck", 2, GoodType.STEEL);
        GameWorld mockWorld = mock(GameWorld.class);
        truck.setWorld(mockWorld);

        // First, load from a source facility
        Facility mockSource = mock(Facility.class);
        when(mockSource.getProduces()).thenReturn(GoodType.STEEL);
        when(mockSource.getStoredOutput()).thenReturn(80);
        truck.loadCargo(mockSource);

        // Now, unload to a destination facility
        Facility mockDest = mock(Facility.class);
        when(mockDest.getConsumes()).thenReturn(GoodType.STEEL);
        when(mockDest.getStoredInput()).thenReturn(10);

        truck.unloadCargo(mockDest);

        assertEquals(0, truck.getCurrentLoad());
        verify(mockDest).setStoredInput(90); // 10 existing + 80 unloaded
        verify(mockWorld).calculateDeliveryProfit(mockSource, mockDest, GoodType.STEEL, 80);
    }

    @Test
    void testUnloadCargoToCity() {
        Vehicle truck = new Truck("Cargo Truck", 2, GoodType.STEEL);
        GameWorld mockWorld = mock(GameWorld.class);
        truck.setWorld(mockWorld);

        // Load from a facility first
        Facility mockSource = mock(Facility.class);
        when(mockSource.getProduces()).thenReturn(GoodType.STEEL);
        when(mockSource.getStoredOutput()).thenReturn(40);
        truck.loadCargo(mockSource);

        // Unload at a city
        City mockCity = mock(City.class);
        truck.unloadCargo(mockCity);

        assertEquals(0, truck.getCurrentLoad());
        verify(mockCity).consumeGoods(GoodType.STEEL, 40);
        verify(mockWorld).calculateDeliveryProfit(mockSource, mockCity, GoodType.STEEL, 40);
    }

    @Test
    void testUpdateMaintenanceTimer() {
        Vehicle bus = new Bus("Bus", 1);
        GameWorld mockWorld = mock(GameWorld.class);
        bus.setWorld(mockWorld);

        // Add 35 seconds of delta time to trigger the 30-second maintenance cycle
        bus.update(35f);

        // Default maintenance cost is 100f
        verify(mockWorld).changePlayerBalance(-100f);
    }

    @Test
    void testUpdateTriggersPathfindingAndMovement() {
        Vehicle bus = new Bus("Bus", 1);
        GameWorld mockWorld = mock(GameWorld.class);
        GameMap mockMap = mock(GameMap.class);

        when(mockWorld.getMap()).thenReturn(mockMap);
        bus.setWorld(mockWorld);

        Tile startTile = new Tile(0, 0);
        Tile targetTile = new Tile(1, 0);

        // Tell the mock map exactly where targetTile is so the real PathFinder can find it
        when(mockMap.getTile(1, 0)).thenReturn(targetTile);

        Route mockRoute = mock(Route.class);
        StopTile mockStop = mock(StopTile.class);
        when(mockStop.getTile()).thenReturn(targetTile);

        when(mockRoute.getStops()).thenReturn(new ArrayList<>(Arrays.asList(mockStop)));
        when(mockRoute.getStopCount()).thenReturn(1);

        bus.assignRoute(mockRoute);

        bus.setCurrentTile(startTile);

        // Call update to trigger movement
        bus.update(1f);

        // Verify the vehicle successfully advanced to the next tile
        assertEquals(targetTile, bus.getCurrentTile());
    }

    @Test
    void testStartMovingToNextTileAbortsIfTileOccupied() {
        Vehicle bus = new Bus("Bus", 1);
        Vehicle otherBus = new Bus("Other Bus", 1);

        GameWorld mockWorld = mock(GameWorld.class);
        GameMap mockMap = mock(GameMap.class);

        when(mockWorld.getMap()).thenReturn(mockMap);

        // Simulate another vehicle blocking the grid
        when(mockWorld.getActiveVehicles()).thenReturn(new ArrayList<>(Arrays.asList(bus, otherBus)));
        bus.setWorld(mockWorld);

        Tile startTile = new Tile(0, 0);
        Tile targetTile = new Tile(1, 0);

        // Place the blocker directly on our destination
        otherBus.setCurrentTile(targetTile);

        // Allow the real PathFinder to discover targetTile
        when(mockMap.getTile(1, 0)).thenReturn(targetTile);

        Route mockRoute = mock(Route.class);
        StopTile mockStop = mock(StopTile.class);
        when(mockStop.getTile()).thenReturn(targetTile);

        when(mockRoute.getStops()).thenReturn(new ArrayList<>(Arrays.asList(mockStop)));
        when(mockRoute.getStopCount()).thenReturn(1);

        bus.assignRoute(mockRoute);

        bus.setCurrentTile(startTile);

        bus.update(1f);

        // The bus should NOT have advanced because the tile was occupied
        assertEquals(startTile, bus.getCurrentTile());
    }
}
