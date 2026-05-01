package io.github.transport_tycoon.control;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TransportTycoon extends Game {

    // The SpriteBatch
    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // Launches the user into main menu first
        this.setScreen(new MainMenuScreen(this));
    }

    @Override
    public void dispose() {
        // clear SpriteBatch when game is closed
        batch.dispose();
        super.dispose();
    }
}
