package io.github.transport_tycoon.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.transport_tycoon.model.City;
import io.github.transport_tycoon.model.Facility;
import io.github.transport_tycoon.model.GoodType;
import io.github.transport_tycoon.model.Zone;

import java.util.Map;

public class HUD {
    //a place where all the HUD widgets are held/placed
    private Stage stage;
    //fonts, colors, etc.
    private Skin skin;

    private PauseListener pauseListener;

    //Labels
    private Label balanceLabel;
    private Label timeLabel;
    private Label buildModeIndicator;
    private Label stopBuildModeIndicator;


    private Table tooltipTable;

    public HUD(SpriteBatch batch) {
        this.stage = new Stage(new ScreenViewport(), batch);
        this.skin = createBasicSkin();
        buildUI();
        System.out.println("View: HUD initialized.");
    }

    private void buildUI() {
        //the bar that fills the screen and places it at the top
        Table panel = new Table();
        panel.setFillParent(true);
        panel.top();
        stage.addActor(panel);

        //dark background bar
        Table background = new Table(skin);
        background.setBackground(skin.newDrawable("background", new Color(0.1f, 0.1f, 0.1f, 0.85f)));
        background.pad(8);

        //shows the Balance on the left
        this.balanceLabel = new Label("Balance: [placeholder]", skin);

        //shows the time in the middle
        this.timeLabel = new Label("Time: [placeholder]", skin);

        //build mode indicator
        this.buildModeIndicator = new Label("", skin);
        this.buildModeIndicator.setColor(Color.YELLOW);

        //stop build mode indicator — shown when the player is placing a stop
        this.stopBuildModeIndicator = new Label("", skin);
        this.stopBuildModeIndicator.setColor(Color.CYAN);

        //shows the pause button on the right
        TextButton pauseButton = new TextButton("Pause Game", skin);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (pauseListener != null) {
                    pauseListener.onPause();
                }
            }
        });

        //Build tooltip table
        tooltipTable = new Table(skin);
        tooltipTable.setBackground(skin.newDrawable("background", new Color(0.1f, 0.1f, 0.1f, 0.9f)));
        tooltipTable.pad(15);
        tooltipTable.setVisible(false);
        stage.addActor(tooltipTable);

        //where every label is placed
        background.add(balanceLabel).expandX().left();
        background.add(timeLabel).expandX().center();
        background.add(buildModeIndicator).expandX().center();
        background.add(stopBuildModeIndicator).expandX().center();
        background.add(pauseButton).expandX().right();

        //placeholder labels
        panel.add(background).growX().height(40);
    }

    public void updateBalance(float balanceAmount) {
        // Updates the text on the screen
        this.balanceLabel.setText("Balance: $" + balanceAmount);
    }

    public void updateTime(String timeText) {
        this.timeLabel.setText("Time: " + timeText);
    }

    public void showBalanceChange(float amount) {
        // Format text and color
        String text = amount > 0 ? "+$" + (int)amount : "-$" + (int)Math.abs(amount);
        Color color = amount > 0 ? Color.GREEN : Color.RED;

        Label changeLabel = new Label(text, skin);
        changeLabel.setColor(color);

        // Find exactly where the main balance label is on the screen
        Vector2 pos = balanceLabel.localToStageCoordinates(new Vector2(0, 0));

        // Offset it slightly to the right of the main balance
        changeLabel.setPosition(pos.x + 100, pos.y);

        // Create the Floating Animation (Move Up + Fade Out over 1.5 seconds)
        changeLabel.addAction(Actions.sequence(
            Actions.parallel(
                Actions.moveBy(0, 30, 1.5f),
                Actions.fadeOut(1.5f)
            ),
            Actions.removeActor() // Destroys the label so it doesn't lag the game!
        ));

        stage.addActor(changeLabel);
    }

    //Shows build mode message when active
    public void setBuildModeActive(boolean active) {
        if (active) {
            this.buildModeIndicator.setText("BUILD MODE ACTIVE - Left Click: Build - Right Click: Remove");
        } else {
            this.buildModeIndicator.setText("");
        }
    }

    // Shows stop build mode message when the player is about to place a stop
    public void setStopBuildModeActive(boolean active) {
        if (active) {
            this.stopBuildModeIndicator.setText("STOP BUILD MODE ACTIVE - Left Click: Build - Right Click: Remove");
        } else {
            this.stopBuildModeIndicator.setText("");
        }
    }

    public void updateTooltip(Zone zone, float screenX, float screenY) {
        // Clear the table completely so we can stack new labels into it
        tooltipTable.clear();

        if (zone == null) {
            tooltipTable.setVisible(false);
            return;
        }

        if (zone instanceof City) {
            City c = (City) zone;

            // Title Label
            Label title = new Label(c.getName() + " (City)", skin);
            title.setFontScale(1.3f);
            title.setColor(Color.GOLD);
            tooltipTable.add(title).left().padBottom(5).row();

            // Size Label
            Label size = new Label("Size: " + c.getGridWidth() + "x" + c.getGridHeight(), skin);
            size.setColor(Color.LIGHT_GRAY);
            tooltipTable.add(size).left().padBottom(10).row();

            // Passengers
            int waitingPassengers = c.getDemands().getOrDefault(GoodType.PASSENGERS, 0);
            tooltipTable.add(new Label("Produces (Waiting):", skin)).left().row();

            Label passLabel = new Label("  ↳ Passengers: " + waitingPassengers, skin);
            passLabel.setColor(Color.LIGHT_GRAY);
            tooltipTable.add(passLabel).left().padBottom(5).row();

            // Demands Header
            tooltipTable.add(new Label("Demands:", skin)).left().row();

            // Demands List
            for (Map.Entry<GoodType, Integer> entry : c.getDemands().entrySet()) {
                // Skip passengers since we already displayed them above
                if (entry.getKey() == GoodType.PASSENGERS) {
                    continue;
                }

                Label demandLabel = new Label("  ↳ " + entry.getKey() + ": " + entry.getValue(), skin);
                demandLabel.setColor(Color.LIGHT_GRAY);
                tooltipTable.add(demandLabel).left().row();
            }

        } else if (zone instanceof Facility) {
            Facility f = (Facility) zone;

            // Title Label
            Label title = new Label(f.getFacilityType() + " (Facility)", skin);
            title.setFontScale(1.3f);
            title.setColor(Color.CYAN);
            tooltipTable.add(title).left().padBottom(5).row();

            // Size Label
            Label size = new Label("Size: " + f.getGridWidth() + "x" + f.getGridHeight(), skin);
            size.setColor(Color.LIGHT_GRAY);
            tooltipTable.add(size).left().padBottom(10).row();

            // Produces
            String producesText = f.getProduces() != null ? f.getProduces().name() : "None";
            tooltipTable.add(new Label("Produces: " + producesText, skin)).left().row();

            Label outLabel = new Label("  ↳ Stored Output: " + f.getStoredOutput(), skin);
            outLabel.setColor(Color.LIGHT_GRAY);
            tooltipTable.add(outLabel).left().padBottom(5).row();

            // Consumes
            String consumesText = f.getConsumes() != null ? f.getConsumes().name() : "None";
            tooltipTable.add(new Label("Consumes: " + consumesText, skin)).left().row();

            if (f.getConsumes() != null) {
                Label inLabel = new Label("  ↳ Stored Input: " + f.getStoredInput(), skin);
                inLabel.setColor(Color.LIGHT_GRAY);
                tooltipTable.add(inLabel).left().row();
            }
        }

        // Resize the background to perfectly wrap around the newly stacked labels
        tooltipTable.pack();

        // Position it near the mouse
        float stageY = Gdx.graphics.getHeight() - screenY;
        tooltipTable.setPosition(screenX + 15, stageY - 15 - tooltipTable.getHeight());
        tooltipTable.setVisible(true);
    }

    //updates all the UI logic, it draws the stage to the screen so that the HUD appears on top of the game
    public void render() {
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    //allows the HUD buttons to reveive click events
    public Stage getStage() {
        return stage;
    }

    //makes sure that the size doesnt change as we zoom-in or out
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    //notifies the GameScreen when the pause button is clicked
    public void setPauseListener(PauseListener listener) {
        this.pauseListener = listener;
    }

    public interface PauseListener {
        void onPause();
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

        //defines how buttons look in their 3 stages
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = tempSkin.newDrawable("background", Color.DARK_GRAY);
        buttonStyle.down = tempSkin.newDrawable("background", Color.GRAY);
        buttonStyle.over = tempSkin.newDrawable("background", Color.LIGHT_GRAY);
        buttonStyle.font = tempSkin.getFont("default");
        tempSkin.add("default", buttonStyle);

        return tempSkin;
    }
}
