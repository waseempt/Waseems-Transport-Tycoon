package io.github.transport_tycoon.control;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import io.github.transport_tycoon.model.GameMap;
import io.github.transport_tycoon.model.GameWorld;
import io.github.transport_tycoon.model.Intersection;
import io.github.transport_tycoon.model.StopTile;
import io.github.transport_tycoon.model.Tile;
import io.github.transport_tycoon.model.Zone;
import io.github.transport_tycoon.view.MinimapRenderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InputHandlerTest {

    private OrthographicCamera camera;
    private GameWorld world;
    private GameMap map;
    private MinimapRenderer minimap;
    private InputHandler handler;

    static class TestCamera extends OrthographicCamera {
        @Override
        public void translate(float x, float y) {
        }

        @Override
        public Vector3 unproject(Vector3 vector) {
            return vector;
        }

        @Override
        public void update() {
        }
    }

    @BeforeEach
    void setUp() {
        camera = spy(new TestCamera());
        world = mock(GameWorld.class);
        map = mock(GameMap.class);
        minimap = mock(MinimapRenderer.class);

        when(world.getMap()).thenReturn(map);
        when(minimap.isInsideMinimap(anyInt(), anyInt())).thenReturn(false);

        handler = new InputHandler(camera, world, minimap);
    }

    @Test
    void testBuildModeBuildsAndRemovesRoads() {
        handler.setBuildMode(true);

        handler.touchDown(64, 64, 0, Input.Buttons.LEFT);
        handler.touchUp(64, 64, 0, Input.Buttons.LEFT);

        handler.touchDown(64, 64, 0, Input.Buttons.RIGHT);
        handler.touchUp(64, 64, 0, Input.Buttons.RIGHT);

        verify(world).buildRoad(1, 1);
        verify(world).removeRoad(1, 1);
    }

    @Test
    void testBuildStopModePlacesAndRemovesStops() {
        handler.setBuildStopMode(true);

        handler.touchDown(128, 64, 0, Input.Buttons.LEFT);
        handler.touchUp(128, 64, 0, Input.Buttons.LEFT);

        handler.touchDown(128, 64, 0, Input.Buttons.RIGHT);
        handler.touchUp(128, 64, 0, Input.Buttons.RIGHT);

        verify(world).tryPlaceStop(2, 1);
        verify(world).removeStop(2, 1);
    }

    @Test
    void testBuildModesTurnEachOtherOff() {
        handler.setBuildStopMode(true);
        assertTrue(handler.getBuildStopMode());

        handler.setBuildMode(true);
        assertFalse(handler.getBuildStopMode());

        handler.setBuildStopMode(true);
        handler.touchDown(64, 64, 0, Input.Buttons.LEFT);
        handler.touchUp(64, 64, 0, Input.Buttons.LEFT);

        verify(world, never()).buildRoad(anyInt(), anyInt());
    }

    @Test
    void testDraggingMovesCameraAndPreventsBuild() {
        handler.setBuildMode(true);

        handler.touchDown(100, 100, 0, Input.Buttons.LEFT);
        handler.touchDragged(120, 130, 0);
        handler.touchUp(120, 130, 0, Input.Buttons.LEFT);

        verify(camera).translate(anyFloat(), anyFloat());
        verify(world, never()).buildRoad(anyInt(), anyInt());
    }

    @Test
    void testScrollClampsZoom() {
        camera.zoom = 0.21f;
        handler.scrolled(0, -1);
        assertEquals(0.2f, camera.zoom, 0.001f);

        camera.zoom = 2.95f;
        handler.scrolled(0, 1);
        assertEquals(3.0f, camera.zoom, 0.001f);
    }

    @Test
    void testMinimapClickMovesCamera() {
        when(minimap.isInsideMinimap(10, 20)).thenReturn(true);
        when(minimap.screenToWorld(10, 20)).thenReturn(new float[]{300f, 400f});

        handler.touchDown(10, 20, 0, Input.Buttons.LEFT);
        assertTrue(handler.touchUp(10, 20, 0, Input.Buttons.LEFT));

        assertEquals(300f, camera.position.x, 0.001f);
        assertEquals(400f, camera.position.y, 0.001f);
        verify(camera).update();
    }

    @Test
    void testRouteAssignmentClickNotifiesStopListener() {
        Tile tile = mock(Tile.class);
        StopTile stop = mock(StopTile.class);
        ArrayList<StopTile> stops = new ArrayList<>();
        stops.add(stop);
        when(map.getTile(1, 1)).thenReturn(tile);
        when(stop.getTile()).thenReturn(tile);
        when(world.getStopTiles()).thenReturn(stops);

        InputHandler.StopClickListener listener = mock(InputHandler.StopClickListener.class);
        handler.setStopClickListener(listener);
        handler.setRouteAssignmentMode(true);

        handler.touchDown(64, 64, 0, Input.Buttons.LEFT);
        handler.touchUp(64, 64, 0, Input.Buttons.LEFT);

        verify(listener).onStopClicked(stop);
    }

    @Test
    void testIntersectionClickNotifiesListener() {
        Tile tile = mock(Tile.class);
        Intersection intersection = mock(Intersection.class);
        when(map.getTile(1, 1)).thenReturn(tile);
        when(tile.hasIntersection()).thenReturn(true);
        when(tile.getIntersection()).thenReturn(intersection);

        InputHandler.IntersectionClickListener listener = mock(InputHandler.IntersectionClickListener.class);
        handler.setIntersectionListener(listener);

        handler.touchDown(64, 64, 0, Input.Buttons.LEFT);
        handler.touchUp(64, 64, 0, Input.Buttons.LEFT);

        verify(listener).onIntersectionClicked(intersection);
    }

    @Test
    void testMouseMovedUpdatesHoverListener() {
        Zone zone = mock(Zone.class);
        when(world.getZoneAt(2, 3)).thenReturn(zone);

        InputHandler.HoverListener listener = mock(InputHandler.HoverListener.class);
        handler.setHoverListener(listener);

        assertTrue(handler.mouseMoved(128, 192));
        verify(listener).onZoneHovered(zone, 128, 192);
    }

    @Test
    void testUnusedInputMethodsReturnFalse() {
        assertFalse(handler.keyDown(1));
        assertFalse(handler.keyUp(1));
        assertFalse(handler.keyTyped('a'));
        assertFalse(handler.touchCancelled(0, 0, 0, 0));
    }
}
