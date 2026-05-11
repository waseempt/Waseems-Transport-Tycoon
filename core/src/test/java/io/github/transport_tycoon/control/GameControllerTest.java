package io.github.transport_tycoon.control;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.transport_tycoon.model.GameWorld;
import io.github.transport_tycoon.view.GameOverScreen;
import io.github.transport_tycoon.view.WorldRenderer;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameControllerTest {

    @Test
    void testNewGameConstructorCreatesWorldAndRenderer() {
        SpriteBatch batch = mock(SpriteBatch.class);
        TransportTycoon game = mock(TransportTycoon.class);

        try (MockedConstruction<GameWorld> worldMock = mockConstruction(GameWorld.class);
             MockedConstruction<WorldRenderer> rendererMock = mockConstruction(WorldRenderer.class)) {

            GameController controller = new GameController(batch, "Player", game);

            assertNotNull(controller.getWorld());
            assertNotNull(controller.getWorldRenderer());
            assertEquals(1, worldMock.constructed().size());
            assertEquals(1, rendererMock.constructed().size());
        }
    }

    @Test
    void testLoadedGameConstructorRelinksWorld() {
        SpriteBatch batch = mock(SpriteBatch.class);
        TransportTycoon game = mock(TransportTycoon.class);
        GameWorld world = mock(GameWorld.class);

        try (MockedConstruction<WorldRenderer> rendererMock = mockConstruction(WorldRenderer.class)) {
            GameController controller = new GameController(batch, world, game);

            assertSame(world, controller.getWorld());
            assertNotNull(controller.getWorldRenderer());
            verify(world).relinkAfterLoad();
        }
    }

    @Test
    void testRenderUpdatesSimulationAndRendersWorld() {
        SpriteBatch batch = mock(SpriteBatch.class);
        TransportTycoon game = mock(TransportTycoon.class);
        GameWorld world = mock(GameWorld.class);
        when(world.isBankrupt()).thenReturn(false);
        when(world.getTimeScale()).thenReturn(2.0f);

        try (MockedConstruction<WorldRenderer> rendererMock = mockConstruction(WorldRenderer.class)) {
            GameController controller = new GameController(batch, world, game);
            WorldRenderer renderer = rendererMock.constructed().get(0);

            controller.render(0.5f);

            verify(world).updateSimulation(1.0f);
            verify(renderer).renderWorld(world, 0.5f);
        }
    }

    @Test
    void testRenderWithRouteModeUsesRouteRenderer() {
        SpriteBatch batch = mock(SpriteBatch.class);
        TransportTycoon game = mock(TransportTycoon.class);
        GameWorld world = mock(GameWorld.class);
        RouteAssignmentMode routeMode = mock(RouteAssignmentMode.class);
        when(world.isBankrupt()).thenReturn(false);
        when(world.getTimeScale()).thenReturn(1.0f);

        try (MockedConstruction<WorldRenderer> rendererMock = mockConstruction(WorldRenderer.class)) {
            GameController controller = new GameController(batch, world, game);
            WorldRenderer renderer = rendererMock.constructed().get(0);

            controller.render(0.25f, routeMode);

            verify(world).updateSimulation(0.25f);
            verify(renderer).renderWorld(world, 0.25f, routeMode);
        }
    }

    @Test
    void testRenderBankruptSetsGameOverScreen() {
        SpriteBatch batch = mock(SpriteBatch.class);
        TransportTycoon game = mock(TransportTycoon.class);
        GameWorld world = mock(GameWorld.class);
        when(world.isBankrupt()).thenReturn(true);

        try (MockedConstruction<WorldRenderer> rendererMock = mockConstruction(WorldRenderer.class);
             MockedConstruction<GameOverScreen> gameOverMock = mockConstruction(GameOverScreen.class)) {
            GameController controller = new GameController(batch, world, game);

            controller.render(0.1f);

            verify(game).setScreen(any());
            assertEquals(1, gameOverMock.constructed().size());
            verify(world, never()).updateSimulation(anyFloat());
        }
    }
}
