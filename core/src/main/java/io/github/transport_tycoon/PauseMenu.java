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


public class PauseMenu {

    private Stage stage;

    private Skin skin;

    private boolean visible = false;


    public PauseMenu(SpriteBatch batch) {
        this.stage = new Stage(new ScreenViewport(), batch);
        this.skin = createBasicSkin();
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

        //exit button
        TextButton exitButton = new TextButton("Exit to Menu", skin);
        panel.add(exitButton).width(200).height(50).row();

        outer.add(panel);
    }

    //renders the pause menu only if it is currently visible
    public void render() {
        if (!visible) return;
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
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
        skin.dispose();
    }

    private Skin createBasicSkin() {
        Skin tempSkin = new Skin();
        BitmapFont font = new BitmapFont();
        BitmapFont titleFont = new BitmapFont();
        tempSkin.add("default", font);
        tempSkin.add("title", titleFont);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        tempSkin.add("background", new Texture(pixmap));
        pixmap.dispose();

        Label.LabelStyle defaultStyle = new Label.LabelStyle();
        defaultStyle.font = tempSkin.getFont("default");
        defaultStyle.fontColor = Color.WHITE;
        tempSkin.add("default", defaultStyle);

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = tempSkin.getFont("title");
        titleStyle.fontColor = Color.WHITE;
        tempSkin.add("title", titleStyle);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = tempSkin.newDrawable("background", Color.DARK_GRAY);
        buttonStyle.down = tempSkin.newDrawable("background", Color.GRAY);
        buttonStyle.over = tempSkin.newDrawable("background", Color.LIGHT_GRAY);
        buttonStyle.font = tempSkin.getFont("default");
        tempSkin.add("default", buttonStyle);

        return tempSkin;
    }
}
