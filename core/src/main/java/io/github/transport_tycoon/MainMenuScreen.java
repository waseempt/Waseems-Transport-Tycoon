package io.github.transport_tycoon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenuScreen implements Screen {

    private final TransportTycoon game;
    private Stage stage;
    private Skin skin;

    public MainMenuScreen(TransportTycoon game) {
        this.game = game;

        // Container for menu elements
        this.stage = new Stage(new ScreenViewport(), game.batch);

        // Routes mouse clicks to the menu stage to handle
        Gdx.input.setInputProcessor(stage);

        // Generate a basic temporary skin for buttons and text
        this.skin = createBasicSkin();

        buildUI();
    }

    private void buildUI() {
        // A Table automatically handles layout and centering
        Table table = new Table();
        table.setFillParent(true); // Table fills the whole screen, holds all elements
        stage.addActor(table);

        // Title Label
        Label titleLabel = new Label("Transport Tycoon", skin);
        titleLabel.setFontScale(2.0f); // Make the title bigger

        // Buttons
        TextButton newGameButton = new TextButton("New Game", skin);
        TextButton loadGameButton = new TextButton("Load Game", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        // Listeners
        newGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SetupScreen(game));
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit(); // Close the application
            }
        });

        // Add everything to the table
        table.add(titleLabel).padBottom(50).row();
        table.add(newGameButton).width(200).height(50).padBottom(20).row();
        table.add(loadGameButton).width(200).height(50).padBottom(20).row();
        table.add(exitButton).width(200).height(50).row();
    }

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update stage and draw table
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    // --- Temporary Helper to generate UI styles without needing asset files ---
    private Skin createBasicSkin() {
        Skin tempSkin = new Skin();
        tempSkin.add("default", new BitmapFont()); // Default LibGDX font

        // Create a 1x1 pixel grey texture for the button backgrounds
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.LIGHT_GRAY);
        pixmap.fill();
        tempSkin.add("background", new Texture(pixmap));

        // Create the Button Style
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = tempSkin.newDrawable("background", Color.DARK_GRAY);
        buttonStyle.down = tempSkin.newDrawable("background", Color.GRAY);
        buttonStyle.over = tempSkin.newDrawable("background", Color.LIGHT_GRAY);
        buttonStyle.font = tempSkin.getFont("default");
        tempSkin.add("default", buttonStyle);

        // Create the Label Style
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = tempSkin.getFont("default");
        tempSkin.add("default", labelStyle);

        return tempSkin;
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    // Unused Screen methods
    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
