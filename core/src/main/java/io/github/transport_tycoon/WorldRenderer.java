package io.github.transport_tycoon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class WorldRenderer {

    private SpriteBatch batch;
    private OrthographicCamera mainCamera;
    private Viewport viewport;

    private TextureAtlas atlas;

    // Forest Tiles
    private TextureRegion grassRegion;
    private TextureRegion oneTreeRegion;
    private TextureRegion twoTreeRegion;
    private TextureRegion threeTreeRegion;
    private TextureRegion fourTreeRegion;

    // Cities
    private TextureRegion cityRegion;
    private TextureRegion sThreeCityRegion;
    private TextureRegion sFourCityRegion;
    private TextureRegion sFiveCityRegion;

    // Facilities
    private TextureRegion facilityRegion;
    private TextureRegion ironMineRegion;
    private TextureRegion lumberCampRegion;
    private TextureRegion steelMillRegion;
    private TextureRegion coalMineRegion;

    // Road Tiles
    private TextureRegion roadRegion;
    private TextureRegion twoIntersectRegion;
    private TextureRegion threeIntersectRegion;
    private TextureRegion fourIntersectRegion;

    // Vehicles
    private TextureRegion busRegion;
    private TextureRegion truckRegion;

    // Grid Size
    private final float TILE_SIZE = 64f;

    public WorldRenderer(SpriteBatch batch) {
        this.batch = batch;

        this.mainCamera = new OrthographicCamera();
        this.mainCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        this.viewport = new ScreenViewport(this.mainCamera);

        this.mainCamera.position.set(1600f, 1600f, 0);
        this.mainCamera.update();

        this.atlas = new TextureAtlas(Gdx.files.internal("Transport_Tycoon.atlas"));

        // Define forest tiles
        this.grassRegion = atlas.findRegion("grass");
        this.oneTreeRegion = atlas.findRegion("forest1");
        this.twoTreeRegion = atlas.findRegion("forest2");
        this.threeTreeRegion = atlas.findRegion("forest3");
        this.fourTreeRegion = atlas.findRegion("forest4");

        // Define cities
        this.cityRegion = atlas.findRegion("city");
        this.sThreeCityRegion = atlas.findRegion("3x3city");
        this.sFourCityRegion = atlas.findRegion("4x4city");
        this.sFiveCityRegion = atlas.findRegion("5x5city");

        // Define facilities
        this.facilityRegion = atlas.findRegion("facility");
        this.ironMineRegion = atlas.findRegion("iron-mine");
        this.lumberCampRegion = atlas.findRegion("lumber-camp");
        this.steelMillRegion = atlas.findRegion("steel-mill");
        this.coalMineRegion = atlas.findRegion("coal-mine");

        // Define roads
        this.roadRegion = atlas.findRegion("road");
        this.twoIntersectRegion = atlas.findRegion("intersection2");
        this.threeIntersectRegion = atlas.findRegion("intersection3");
        this.fourIntersectRegion = atlas.findRegion("intersection4");

        // Define vehicles
        this.busRegion = atlas.findRegion("bus");
        this.truckRegion = atlas.findRegion("truck");

        System.out.println("View: WorldRenderer initialized with Camera and Assets.");
    }

    public void renderWorld(GameWorld world, float delta) {
        clampCamera();

        batch.setProjectionMatrix(mainCamera.combined);
        batch.begin();

        GameMap map = world.getMap();

        for (int x = 0; x < 50; x++) {
            for (int y = 0; y < 50; y++) {
                float drawX = x * TILE_SIZE;
                float drawY = y * TILE_SIZE;

                Tile tile = map.getTile(x, y);

                if (grassRegion != null) {
                    batch.draw(grassRegion, drawX, drawY, TILE_SIZE, TILE_SIZE);
                }

                if (tile != null && tile.hasForest()) {
                    TextureRegion forestRegion = null;

                    switch (tile.getTreeCount()) {
                        case 1:
                            forestRegion = oneTreeRegion;
                            break;
                        case 2:
                            forestRegion = twoTreeRegion;
                            break;
                        case 3:
                            forestRegion = threeTreeRegion;
                            break;
                        case 4:
                            forestRegion = fourTreeRegion;
                            break;
                    }

                    if (forestRegion != null) {
                        batch.draw(forestRegion, drawX, drawY, TILE_SIZE, TILE_SIZE);
                    }
                }

                if (tile != null && tile.hasRoad()) {
                    if (roadRegion != null) {
                        batch.draw(roadRegion, drawX, drawY, TILE_SIZE, TILE_SIZE);
                    }
                }
            }
        }

        // Draw facilities
        for (Facility facility : world.getFacilities()) {
            Tile anchor = facility.getAnchorTile();
            TextureRegion region = null;

            switch (facility.getFacilityType()) {
                case "Coal Mine":
                    region = coalMineRegion;
                    break;
                case "Iron Mine":
                    region = ironMineRegion;
                    break;
                case "Steel Mill":
                    region = steelMillRegion;
                    break;
                case "Lumber Camp":
                    region = lumberCampRegion;
                    break;
                default:
                    region = coalMineRegion;
                    System.err.println("Could not find facility region");
            }

            if (anchor != null && region != null) {
                float drawX = anchor.getGridX() * TILE_SIZE;
                float drawY = anchor.getGridY() * TILE_SIZE;

                float pixelWidth = facility.getGridWidth() * TILE_SIZE;
                float pixelHeight = facility.getGridHeight() * TILE_SIZE;

                batch.draw(region, drawX, drawY, pixelWidth, pixelHeight);
            }
        }

        // Draw cities
        for (City city : world.getCities()) {
            Tile anchor = city.getAnchorTile();
            TextureRegion region = null;

            switch (city.getGridWidth()) {
                case 3:
                    region = sThreeCityRegion;
                    break;
                case 4:
                    region = sFourCityRegion;
                    break;
                case 5:
                    region = sFiveCityRegion;
                    break;
                default:
                    System.err.println("Could not find city region");
            }

            if (anchor != null && region != null) {
                float drawX = anchor.getGridX() * TILE_SIZE;
                float drawY = anchor.getGridY() * TILE_SIZE;

                float pixelWidth = city.getGridWidth() * TILE_SIZE;
                float pixelHeight = city.getGridHeight() * TILE_SIZE;

                batch.draw(region, drawX, drawY, pixelWidth, pixelHeight);
            }
        }

        batch.end();
    }

    private void clampCamera() {
        float mapWidthPixels = 50 * TILE_SIZE;
        float mapHeightPixels = 50 * TILE_SIZE;

        float cameraHalfWidth = (mainCamera.viewportWidth * mainCamera.zoom) / 2f;
        float cameraHalfHeight = (mainCamera.viewportHeight * mainCamera.zoom) / 2f;

        float minX = cameraHalfWidth;
        float maxX = mapWidthPixels - cameraHalfWidth;

        if (maxX < minX) {
            mainCamera.position.x = mapWidthPixels / 2f;
        } else {
            if (mainCamera.position.x < minX) mainCamera.position.x = minX;
            if (mainCamera.position.x > maxX) mainCamera.position.x = maxX;
        }

        float minY = cameraHalfHeight;
        float maxY = mapHeightPixels - cameraHalfHeight;

        if (maxY < minY) {
            mainCamera.position.y = mapHeightPixels / 2f;
        } else {
            if (mainCamera.position.y < minY) mainCamera.position.y = minY;
            if (mainCamera.position.y > maxY) mainCamera.position.y = maxY;
        }

        mainCamera.update();
    }

    public OrthographicCamera getMainCamera() {
        return this.mainCamera;
    }

    public Viewport getViewport() {
        return this.viewport;
    }

    public void dispose() {
        if (atlas != null)
            atlas.dispose();
    }
}
