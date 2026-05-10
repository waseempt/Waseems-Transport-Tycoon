package io.github.transport_tycoon.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class SkinManager {

    private static Skin instance;

    // Returns the singleton
    public static Skin getSkin() {
        if (instance == null) {
            instance = createSkin();
        }
        return instance;
    }

    private static Skin createSkin() {
        Skin skin = new Skin();

        // Fonts
        skin.add("default", new BitmapFont());
        skin.add("title", new BitmapFont());

        // Texture
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("background", new Texture(pixmap));
        pixmap.dispose();

        // Labels
        Label.LabelStyle defaultLabel = new Label.LabelStyle();
        defaultLabel.font = skin.getFont("default");
        defaultLabel.fontColor = Color.WHITE;
        skin.add("default", defaultLabel);

        Label.LabelStyle titleLabel = new Label.LabelStyle();
        titleLabel.font = skin.getFont("title");
        titleLabel.fontColor = Color.WHITE;
        skin.add("title", titleLabel);

        Label.LabelStyle errorLabel = new Label.LabelStyle();
        errorLabel.font = skin.getFont("default");
        errorLabel.fontColor = Color.RED;
        skin.add("error", errorLabel);



        // Default Button
        TextButton.TextButtonStyle defaultBtn = new TextButton.TextButtonStyle();
        defaultBtn.up = skin.newDrawable("background", Color.DARK_GRAY);
        defaultBtn.down = skin.newDrawable("background", Color.GRAY);
        defaultBtn.over = skin.newDrawable("background", Color.LIGHT_GRAY);
        defaultBtn.font = skin.getFont("default");
        skin.add("default", defaultBtn);

        // Selected Button
        TextButton.TextButtonStyle selectedBtn = new TextButton.TextButtonStyle();
        selectedBtn.up = skin.newDrawable("background", new Color(0.15f, 0.55f, 0.15f, 1f));
        selectedBtn.down = skin.newDrawable("background", new Color(0.20f, 0.65f, 0.20f, 1f));
        selectedBtn.over = skin.newDrawable("background", new Color(0.20f, 0.65f, 0.20f, 1f));
        selectedBtn.font = skin.getFont("default");
        skin.add("selected", selectedBtn);

        // Confirm Button
        TextButton.TextButtonStyle confirmBtn = new TextButton.TextButtonStyle();
        confirmBtn.up = skin.newDrawable("background", new Color(0.1f, 0.55f, 0.1f, 1f));
        confirmBtn.down = skin.newDrawable("background", new Color(0.15f, 0.65f, 0.15f, 1f));
        confirmBtn.over = skin.newDrawable("background", new Color(0.15f, 0.65f, 0.15f, 1f));
        confirmBtn.disabled = skin.newDrawable("background", new Color(0.25f, 0.25f, 0.25f, 1f));
        confirmBtn.font = skin.getFont("default");
        skin.add("confirm", confirmBtn);

        // TextField
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = skin.getFont("default");
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.background = skin.newDrawable("background", Color.DARK_GRAY);
        textFieldStyle.cursor = skin.newDrawable("background", Color.WHITE);
        textFieldStyle.messageFontColor = Color.GRAY;
        textFieldStyle.messageFont = skin.getFont("default");
        skin.add("default", textFieldStyle);

        // Scrollpane
        ScrollPane.ScrollPaneStyle scrollStyle = new ScrollPane.ScrollPaneStyle();
        skin.add("default", scrollStyle);

        return skin;
    }

    public static void dispose() {
        if (instance != null) {
            instance.dispose();
            instance = null;
        }
    }
}
