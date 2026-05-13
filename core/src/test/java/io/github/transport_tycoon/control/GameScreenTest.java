package io.github.transport_tycoon.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.transport_tycoon.model.GameWorld;
import io.github.transport_tycoon.view.ControlPanel;
import io.github.transport_tycoon.view.HUD;
import io.github.transport_tycoon.view.MinimapRenderer;
import io.github.transport_tycoon.view.PauseMenu;
import io.github.transport_tycoon.view.PurchaseVehicle;
import io.github.transport_tycoon.view.RouteAssignmentOverlay;
import io.github.transport_tycoon.view.TrafficLightUI;
import io.github.transport_tycoon.view.VehicleWindow;
import io.github.transport_tycoon.view.WorldRenderer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameScreenTest {

    private Input originalInput;
    private GL20 originalGl;
    private Input input;
    private GL20 gl;
    private TransportTycoon game;
    private GameWorld world;
    private WorldRenderer renderer;

    @BeforeEach
    void setUp() throws Exception {
        input = mock(Input.class);
        gl = mock(GL20.class);
        setGdxField("input", input);
        setGdxField("gl", gl);

        game = mock(TransportTycoon.class);
        game.batch = mock(SpriteBatch.class);
        world = mock(GameWorld.class);
        renderer = mock(WorldRenderer.class);
        when(renderer.getMainCamera()).thenReturn(new OrthographicCamera());
        when(renderer.getViewport()).thenReturn(mock(Viewport.class));
    }

    @AfterEach
    void tearDown() throws Exception {
        setGdxField("input", originalInput);
        setGdxField("gl", originalGl);
    }

    private void setGdxField(String name, Object value) throws Exception {
        Field field = Gdx.class.getDeclaredField(name);
        field.setAccessible(true);
        if ("input".equals(name) && originalInput == null) {
            originalInput = (Input) field.get(null);
        }
        if ("gl".equals(name) && originalGl == null) {
            originalGl = (GL20) field.get(null);
        }
        field.set(null, value);
    }

    private Stage stage() {
        return mock(Stage.class);
    }

    @Test
    void testNewGameConstructorWiresScreenObjects() {
        try (MockedConstruction<GameController> controllerMock = mockController();
             MockedConstruction<ControlPanel> controlPanelMock = mockConstruction(ControlPanel.class);
             MockedConstruction<HUD> hudMock = mockConstruction(HUD.class);
             MockedConstruction<PauseMenu> pauseMenuMock = mockConstruction(PauseMenu.class);
             MockedConstruction<VehicleWindow> vehicleWindowMock = mockConstruction(VehicleWindow.class);
             MockedConstruction<PurchaseVehicle> purchaseMock = mockConstruction(PurchaseVehicle.class);
             MockedConstruction<RouteAssignmentOverlay> overlayMock = mockConstruction(RouteAssignmentOverlay.class);
             MockedConstruction<MinimapRenderer> minimapMock = mockConstruction(MinimapRenderer.class);
             MockedConstruction<TrafficLightUI> trafficMock = mockConstruction(TrafficLightUI.class);
             MockedConstruction<InputHandler> inputHandlerMock = mockConstruction(InputHandler.class)) {

            GameScreen screen = new GameScreen(game, "Player");

            assertNotNull(screen);
            assertEquals(1, controllerMock.constructed().size());
            assertEquals(1, inputHandlerMock.constructed().size());
            verify(world).setBalanceChangeListener(any());
            verify(input).setInputProcessor(any());
        }
    }

    @Test
    void testLoadedGameConstructorWiresScreenObjects() {
        try (MockedConstruction<GameController> controllerMock = mockController();
             MockedConstruction<ControlPanel> controlPanelMock = mockConstruction(ControlPanel.class);
             MockedConstruction<HUD> hudMock = mockConstruction(HUD.class);
             MockedConstruction<PauseMenu> pauseMenuMock = mockConstruction(PauseMenu.class);
             MockedConstruction<VehicleWindow> vehicleWindowMock = mockConstruction(VehicleWindow.class);
             MockedConstruction<PurchaseVehicle> purchaseMock = mockConstruction(PurchaseVehicle.class);
             MockedConstruction<RouteAssignmentOverlay> overlayMock = mockConstruction(RouteAssignmentOverlay.class);
             MockedConstruction<MinimapRenderer> minimapMock = mockConstruction(MinimapRenderer.class);
             MockedConstruction<TrafficLightUI> trafficMock = mockConstruction(TrafficLightUI.class);
             MockedConstruction<InputHandler> inputHandlerMock = mockConstruction(InputHandler.class)) {

            GameScreen screen = new GameScreen(game, world);

            assertNotNull(screen);
            assertEquals(1, controllerMock.constructed().size());
            verify(world).setBalanceChangeListener(any());
        }
    }

    @Test
    void testShowRefreshesInputProcessor() {
        try (MockedConstruction<GameController> controllerMock = mockController();
             MockedConstruction<ControlPanel> controlPanelMock = mockConstruction(ControlPanel.class, (mock, context) -> when(mock.getStage()).thenReturn(stage()));
             MockedConstruction<HUD> hudMock = mockConstruction(HUD.class, (mock, context) -> when(mock.getStage()).thenReturn(stage()));
             MockedConstruction<PauseMenu> pauseMenuMock = mockConstruction(PauseMenu.class);
             MockedConstruction<VehicleWindow> vehicleWindowMock = mockConstruction(VehicleWindow.class, (mock, context) -> when(mock.getStage()).thenReturn(stage()));
             MockedConstruction<PurchaseVehicle> purchaseMock = mockConstruction(PurchaseVehicle.class, (mock, context) -> when(mock.getStage()).thenReturn(stage()));
             MockedConstruction<RouteAssignmentOverlay> overlayMock = mockConstruction(RouteAssignmentOverlay.class);
             MockedConstruction<MinimapRenderer> minimapMock = mockConstruction(MinimapRenderer.class);
             MockedConstruction<TrafficLightUI> trafficMock = mockConstruction(TrafficLightUI.class, (mock, context) -> when(mock.getStage()).thenReturn(stage()));
             MockedConstruction<InputHandler> inputHandlerMock = mockConstruction(InputHandler.class)) {

            GameScreen screen = new GameScreen(game, "Player");
            screen.show();

            verify(input, atLeast(2)).setInputProcessor(any());
        }
    }

    @Test
    void testRenderUpdatesControllerAndUi() {
        when(world.getPlayerBalance()).thenReturn(1234f);
        when(world.getFormattedGameTime()).thenReturn("01:02:03");

        try (MockedConstruction<GameController> controllerMock = mockController();
             MockedConstruction<ControlPanel> controlPanelMock = mockConstruction(ControlPanel.class);
             MockedConstruction<HUD> hudMock = mockConstruction(HUD.class);
             MockedConstruction<PauseMenu> pauseMenuMock = mockConstruction(PauseMenu.class);
             MockedConstruction<VehicleWindow> vehicleWindowMock = mockConstruction(VehicleWindow.class);
             MockedConstruction<PurchaseVehicle> purchaseMock = mockConstruction(PurchaseVehicle.class);
             MockedConstruction<RouteAssignmentOverlay> overlayMock = mockConstruction(RouteAssignmentOverlay.class);
             MockedConstruction<MinimapRenderer> minimapMock = mockConstruction(MinimapRenderer.class);
             MockedConstruction<TrafficLightUI> trafficMock = mockConstruction(TrafficLightUI.class);
             MockedConstruction<InputHandler> inputHandlerMock = mockConstruction(InputHandler.class)) {

            GameScreen screen = new GameScreen(game, "Player");
            screen.render(0.16f);

            verify(controllerMock.constructed().get(0)).render(eq(0.16f), isNull());
            verify(minimapMock.constructed().get(0)).render(world);
            verify(hudMock.constructed().get(0)).updateBalance(1234f);
            verify(hudMock.constructed().get(0)).updateTime("01:02:03");
        }
    }

    @Test
    void testResizeAndDisposeCallEveryComponent() {
        try (MockedConstruction<GameController> controllerMock = mockController();
             MockedConstruction<ControlPanel> controlPanelMock = mockConstruction(ControlPanel.class);
             MockedConstruction<HUD> hudMock = mockConstruction(HUD.class);
             MockedConstruction<PauseMenu> pauseMenuMock = mockConstruction(PauseMenu.class);
             MockedConstruction<VehicleWindow> vehicleWindowMock = mockConstruction(VehicleWindow.class);
             MockedConstruction<PurchaseVehicle> purchaseMock = mockConstruction(PurchaseVehicle.class);
             MockedConstruction<RouteAssignmentOverlay> overlayMock = mockConstruction(RouteAssignmentOverlay.class);
             MockedConstruction<MinimapRenderer> minimapMock = mockConstruction(MinimapRenderer.class);
             MockedConstruction<TrafficLightUI> trafficMock = mockConstruction(TrafficLightUI.class);
             MockedConstruction<InputHandler> inputHandlerMock = mockConstruction(InputHandler.class)) {

            GameScreen screen = new GameScreen(game, "Player");
            screen.resize(800, 600);
            screen.dispose();

            verify(controlPanelMock.constructed().get(0)).resize(800, 600);
            verify(hudMock.constructed().get(0)).resize(800, 600);
            verify(minimapMock.constructed().get(0)).resize(800, 600);
            verify(controlPanelMock.constructed().get(0)).dispose();
            verify(hudMock.constructed().get(0)).dispose();
            verify(minimapMock.constructed().get(0)).dispose();
            verify(overlayMock.constructed().get(0)).dispose();
        }
    }

    @Test
    void testEmptyLifecycleMethodsDoNotThrow() {
        try (MockedConstruction<GameController> controllerMock = mockController();
             MockedConstruction<ControlPanel> controlPanelMock = mockConstruction(ControlPanel.class);
             MockedConstruction<HUD> hudMock = mockConstruction(HUD.class);
             MockedConstruction<PauseMenu> pauseMenuMock = mockConstruction(PauseMenu.class);
             MockedConstruction<VehicleWindow> vehicleWindowMock = mockConstruction(VehicleWindow.class);
             MockedConstruction<PurchaseVehicle> purchaseMock = mockConstruction(PurchaseVehicle.class);
             MockedConstruction<RouteAssignmentOverlay> overlayMock = mockConstruction(RouteAssignmentOverlay.class);
             MockedConstruction<MinimapRenderer> minimapMock = mockConstruction(MinimapRenderer.class);
             MockedConstruction<TrafficLightUI> trafficMock = mockConstruction(TrafficLightUI.class);
             MockedConstruction<InputHandler> inputHandlerMock = mockConstruction(InputHandler.class)) {

            GameScreen screen = new GameScreen(game, "Player");

            assertDoesNotThrow(screen::pause);
            assertDoesNotThrow(screen::resume);
            assertDoesNotThrow(screen::hide);
        }
    }

    private MockedConstruction<GameController> mockController() {
        return mockConstruction(GameController.class, (mock, context) -> {
            when(mock.getWorld()).thenReturn(world);
            when(mock.getWorldRenderer()).thenReturn(renderer);
        });
    }
}
