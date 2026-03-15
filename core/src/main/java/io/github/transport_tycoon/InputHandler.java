package io.github.transport_tycoon;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class InputHandler implements InputProcessor {

    private OrthographicCamera camera;

    // mouse position last frame
    private float lastMouseX;
    private float lastMouseY;

    public InputHandler(OrthographicCamera camera) {
        this.camera = camera;
        System.out.println("Control: InputHandler initialized and linked to Camera.");
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        lastMouseX = screenX;
        lastMouseY = screenY;
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        float deltaX = lastMouseX - screenX;
        float deltaY = screenY - lastMouseY;

        camera.translate(deltaX * camera.zoom, deltaY * camera.zoom);

        lastMouseX = screenX;
        lastMouseY = screenY;
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
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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
