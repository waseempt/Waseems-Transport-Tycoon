package io.github.transport_tycoon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MinimapRenderer {

    private static final float MINI_TILE_SIZE = 4f;
    private static final float PADDING = 20f;
    private static final int MAP_SIZE = 50;

    private final OrthographicCamera camera;
    private final ScreenViewport viewport;
    private final ShapeRenderer shapeRenderer;

    public MinimapRenderer() {
        this.camera = new OrthographicCamera();
        this.viewport = new ScreenViewport(camera);
        this.shapeRenderer = new ShapeRenderer();

        this.viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    }

    public void render(GameWorld world) {
        if (world == null || world.getMap() == null) return;

        GameMap map = world.getMap();

        float minimapWidth = MAP_SIZE * MINI_TILE_SIZE;
        float minimapHeight = MAP_SIZE * MINI_TILE_SIZE;

        float startX = viewport.getWorldWidth() - minimapWidth - PADDING;
        float startY = PADDING;

        viewport.apply();
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (int x = 0; x < MAP_SIZE; x++) {
            for (int y = 0; y < MAP_SIZE; y++) {

                Tile tile = map.getTile(x, y);
                if (tile == null) continue;

                float drawX = startX + x * MINI_TILE_SIZE;
                float drawY = startY + y * MINI_TILE_SIZE;

                if (tile.hasForest()) {
                    switch (tile.getTreeCount()) {
                        case 1: shapeRenderer.setColor(0.15f, 0.55f, 0.15f, 1f); break;
                        case 2: shapeRenderer.setColor(0.12f, 0.50f, 0.12f, 1f); break;
                        case 3: shapeRenderer.setColor(0.10f, 0.42f, 0.10f, 1f); break;
                        default: shapeRenderer.setColor(0.08f, 0.32f, 0.08f, 1f); break;
                    }
                } else {
                    shapeRenderer.setColor(0.35f, 0.70f, 0.35f, 1f);
                }

                shapeRenderer.rect(drawX, drawY, MINI_TILE_SIZE, MINI_TILE_SIZE);
            }
        }

        shapeRenderer.setColor(Color.GRAY);
        for (Facility facility : world.getFacilities()) {
            for (Tile tile : facility.getTiles()) {
                float drawX = startX + tile.getGridX() * MINI_TILE_SIZE;
                float drawY = startY + tile.getGridY() * MINI_TILE_SIZE;
                shapeRenderer.rect(drawX, drawY, MINI_TILE_SIZE, MINI_TILE_SIZE);
            }
        }

        shapeRenderer.setColor(Color.GOLD);
        for (City city : world.getCities()) {
            for (Tile tile : city.getTiles()) {
                float drawX = startX + tile.getGridX() * MINI_TILE_SIZE;
                float drawY = startY + tile.getGridY() * MINI_TILE_SIZE;
                shapeRenderer.rect(drawX, drawY, MINI_TILE_SIZE, MINI_TILE_SIZE);
            }
        }

        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(startX, startY, minimapWidth, minimapHeight);
        shapeRenderer.end();
    }

    // checks if click is inside minimap
    public boolean isInsideMinimap(int screenX, int screenY) {
        float minimapWidth = MAP_SIZE * MINI_TILE_SIZE;
        float minimapHeight = MAP_SIZE * MINI_TILE_SIZE;

        float startX = viewport.getWorldWidth() - minimapWidth - PADDING;
        float startY = PADDING;

        float fixedY = Gdx.graphics.getHeight() - screenY;

        return screenX >= startX && screenX <= startX + minimapWidth &&
            fixedY >= startY && fixedY <= startY + minimapHeight;
    }

    // converts minimap click to world position
    public float[] screenToWorld(int screenX, int screenY) {
        float minimapWidth = MAP_SIZE * MINI_TILE_SIZE;
        float minimapHeight = MAP_SIZE * MINI_TILE_SIZE;

        float startX = viewport.getWorldWidth() - minimapWidth - PADDING;
        float startY = PADDING;

        float fixedY = Gdx.graphics.getHeight() - screenY;

        float localX = screenX - startX;
        float localY = fixedY - startY;

        float worldX = (localX / minimapWidth) * (MAP_SIZE * 64f);
        float worldY = (localY / minimapHeight) * (MAP_SIZE * 64f);

        return new float[]{worldX, worldY};
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
