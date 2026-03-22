package io.github.transport_tycoon;

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

        //shows the pause button on the right
        Label pauseLabel = new Label("Pause Game: [placeholder]", skin);

        //pause button
        TextButton pauseButton = new TextButton("Pause Game", skin);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (pauseListener != null) {
                    pauseListener.onPause();
                }
            }
        });

        //where every label is placed
        background.add(balanceLabel).expandX().left();
        background.add(timeLabel).expandX().center();
        background.add(buildModeIndicator).expandX().center();
        background.add(pauseButton).expandX().right();

        //placeholder labels
        panel.add(background).growX().height(40);
    }

    public void updateBalance(float balanceAmount) {
        // Updates the text on the screen
        this.balanceLabel.setText("Balance: $" + balanceAmount);
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
            this.buildModeIndicator.setText("BUILD MODE ACTIVE");
        } else {
            this.buildModeIndicator.setText("");
        }
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
