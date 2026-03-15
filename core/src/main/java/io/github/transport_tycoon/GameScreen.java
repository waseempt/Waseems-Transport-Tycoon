package io.github.transport_tycoon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class GameScreen implements Screen {

    private final TransportTycoon game;
    private GameController controller;
    private InputHandler inputHandler;

    public GameScreen(TransportTycoon game) {
        this.game = game;

        // Instantiate the Controller, which instantiates the rest
        this.controller = new GameController(game.batch);

        OrthographicCamera camera = controller.getWorldRenderer().getMainCamera();
        this.inputHandler = new InputHandler(camera);

        Gdx.input.setInputProcessor(this.inputHandler);
    }

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        controller.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        // Prevent stretching when resizing viewport
        controller.getWorldRenderer().getViewport().update(width, height, false);
    }

    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
