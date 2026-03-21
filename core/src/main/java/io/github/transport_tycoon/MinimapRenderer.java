package io.github.transport_tycoon;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MinimapRenderer {

    private final float MINI_TILE_SIZE = 4f;

    public void render(GameWorld world, SpriteBatch batch) {

        GameMap map = world.getMap();

        for (int x = 0; x < 50; x++) {
            for (int y = 0; y < 50; y++) {

                float drawX = x * MINI_TILE_SIZE + 20;
                float drawY = y * MINI_TILE_SIZE + 20;

                //  (commit 2 کاملش می‌کنیم)
            }
        }
    }
}
