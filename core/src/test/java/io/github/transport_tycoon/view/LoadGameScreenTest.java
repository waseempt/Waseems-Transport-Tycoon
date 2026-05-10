package io.github.transport_tycoon.view;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.GdxNativesLoader;
import io.github.transport_tycoon.control.SaveManager;
import io.github.transport_tycoon.control.TransportTycoon;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoadGameScreenTest {

    @Mock private TransportTycoon mockGame;
    @Mock private SpriteBatch mockBatch;
    @Mock private Skin mockSkin;

    private MockedStatic<SkinManager> mockedSkinManager;
    private MockedStatic<SaveManager> mockedSaveManager;
    private LoadGameScreen loadGameScreen;

    @BeforeAll
    public static void initNatives() {
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
        when(Gdx.graphics.getDeltaTime()).thenReturn(0.016f);

        Matrix4 dummyMatrix = new Matrix4();
        when(mockBatch.getTransformMatrix()).thenReturn(dummyMatrix);
        when(mockBatch.getProjectionMatrix()).thenReturn(dummyMatrix);
        when(mockBatch.getColor()).thenReturn(Color.WHITE);

        mockGame.batch = mockBatch;

        mockedSaveManager = mockStatic(SaveManager.class);
        mockedSaveManager.when(SaveManager::getAvailableSaves).thenReturn(new FileHandle[0]);

        setupMockSkin(mockSkin);
        mockedSkinManager = mockStatic(SkinManager.class);
        mockedSkinManager.when(SkinManager::getSkin).thenReturn(mockSkin);

        loadGameScreen = new LoadGameScreen(mockGame);
    }

    private void setupMockSkin(Skin skin) {
        BitmapFont.BitmapFontData realFontData = new BitmapFont.BitmapFontData();
        realFontData.markupEnabled = false;
        realFontData.lineHeight = 10f; realFontData.capHeight = 10f;
        realFontData.ascent = 10f; realFontData.descent = -10f;
        realFontData.down = -10f; realFontData.scaleX = 1f;
        realFontData.scaleY = 1f; realFontData.blankLineScale = 1f;
        realFontData.xHeight = 10f; realFontData.spaceXadvance = 10f;

        BitmapFont.Glyph spaceGlyph = new BitmapFont.Glyph();
        spaceGlyph.id = ' '; spaceGlyph.xadvance = 10;
        realFontData.setGlyph(' ', spaceGlyph);
        realFontData.missingGlyph = spaceGlyph;

        Texture mockTexture = mock(Texture.class);
        when(mockTexture.getWidth()).thenReturn(100);
        when(mockTexture.getHeight()).thenReturn(100);

        TextureRegion mockRegion = mock(TextureRegion.class);
        when(mockRegion.getTexture()).thenReturn(mockTexture);
        when(mockRegion.getRegionWidth()).thenReturn(10);
        when(mockRegion.getRegionHeight()).thenReturn(10);

        BitmapFont realFont = new BitmapFont(realFontData, mockRegion, false);

        Drawable mockDrawable = mock(Drawable.class);
        when(mockDrawable.getMinWidth()).thenReturn(0f);
        when(mockDrawable.getMinHeight()).thenReturn(0f);
        when(mockDrawable.getLeftWidth()).thenReturn(0f);
        when(mockDrawable.getRightWidth()).thenReturn(0f);
        when(mockDrawable.getTopHeight()).thenReturn(0f);
        when(mockDrawable.getBottomHeight()).thenReturn(0f);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = realFont;
        labelStyle.fontColor = Color.WHITE;
        when(skin.get(Label.LabelStyle.class)).thenReturn(labelStyle);
        when(skin.get(anyString(), eq(Label.LabelStyle.class))).thenReturn(labelStyle);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = realFont;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.up = mockDrawable;
        buttonStyle.down = mockDrawable;
        when(skin.get(TextButton.TextButtonStyle.class)).thenReturn(buttonStyle);
        when(skin.get(anyString(), eq(TextButton.TextButtonStyle.class))).thenReturn(buttonStyle);

        ScrollPane.ScrollPaneStyle scrollStyle = new ScrollPane.ScrollPaneStyle();
        scrollStyle.background = mockDrawable;
        scrollStyle.hScroll = mockDrawable;
        scrollStyle.hScrollKnob = mockDrawable;
        scrollStyle.vScroll = mockDrawable;
        scrollStyle.vScrollKnob = mockDrawable;
        when(skin.get(ScrollPane.ScrollPaneStyle.class)).thenReturn(scrollStyle);
        when(skin.get(anyString(), eq(ScrollPane.ScrollPaneStyle.class))).thenReturn(scrollStyle);

        when(skin.newDrawable(anyString(), any(Color.class))).thenReturn(mockDrawable);
        when(skin.getDrawable(anyString())).thenReturn(mockDrawable);
    }

    @AfterEach
    public void tearDown() {
        mockedSkinManager.close();
        mockedSaveManager.close();
    }

    @Test
    public void testRender() {
        loadGameScreen.render(0.016f);
        verify(Gdx.gl).glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Test
    public void testResizeAndLifecycle() {
        loadGameScreen.resize(1920, 1080);
        loadGameScreen.show();
        loadGameScreen.pause();
        loadGameScreen.resume();
        loadGameScreen.hide();
        loadGameScreen.dispose();
    }
}
