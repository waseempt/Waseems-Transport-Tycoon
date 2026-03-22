package io.github.transport_tycoon;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;

public class InputHandler implements InputProcessor {

    private OrthographicCamera camera;

    // mouse position last frame
    private float lastMouseX;
    private float lastMouseY;

    // used to handle dragging to move while building
    private float initialClickX;
    private float initialClickY;
    private boolean wasDragging;

    // For build mode
    private GameWorld world;
    private boolean isBuildMode = false;

    public InputHandler(OrthographicCamera camera, GameWorld world) {
        this.camera = camera;
        this.world = world;
        System.out.println("Control: InputHandler initialized and linked to Camera.");
    }

    public void setBuildMode(boolean active) {
        this.isBuildMode = active;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        lastMouseX = screenX;
        lastMouseY = screenY;

        initialClickX = screenX;
        initialClickY = screenY;
        wasDragging = false;


        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        float deltaX = lastMouseX - screenX;
        float deltaY = screenY - lastMouseY;

        camera.translate(deltaX * camera.zoom, deltaY * camera.zoom);

        lastMouseX = screenX;
        lastMouseY = screenY;

        // Calculates whether the click was an actual drag, allows room for human error
        if (Math.abs(screenX - initialClickX) > 5 ||
            Math.abs(screenY - initialClickY) > 5) {
            wasDragging = true;
        }
        return true;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        // We change the zoom by 10% per physical notch on the scroll wheel
        camera.zoom += amountY * 0.1f;

        // Prevent the player from zooming too far in or out.
        if (camera.zoom < 0.2f) camera.zoom = 0.2f;
        if (camera.zoom > 3.0f) camera.zoom = 3.0f;

        return true;
    }


    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        if (isBuildMode && !wasDragging) {

            // convert pixels to Map Math
            Vector3 worldCoords = new Vector3(screenX, screenY, 0);
            camera.unproject(worldCoords);

            // find the tile's coordinates
            int gridX = (int) (worldCoords.x / 64f);
            int gridY = (int) (worldCoords.y / 64f);

            world.buildRoad(gridX, gridY);

        }

        // Reset the drag flag
        wasDragging = false;
        return true;
    }


    // --- Required Unused Interface Methods
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode){
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }
}
