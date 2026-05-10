package io.github.transport_tycoon.view;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.GdxNativesLoader;
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

public class MinimapRendererTest {

    @Mock private GameWorld mockWorld;
    @Mock private GameMap mockMap;

    private MockedConstruction<ShapeRenderer> mockedShapeRenderer;
    private MinimapRenderer minimapRenderer;

    @BeforeAll
    public static void init() {
        GdxNativesLoader.load();
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        Gdx.app = mock(Application.class);
        when(Gdx.app.getType()).thenReturn(Application.ApplicationType.HeadlessDesktop);

        Gdx.files = mock(Files.class);
        Gdx.gl = mock(GL20.class);
        Gdx.gl20 = Gdx.gl;
        Gdx.graphics = mock(Graphics.class);
        Gdx.input = mock(Input.class);

        when(Gdx.graphics.getWidth()).thenReturn(1920);
        when(Gdx.graphics.getHeight()).thenReturn(1080);

        mockedShapeRenderer = mockConstruction(ShapeRenderer.class);

        minimapRenderer = new MinimapRenderer();
    }

    @AfterEach
    public void tearDown() {
        if (mockedShapeRenderer != null) {
            mockedShapeRenderer.close();
        }
    }

    @Test
    public void testRenderWithNullWorld() {
        minimapRenderer.render(null);
    }

    @Test
    public void testRenderWithEmptyWorld() {
        when(mockWorld.getMap()).thenReturn(mockMap);
        when(mockWorld.getFacilities()).thenReturn(new ArrayList<>());
        when(mockWorld.getCities()).thenReturn(new ArrayList<>());

        minimapRenderer.render(mockWorld);
    }

    @Test
    public void testIsInsideMinimap() {
        boolean inside = minimapRenderer.isInsideMinimap(1750, 1000);
        assertTrue(inside);

        boolean outside = minimapRenderer.isInsideMinimap(100, 100);
        assertFalse(outside);
    }

    @Test
    public void testResizeAndDispose() {
        minimapRenderer.resize(1280, 720);
        minimapRenderer.dispose();
    }
}
