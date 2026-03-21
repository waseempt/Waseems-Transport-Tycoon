package io.github.transport_tycoon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class GameScreen implements Screen {

    private final TransportTycoon game;
    private GameController controller;
    private InputHandler inputHandler;
    private ControlPanel controlPanel;
    private HUD hud;



    public GameScreen(TransportTycoon game,  String tycoonName) {
        this.game = game;

        // Instantiate the Controller, which instantiates the rest
        this.controller = new GameController(game.batch, tycoonName);

        //fixed size panel at the bottom of the screen
        this.controlPanel = new ControlPanel(game.batch);

        //fixed size panel at the top of the screen
        this.hud = new HUD(game.batch);

        OrthographicCamera camera = controller.getWorldRenderer().getMainCamera();
        this.inputHandler = new InputHandler(camera);

    }

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        controller.render(delta);

        hud.render();


        //draws the UI on top
        controlPanel.render();

    }

    @Override
    public void resize(int width, int height) {
        //Prevent stretching when resizing viewport
        controller.getWorldRenderer().getViewport().update(width, height, false);
        controlPanel.resize(width, height);

        hud.resize(width, height);

    }

    @Override public void show() {
        Gdx.input.setInputProcessor(inputHandler);
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        controlPanel.dispose();
        hud.dispose();

    }

}
