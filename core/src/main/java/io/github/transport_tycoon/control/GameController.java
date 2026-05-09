package io.github.transport_tycoon.control;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.transport_tycoon.model.GameWorld;
import io.github.transport_tycoon.model.Vehicle;
import io.github.transport_tycoon.view.WorldRenderer;

public class GameController {

    private GameWorld world;
    private WorldRenderer worldRenderer;
    private TransportTycoon game;

    public GameController(SpriteBatch batch, String tycoonName, TransportTycoon game) {
        this.game = game;
        this.world = new GameWorld(tycoonName);
        this.worldRenderer = new WorldRenderer(batch);
        System.out.println("Controller: Architecture linked successfully.");
    }

    public GameController(SpriteBatch batch, GameWorld loadedWorld, TransportTycoon game) {
        this.game = game;
        this.world = loadedWorld;

        this.world.relinkAfterLoad();

        this.worldRenderer = new WorldRenderer(batch);
        System.out.println("Controller: Architecture linked successfully from save file.");
    }

    // Runs every frame
    public void render(float delta) {

        if (world.isBankrupt()) {
            game.setScreen(new GameOverScreen(game));
            return;
        }

        // Updates sim speed
        float simulationDelta = delta * world.getTimeScale();
        world.updateSimulation(simulationDelta);

        // Check for bankruptcy
        if (world.isBankrupt()) {
            System.out.println("BANKRUPT! Game Over.");
        }

        // Render
        worldRenderer.renderWorld(world, delta);
    }

    public void render(float delta, RouteAssignmentMode routeMode) {
        if (world.isBankrupt()) {
            game.setScreen(new GameOverScreen(game));
            return;
        }
        float simulationDelta = delta * world.getTimeScale();
        world.updateSimulation(simulationDelta);
        worldRenderer.renderWorld(world, delta, routeMode);
    }

    public WorldRenderer getWorldRenderer() {
        return this.worldRenderer;
    }

    public GameWorld getWorld() {
        return this.world;
    }
}
