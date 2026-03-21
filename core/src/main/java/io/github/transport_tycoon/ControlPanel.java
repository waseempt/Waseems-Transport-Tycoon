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
import com.badlogic.gdx.utils.viewport.Viewport;

public class ControlPanel {

    private Stage stage;
    private Skin skin;

    public ControlPanel(SpriteBatch batch) {
        this.stage = new Stage(new ScreenViewport(), batch);
        this.skin = createBasicSkin();
        buildUI();
        System.out.println("View: ControlPanel initialized.");
    }

    private void buildUI() {
        Table panel = new Table();
        panel.setFillParent(true);
        panel.bottom().left();
        panel.pad(10);
        stage.addActor(panel);

        Table background = new Table(skin);
        background.setBackground(skin.newDrawable("background", new Color(0.1f, 0.1f, 0.1f, 0.85f)));
        background.pad(12);
        background.defaults().left().padRight(20);



        // Wide horizontal bar: ~75% of viewport width, fixed height
        panel.add(background).width(1100).height(60);
    }

    public void render() {
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public Stage getStage() {
        return stage;
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
