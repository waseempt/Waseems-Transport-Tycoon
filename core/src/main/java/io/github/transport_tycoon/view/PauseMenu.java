package io.github.transport_tycoon.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.transport_tycoon.control.InputHandler;


public class PauseMenu {

    private Stage stage;

    private Skin skin;

    private boolean visible = false;

    private ResumeListener resumeListener;

    private ExitListener exitListener;

    private Label saveFeedbackLabel;



    public PauseMenu(SpriteBatch batch) {
        this.stage = new Stage(new ScreenViewport(), batch);
        this.skin = SkinManager.getSkin();
        buildUI();
        System.out.println("View: PauseMenu initialized.");
    }

    private void buildUI() {
        //outer table centers content on screen
        Table outer = new Table();
        outer.setFillParent(true);
        outer.center();
        stage.addActor(outer);

        //dark background panel
        Table panel = new Table(skin);
        panel.setBackground(skin.newDrawable("background", new Color(0.1f, 0.1f, 0.1f, 0.9f)));
        panel.pad(30);
        panel.defaults().padBottom(15);

        //title
        Label title = new Label("Paused", skin, "title");
        panel.add(title).padBottom(30).row();

        //resume button
        TextButton resumeButton = new TextButton("Resume", skin);
        panel.add(resumeButton).width(200).height(50).row();

        //save button
        TextButton saveButton = new TextButton("Save Game", skin);
        panel.add(saveButton).width(200).height(50).row();

        //exit button
        TextButton exitButton = new TextButton("Exit to Menu", skin);
        panel.add(exitButton).width(200).height(50).row();

        saveFeedbackLabel = new Label("", skin);
        saveFeedbackLabel.getColor().a = 0; // Start fully transparent
        panel.add(saveFeedbackLabel).padTop(15).row();

        outer.add(panel);

        //ResumeListener callback when clicked, it handles restoring time and hiding the menu
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (resumeListener != null) {
                    resumeListener.onResume();
                }
            }
        });

        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (saveListener != null) {
                    saveListener.onSave();
                }
            }
        });

        //Fires the ExitListener callback when clicked
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (exitListener != null) {
                    exitListener.onExit();
                }
            }
        });
    }

    //renders the pause menu only if it is currently visible
    public void render() {
        if (!visible) return;
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }


    //Interface is used to notify GameScreen when the exit button is clicked

    public interface ExitListener {
        void onExit();
    }

    //sets the ExitListerner so gameScreen reacts when the player clicks Exit
    public void setExitListener(ExitListener listener) {
        this.exitListener = listener;
    }

    public interface SaveListener {
        void onSave();
    }
    private SaveListener saveListener;
    public void setSaveListener(SaveListener listener) {
        this.saveListener = listener;
    }

    public void showSaveSuccess() {
        saveFeedbackLabel.setText("Game Saved Successfully!");
        saveFeedbackLabel.clearActions(); // Stop any existing fade animations

        // Sequence: Become visible immediately -> Wait 2 seconds -> Fade out over 1 second
        saveFeedbackLabel.addAction(Actions.sequence(
            Actions.alpha(1f),
            Actions.delay(2f),
            Actions.fadeOut(1f)
        ));
    }

    public void hide() {
        visible = false;
    }
    //notifies the GameScreen when the resume button is clicked
    public interface ResumeListener {
        void onResume();
    }

    public void setResumeListener(ResumeListener listener) {
        this.resumeListener = listener;
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    //shows the pause menu
    public void show() {
        visible = true;
        Gdx.input.setInputProcessor(stage);
    }

    //returns whether the pause menu is currently visible
    public boolean isVisible() {
        return visible;
    }


    public void dispose() {
        stage.dispose();
    }
}
