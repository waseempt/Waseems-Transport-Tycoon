package io.github.transport_tycoon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class HUD {
    //a place where all the HUD widgets are held/placed
    private Stage stage;
    //fonts, colors, etc.
    private Skin skin;


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
        Label balanceLabel = new Label("Balance: [placeholder]", skin);

        //shows the time in the middle
        Label timeLabel = new Label("Time: [placeholder]", skin);

        //shows the pause button on the right
        Label pauseLabel = new Label("Pause Game: [placeholder]", skin);

        //where every label is placed
        background.add(balanceLabel).expandX().left();
        background.add(timeLabel).expandX().center();
        background.add(pauseLabel).expandX().right();

        //placeholder labels
        panel.add(background).growX().height(40);
    }


    //updates all teh UI logic, it draws the stage to the screen so that the HUD appears on top of the game
    public void render() {
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    //makes sure that the size doesnt change as we zoom-in or out
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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

        return tempSkin;
    }
}
