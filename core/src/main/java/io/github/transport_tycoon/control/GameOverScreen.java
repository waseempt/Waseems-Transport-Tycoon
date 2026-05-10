package io.github.transport_tycoon.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import io.github.transport_tycoon.view.SkinManager;

public class GameOverScreen implements Screen {

    private final TransportTycoon game;
    private Stage stage;
    private Skin skin;

    public GameOverScreen(TransportTycoon game) {
        this.game = game;
        this.stage = new Stage(new ExtendViewport(1280, 720), game.batch);
        this.skin = SkinManager.getSkin();

        buildUI();
    }

    private void buildUI() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Title Label
        Label titleLabel = new Label("You Lost!", skin, "title");
        titleLabel.setColor(Color.valueOf("DA373C"));
        table.add(titleLabel).padBottom(15).row();

        // Subtitle Label
        Label subtitleLabel = new Label("You went bankrupt.", skin);
        table.add(subtitleLabel).padBottom(50).row();

        // Return to Menu Button
        TextButton menuButton = new TextButton("Return to Menu", skin);
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        table.add(menuButton).width(300).height(50).row();
    }

    @Override
    public void show() {
        // Ensure the stage captures mouse clicks
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // Clear screen to the dark theme background color
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
