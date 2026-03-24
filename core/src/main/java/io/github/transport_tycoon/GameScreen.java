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
    private PauseMenu pauseMenu;

    private MinimapRenderer minimapRenderer;

    private boolean isBuildMode = false;

    public GameScreen(TransportTycoon game,  String tycoonName) {
        this.game = game;

        // Instantiate the Controller, which instantiates the rest
        this.controller = new GameController(game.batch, tycoonName);

        //fixed size panel at the bottom of the screen
        this.controlPanel = new ControlPanel(game.batch);

        //fixed size panel at the top of the screen
        this.hud = new HUD(game.batch);

        this.pauseMenu = new PauseMenu(game.batch);
        //stops the simulation and shows the pause menu when game paused
        hud.setPauseListener(() -> {
            controller.getWorld().pause();
            pauseMenu.show();
        });

        //restores simulation speed and hides the pause menu when resumed
        pauseMenu.setResumeListener(() -> {
            controller.getWorld().resume();
            pauseMenu.hide(inputHandler, hud.getStage(), controlPanel.getStage());
        });

        //routes the player back to the main menu when exiting
        pauseMenu.setExitListener(() -> {
            game.setScreen(new MainMenuScreen(game));
        });

        //Build mode listener
        this.controlPanel.setBuildListener(() -> {
            isBuildMode = !isBuildMode;
            hud.setBuildModeActive(isBuildMode);
            inputHandler.setBuildMode(isBuildMode);
        });

        // When controller tells the screen balance was changed, balance tells the hud to activate the animation
        controller.getWorld().setBalanceChangeListener(amount -> {
            hud.showBalanceChange(amount);
        });

        // Minimap now belongs to GameScreen
        this.minimapRenderer = new MinimapRenderer();

        OrthographicCamera camera = controller.getWorldRenderer().getMainCamera();

        // pass minimap to input handler
        this.inputHandler = new InputHandler(camera, controller.getWorld(), minimapRenderer);

        Gdx.input.setInputProcessor(this.inputHandler);
    }

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        controller.render(delta);

        // Render minimap on top of world
        minimapRenderer.render(controller.getWorld());

        // Update HUD values
        float currentBalance = controller.getWorld().getPlayerBalance();

        hud.updateBalance(currentBalance);
        hud.render();

        // draws the UI on top
        controlPanel.render();

        pauseMenu.render();

    }

    @Override
    public void resize(int width, int height) {
        // Prevent stretching when resizing viewport
        controller.getWorldRenderer().getViewport().update(width, height, false);
        controlPanel.resize(width, height);

        hud.resize(width, height);

        minimapRenderer.resize(width, height);

        pauseMenu.resize(width, height);

    }

    //sets up InputMultiplexer  so teh HUD stage receives button clicks
    @Override
    public void show() {
        com.badlogic.gdx.InputMultiplexer multiplexer = new com.badlogic.gdx.InputMultiplexer();
        multiplexer.addProcessor(hud.getStage());
        multiplexer.addProcessor(controlPanel.getStage());
        multiplexer.addProcessor(inputHandler);
        Gdx.input.setInputProcessor(multiplexer);
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        controlPanel.dispose();
        hud.dispose();
        minimapRenderer.dispose();
        pauseMenu.dispose();
    }
}
