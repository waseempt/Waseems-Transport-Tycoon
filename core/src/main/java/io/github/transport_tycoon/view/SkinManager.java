package io.github.transport_tycoon.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class SkinManager {

    private static Skin instance;

    // Color Pallete
    private static final Color BG_DARK = Color.valueOf("1E1F22");
    private static final Color BG_PANEL = Color.valueOf("2B2D31");
    private static final Color BTN_DEFAULT = Color.valueOf("313338");
    private static final Color BTN_HOVER = Color.valueOf("404249");
    private static final Color BTN_DOWN = Color.valueOf("111214");

    private static final Color ACCENT_BLUE = Color.valueOf("5865F2");
    private static final Color ACCENT_GREEN = Color.valueOf("23A559");
    private static final Color ACCENT_GREEN_HOVER = Color.valueOf("1B8546");
    private static final Color ACCENT_RED = Color.valueOf("DA373C");

    private static final Color TEXT_MAIN = Color.valueOf("F2F3F5");
    private static final Color TEXT_MUTED = Color.valueOf("949BA4");

    public static Skin getSkin() {
        if (instance == null) {
            instance = createSkin();
        }
        return instance;
    }

    private static Skin createSkin() {
        Skin skin = new Skin();

        // Fonts
        BitmapFont defaultFont = generateFont("fonts/Nunito-Bold.ttf", 16);
        BitmapFont titleFont = generateFont("fonts/Nunito-SemiBold.ttf", 42);
        BitmapFont tooltipTitleFont = generateFont("fonts/Nunito-SemiBold.ttf", 24);

        skin.add("default", defaultFont);
        skin.add("title", titleFont);
        skin.add("tooltip-title", tooltipTitleFont);

        // Rounded Elements
        skin.add("bg_dark", createRoundedDrawable(BG_DARK, 6), Drawable.class);
        skin.add("bg_panel", createRoundedDrawable(BG_PANEL, 6), Drawable.class);
        skin.add("btn_default", createRoundedDrawable(BTN_DEFAULT, 6), Drawable.class);
        skin.add("btn_hover", createRoundedDrawable(BTN_HOVER, 6), Drawable.class);
        skin.add("btn_down", createRoundedDrawable(BTN_DOWN, 6), Drawable.class);
        skin.add("accent_blue", createRoundedDrawable(ACCENT_BLUE, 6), Drawable.class);
        skin.add("accent_green", createRoundedDrawable(ACCENT_GREEN, 6), Drawable.class);
        skin.add("accent_green_hover", createRoundedDrawable(ACCENT_GREEN_HOVER, 6), Drawable.class);
        skin.add("accent_red", createRoundedDrawable(ACCENT_RED, 6), Drawable.class);

        skin.add("cursor_color", createRoundedDrawable(TEXT_MAIN, 0), Drawable.class);
        skin.add("background", createRoundedDrawable(BG_PANEL, 6), Drawable.class);

        // Labels
        Label.LabelStyle defaultLabel = new Label.LabelStyle();
        defaultLabel.font = skin.getFont("default");
        defaultLabel.fontColor = TEXT_MAIN;
        skin.add("default", defaultLabel);

        Label.LabelStyle titleLabel = new Label.LabelStyle();
        titleLabel.font = skin.getFont("title");
        titleLabel.fontColor = TEXT_MAIN;
        skin.add("title", titleLabel);

        Label.LabelStyle tooltipTitleLabel = new Label.LabelStyle();
        titleLabel.font = skin.getFont("tooltip-title");
        titleLabel.fontColor = TEXT_MAIN;
        skin.add("tooltip-title", titleLabel);

        Label.LabelStyle errorLabel = new Label.LabelStyle();
        errorLabel.font = skin.getFont("default");
        errorLabel.fontColor = ACCENT_RED;
        skin.add("error", errorLabel);

        // Buttons
        TextButton.TextButtonStyle defaultBtn = new TextButton.TextButtonStyle();
        defaultBtn.up = skin.getDrawable("btn_default");
        defaultBtn.down = skin.getDrawable("btn_down");
        defaultBtn.over = skin.getDrawable("btn_hover");
        defaultBtn.font = skin.getFont("default");
        defaultBtn.fontColor = TEXT_MAIN;
        skin.add("default", defaultBtn);

        TextButton.TextButtonStyle selectedBtn = new TextButton.TextButtonStyle();
        selectedBtn.up = skin.getDrawable("accent_blue");
        selectedBtn.down = skin.getDrawable("btn_down");
        selectedBtn.over = skin.getDrawable("btn_hover");
        selectedBtn.font = skin.getFont("default");
        selectedBtn.fontColor = Color.WHITE;
        skin.add("selected", selectedBtn);

        TextButton.TextButtonStyle confirmBtn = new TextButton.TextButtonStyle();
        confirmBtn.up = skin.getDrawable("accent_green");
        confirmBtn.down = skin.getDrawable("btn_down");
        confirmBtn.over = skin.getDrawable("accent_green_hover");
        confirmBtn.disabled = skin.getDrawable("bg_dark");
        confirmBtn.font = skin.getFont("default");
        confirmBtn.fontColor = Color.WHITE;
        confirmBtn.disabledFontColor = TEXT_MUTED;
        skin.add("confirm", confirmBtn);

        // TextField
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = skin.getFont("default");
        textFieldStyle.fontColor = TEXT_MAIN;
        textFieldStyle.background = skin.getDrawable("bg_dark");
        textFieldStyle.cursor = skin.getDrawable("cursor_color");
        textFieldStyle.messageFontColor = TEXT_MUTED;
        textFieldStyle.messageFont = skin.getFont("default");
        skin.add("default", textFieldStyle);

        // Scrollpane
        ScrollPane.ScrollPaneStyle scrollStyle = new ScrollPane.ScrollPaneStyle();
        skin.add("default", scrollStyle);

        return skin;
    }

    private static Drawable createRoundedDrawable(Color color, int radius) {
        if (radius == 0) {
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(color);
            pixmap.fill();
            Texture texture = new Texture(pixmap);
            pixmap.dispose();
            return new NinePatchDrawable(new NinePatch(texture));
        }

        int d = radius * 2;
        int size = d + 4;
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);

        // Draw 4 corners
        pixmap.fillCircle(radius, radius, radius);
        pixmap.fillCircle(size - radius, radius, radius);
        pixmap.fillCircle(radius, size - radius, radius);
        pixmap.fillCircle(size - radius, size - radius, radius);

        // Fill the spaces between corners
        pixmap.fillRectangle(radius, 0, size - d, size);
        pixmap.fillRectangle(0, radius, size, size - d);

        Texture texture = new Texture(pixmap);
        pixmap.dispose();

        // Create NinePatch and wrap it in a Drawable
        NinePatch patch = new NinePatch(texture, radius, radius, radius, radius);
        NinePatchDrawable drawable = new NinePatchDrawable(patch);

        // Add built-in padding so text never touches the corners
        drawable.setTopHeight(8);
        drawable.setBottomHeight(8);
        drawable.setLeftWidth(12);
        drawable.setRightWidth(12);

        return drawable;
    }

    private static BitmapFont generateFont(String fontPath, int size) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontPath));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        float oversample = 3f;
        parameter.size = (int)(size * oversample);

        parameter.genMipMaps = true;
        parameter.minFilter = Texture.TextureFilter.MipMapLinearLinear;
        parameter.magFilter = Texture.TextureFilter.Linear;

        BitmapFont font = generator.generateFont(parameter);

        font.getData().setScale(1f / oversample);

        generator.dispose();

        return font;
    }

    public static void dispose() {
        if (instance != null) {
            instance.dispose();
            instance = null;
        }
    }
}
