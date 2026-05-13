package io.github.transport_tycoon.view;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.GdxNativesLoader;
import io.github.transport_tycoon.control.RouteAssignmentMode;
import io.github.transport_tycoon.model.GameMap;
import io.github.transport_tycoon.model.GameWorld;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

public class WorldRendererTest {

    @Mock private SpriteBatch mockBatch;
    @Mock private GameWorld mockWorld;
    @Mock private GameMap mockMap;
    @Mock private RouteAssignmentMode mockRouteMode;

    private MockedConstruction<TextureAtlas> mockedAtlas;
    private MockedConstruction<ShapeRenderer> mockedShape;
    private WorldRenderer worldRenderer;

    @BeforeAll
    public static void init() {
        GdxNativesLoader.load();
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        Matrix4 dummyMatrix = new Matrix4();
        when(mockBatch.getTransformMatrix()).thenReturn(dummyMatrix);
        when(mockBatch.getProjectionMatrix()).thenReturn(dummyMatrix);
        when(mockBatch.getColor()).thenReturn(Color.WHITE);

        Gdx.app = mock(Application.class);
        when(Gdx.app.getType()).thenReturn(Application.ApplicationType.HeadlessDesktop);

        Gdx.files = mock(Files.class);
        Gdx.gl = mock(GL20.class);
        Gdx.gl20 = Gdx.gl;
        Gdx.graphics = mock(Graphics.class);

        when(Gdx.graphics.getWidth()).thenReturn(1920);
        when(Gdx.graphics.getHeight()).thenReturn(1080);

        when(Gdx.files.internal(anyString())).thenReturn(mock(FileHandle.class));

        mockedAtlas = mockConstruction(TextureAtlas.class, (atlasMock, context) -> {
            when(atlasMock.findRegion(anyString())).thenReturn(mock(TextureAtlas.AtlasRegion.class));
        });

        mockedShape = mockConstruction(ShapeRenderer.class);

        worldRenderer = new WorldRenderer(mockBatch);
    }

    @AfterEach
    public void tearDown() {
        if (mockedAtlas != null) mockedAtlas.close();
        if (mockedShape != null) mockedShape.close();
    }

    @Test
    public void testInitialization() {
        assertNotNull(worldRenderer.getMainCamera());
        assertNotNull(worldRenderer.getViewport());
    }

    @Test
    public void testRenderWorld() {
        when(mockWorld.getMap()).thenReturn(mockMap);
        when(mockWorld.getFacilities()).thenReturn(new ArrayList<>());
        when(mockWorld.getCities()).thenReturn(new ArrayList<>());
        when(mockWorld.getStopTiles()).thenReturn(new ArrayList<>());
        when(mockWorld.getActiveVehicles()).thenReturn(new ArrayList<>());

        worldRenderer.renderWorld(mockWorld, 0.016f);
    }

    @Test
    public void testRenderWorldWithRouteMode() {
        when(mockWorld.getMap()).thenReturn(mockMap);
        when(mockWorld.getFacilities()).thenReturn(new ArrayList<>());
        when(mockWorld.getCities()).thenReturn(new ArrayList<>());
        when(mockWorld.getStopTiles()).thenReturn(new ArrayList<>());
        when(mockWorld.getActiveVehicles()).thenReturn(new ArrayList<>());

        when(mockRouteMode.getSelectedStops()).thenReturn(new ArrayList<>());

        worldRenderer.renderWorld(mockWorld, 0.016f, mockRouteMode);
    }

    @Test
    public void testDispose() {
        worldRenderer.dispose();
    }
}
