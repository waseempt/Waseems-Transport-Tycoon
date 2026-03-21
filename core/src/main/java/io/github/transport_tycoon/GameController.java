package io.github.transport_tycoon;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameController {

    private GameWorld world;
    private WorldRenderer worldRenderer;

    public GameController(SpriteBatch batch) {
        this.world = new GameWorld();
        this.worldRenderer = new WorldRenderer(batch);
        System.out.println("Controller: Architecture linked successfully.");
    }

    // Runs every frame
    public void render(float delta) {
        // Updates sim speed
        float simulationDelta = delta * world.getTimeScale();
        world.updateSimulation(simulationDelta);

        //Logic goes here...

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
