package io.github.transport_tycoon;

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
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

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
        this.stage = new Stage(new ScreenViewport(), batch);
        this.skin = createBasicSkin();
        buildUI();
        System.out.println("View: ControlPanel initialized.");
    }

    private void buildUI() {
        //the layout container
        Table panel = new Table();
        panel.setFillParent(true);
        panel.bottom().left();
        panel.pad(10);
        stage.addActor(panel);

        //the visible dark panel
        Table background = new Table(skin);
        background.setBackground(skin.newDrawable("background", new Color(0.1f, 0.1f, 0.1f, 0.85f)));
        background.pad(12);
        background.defaults().left().padRight(20);

        //where the speed shows
        Label speedLabel = new Label("Speed:", skin);
        background.add(speedLabel);

        TextButton pauseButton = new TextButton("Pause", skin);
        background.add(pauseButton);

        TextButton normalSpeedButton = new TextButton("x1", skin);
        background.add(normalSpeedButton);

        TextButton fastSpeedButton = new TextButton("x2", skin);
        background.add(fastSpeedButton);

        TextButton veryFastSpeedButton = new TextButton("x4", skin);
        background.add(veryFastSpeedButton);

        //shows the build mode
        TextButton buildButton = new TextButton("Build Roads", skin);
        background.add(buildButton);

        // Build Stops button — costs $60 per stop placed
        TextButton buildStopButton = new TextButton("Build Stops", skin);
        buildStopButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (buildStopListener != null) {
                    buildStopListener.onBuildStop();
                }
            }
        });
        background.add(buildStopButton);

        // opens the vehicle list window
        TextButton vehiclesButton = new TextButton("Vehicles", skin);
        vehiclesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (vehicleWindowListener != null) {
                    vehicleWindowListener.onVehicleWindow();
                }
            }
        });
        background.add(vehiclesButton);

        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (speedChangeListener != null) {
                    speedChangeListener.onPauseToggle();
                }
            }
        });

        normalSpeedButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (speedChangeListener != null) {
                    speedChangeListener.onSpeedSelected(1f);
                }
            }
        });

        fastSpeedButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (speedChangeListener != null) {
                    speedChangeListener.onSpeedSelected(2f);
                }
            }
        });

        veryFastSpeedButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (speedChangeListener != null) {
                    speedChangeListener.onSpeedSelected(4f);
                }
            }
        });

        // Build mode ClickListener
        buildButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (buildListener != null) {
                    buildListener.onBuildToggle();
                }
            }
        });

        //the size of the panel
        panel.add(background).width(1100).height(60);
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
        skin.dispose();
    }

    private Skin createBasicSkin() {
        Skin tempSkin = new Skin();
        tempSkin.add("default", new BitmapFont());

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        tempSkin.add("background", new Texture(pixmap));
        pixmap.dispose();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = tempSkin.getFont("default");
        labelStyle.fontColor = Color.WHITE;
        tempSkin.add("default", labelStyle);

        TextButton.TextButtonStyle buttonStyle =
            new TextButton.TextButtonStyle();
        buttonStyle.up = tempSkin.newDrawable("background", Color.DARK_GRAY);
        buttonStyle.down = tempSkin.newDrawable("background", Color.GRAY);
        buttonStyle.over = tempSkin.newDrawable("background", Color.LIGHT_GRAY);
        buttonStyle.font = tempSkin.getFont("default");
        tempSkin.add("default", buttonStyle);

        return tempSkin;
    }
}
