package io.github.transport_tycoon.control;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import io.github.transport_tycoon.model.*;
import io.github.transport_tycoon.view.MinimapRenderer;

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
    private boolean isBuildStopMode = false;

    // minimap reference
    private MinimapRenderer minimap;

    // Hoverlistener
    private HoverListener hoverListener;

    public interface IntersectionClickListener {
        void onIntersectionClicked(Intersection intersection);
    }

    private IntersectionClickListener intersectionListener;

    public void setIntersectionListener(IntersectionClickListener listener) {
        this.intersectionListener = listener;
    }

    public interface StopClickListener {
        void onStopClicked(StopTile stop);
    }
    private StopClickListener stopClickListener;
    public void setStopClickListener(StopClickListener l) { this.stopClickListener = l; }

    private boolean isRouteAssignmentMode = false;
    public void setRouteAssignmentMode(boolean active) { this.isRouteAssignmentMode = active; }
    public boolean isRouteAssignmentMode() { return isRouteAssignmentMode; }

    private StopTile findStopAt(int gridX, int gridY) {
        Tile tile = world.getMap().getTile(gridX, gridY);
        if (tile == null) return null;
        for (StopTile stop : world.getStopTiles()) {
            if (stop.getTile() == tile) return stop;
        }
        return null;
    }

    public InputHandler(OrthographicCamera camera, GameWorld world, MinimapRenderer minimap) {
        this.camera = camera;
        this.world = world;
        this.minimap = minimap;
        System.out.println("Control: InputHandler initialized and linked to Camera.");
    }

    public void setBuildStopMode(boolean active) {
        isBuildStopMode = active;
        if (active)
            isBuildMode = false;
    }

    public boolean getBuildStopMode() {
        return isBuildStopMode;
    }

    public void setBuildMode(boolean active) {
        isBuildMode = active;
        if (active)
            isBuildStopMode = false;
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

        // check if click is on minimap
        if (minimap.isInsideMinimap(screenX, screenY)) {
            float[] worldPos = minimap.screenToWorld(screenX, screenY);
            camera.position.set(worldPos[0], worldPos[1], 0);
            camera.update();
            return true;
        }
        if (!isBuildMode && !isBuildStopMode && !wasDragging && button == Input.Buttons.LEFT) {
            Vector3 worldCoords = new Vector3(screenX, screenY, 0);
            camera.unproject(worldCoords);
            int gridX = (int) (worldCoords.x / 64f);
            int gridY = (int) (worldCoords.y / 64f);


            if (isRouteAssignmentMode) {
                StopTile clicked = findStopAt(gridX, gridY);
                System.out.println("Route mode click at grid: " + gridX + ", " + gridY + " | Stop found: " + (clicked != null));
                if (clicked != null && stopClickListener != null) {
                    stopClickListener.onStopClicked(clicked);
                }
                wasDragging = false;
                return true;
            }



            Tile tile = world.getMap().getTile(gridX, gridY);
            if (tile != null && tile.hasIntersection()) {
                if (intersectionListener != null) {
                    intersectionListener.onIntersectionClicked(tile.getIntersection());
                }
                return true;
            }
        }

        // Build road if build mode and not dragging
        if (isBuildMode && !wasDragging) {

            // convert pixels to Map Math
            Vector3 worldCoords = new Vector3(screenX, screenY, 0);
            camera.unproject(worldCoords);

            // find the tile's coordinates
            int gridX = (int) (worldCoords.x / 64f);
            int gridY = (int) (worldCoords.y / 64f);

            // Build if left mouse button
            if (button == Input.Buttons.LEFT) {
                world.buildRoad(gridX, gridY);
            }

            // Remove if right mouse button
            else if (button == Input.Buttons.RIGHT) {
                world.removeRoad(gridX, gridY);
            }

        }
        // Place stop if in stop build mode and not dragging
        if (isBuildStopMode && !wasDragging) {
            Vector3 worldCoords = new Vector3(screenX, screenY, 0);
            camera.unproject(worldCoords);
            int gridX = (int)(worldCoords.x / 64f);
            int gridY = (int)(worldCoords.y / 64f);

            // Build stop if left mouse button
            if (button == Input.Buttons.LEFT) {
                world.tryPlaceStop(gridX, gridY);
            }

            // Remove if right mouse button
            else if (button == Input.Buttons.RIGHT) {
                world.removeStop(gridX,gridY);
            }
        }

        // Reset the drag flag
        wasDragging = false;
        return true;
    }


    public interface HoverListener {
        void onZoneHovered(Zone zone, int screenX, int screenY);
    }


    public void setHoverListener(HoverListener listener) {
        this.hoverListener = listener;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        Vector3 worldCoords = new Vector3(screenX, screenY, 0);
        camera.unproject(worldCoords);

        int gridX = (int) (worldCoords.x / 64f);
        int gridY = (int) (worldCoords.y / 64f);

        Zone hoveredZone = world.getZoneAt(gridX, gridY);

        if (hoverListener != null) {
            hoverListener.onZoneHovered(hoveredZone, screenX, screenY);
        }
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
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }
}
