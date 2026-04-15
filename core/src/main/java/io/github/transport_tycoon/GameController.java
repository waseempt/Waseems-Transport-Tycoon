package io.github.transport_tycoon;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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

    public WorldRenderer getWorldRenderer() {
        return this.worldRenderer;
    }

    public GameWorld getWorld() {
        return this.world;
    }
}
