package io.github.transport_tycoon.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class ControlPanel {
    //speed 3
    private SpeedChangeListener speedChangeListener;
    public void setSpeedChangeListener(SpeedChangeListener listener) {
        this.speedChangeListener = listener;
    }
    public interface SpeedChangeListener {
        void onPauseToggle();
        void onSpeedSelected(float speed);
    }
    private Stage stage;
    private Skin skin;

    private BuildModeListener buildListener;
    private VehicleWindowListener vehicleWindowListener;


    public ControlPanel(SpriteBatch batch) {
        this.stage = new Stage(new ExtendViewport(1920, 1080), batch);
        this.skin = SkinManager.getSkin();
        buildUI();
        System.out.println("View: ControlPanel initialized.");
    }

    private void buildUI() {
        // The layout container
        Table panel = new Table();
        panel.setFillParent(true);
        panel.bottom().left();
        panel.pad(10);
        stage.addActor(panel);

        // The visible dark panel
        Table background = new Table(skin);
        background.setBackground(skin.newDrawable("background", new Color(0.1f, 0.1f, 0.1f, 0.85f)));
        background.pad(12, 25, 12, 25); // Added some side padding so buttons don't hit the very edge

        // Speed on the left
        Table speedTable = new Table();
        speedTable.defaults().height(35).padRight(10);

        Label speedLabel = new Label("Speed:", skin);
        TextButton pauseButton = new TextButton("Pause", skin);
        TextButton normalSpeedButton = new TextButton("x1", skin);
        TextButton fastSpeedButton = new TextButton("x2", skin);
        TextButton veryFastSpeedButton = new TextButton("x4", skin);

        speedTable.add(speedLabel);
        speedTable.add(pauseButton);
        speedTable.add(normalSpeedButton);
        speedTable.add(fastSpeedButton);
        speedTable.add(veryFastSpeedButton).padRight(0); // No padding on the last item

        // Controls centered
        Table buildTable = new Table();
        buildTable.defaults().height(35).padRight(10);

        Label buildLabel = new Label("Build:", skin);
        TextButton buildButton = new TextButton("Build Roads", skin);
        TextButton buildStopButton = new TextButton("Build Stops", skin);

        buildTable.add(buildLabel);
        buildTable.add(buildButton);
        buildTable.add(buildStopButton).padRight(0); // Removed the old 50px hack

        // Vehicles on the right
        Table vehiclesTable = new Table();
        TextButton vehiclesButton = new TextButton("Vehicles", skin);
        vehiclesTable.add(vehiclesButton).height(35);


        // Listeners
        buildStopButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (buildStopListener != null) buildStopListener.onBuildStop();
            }
        });

        vehiclesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (vehicleWindowListener != null) vehicleWindowListener.onVehicleWindow();
            }
        });

        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (speedChangeListener != null) speedChangeListener.onPauseToggle();
            }
        });

        normalSpeedButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (speedChangeListener != null) speedChangeListener.onSpeedSelected(1f);
            }
        });

        fastSpeedButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (speedChangeListener != null) speedChangeListener.onSpeedSelected(2f);
            }
        });

        veryFastSpeedButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (speedChangeListener != null) speedChangeListener.onSpeedSelected(4f);
            }
        });

        buildButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (buildListener != null) buildListener.onBuildToggle();
            }
        });

        // Add groups to the panel
        background.add(speedTable).expandX().left();
        background.add(buildTable).expandX().center();
        background.add(vehiclesTable).expandX().right();


        panel.add(background).width(1200).height(60);
    }

    //updates all the UI logic
    public void render() {
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    // to make sure that the size doesnt change as we zoom-in or out
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public Stage getStage() {
        return stage;
    }

    public interface BuildModeListener {
        void onBuildToggle();
    }

    public void setBuildListener(BuildModeListener listener) {
        this.buildListener = listener;
    }

    // Callback triggered when the Build Stops button is clicked
    public interface BuildStopListener {
        void onBuildStop();
    }

    private BuildStopListener buildStopListener;

    public void setBuildStopListener(BuildStopListener listener) {
        this.buildStopListener = listener;
    }

    // Callback triggered when the Vehicles button is clicked
    public interface VehicleWindowListener {
        void onVehicleWindow();
    }

    public void setVehicleWindowListener(VehicleWindowListener listener) {
        this.vehicleWindowListener = listener;
    }

    public void dispose() {
        stage.dispose();
    }
}
