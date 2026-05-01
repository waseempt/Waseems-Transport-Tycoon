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
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.transport_tycoon.control.RouteAssignmentMode;

public class RouteAssignmentOverlay {

    private Stage stage;
    private Skin skin;
    private boolean visible = false;

    private Label instructionLabel;
    private Label stopCountLabel;
    private TextButton confirmButton;

    public interface ConfirmListener { void onConfirm(); }
    public interface CancelListener  { void onCancel();  }

    private ConfirmListener confirmListener;
    private CancelListener  cancelListener;

    public void setConfirmListener(ConfirmListener l) { this.confirmListener = l; }
    public void setCancelListener(CancelListener l)   { this.cancelListener  = l; }

    public RouteAssignmentOverlay(SpriteBatch batch) {
        this.stage = new Stage(new ScreenViewport(), batch);
        this.skin  = createBasicSkin();
        buildUI();
    }

    private void buildUI() {
        Table root = new Table();
        root.setFillParent(true);
        root.top().center();
        root.padTop(50);
        stage.addActor(root);

        Table panel = new Table(skin);
        panel.setBackground(skin.newDrawable("bg", new Color(0.05f, 0.05f, 0.4f, 0.92f)));
        panel.pad(12, 20, 12, 20);
        panel.defaults().padRight(16);

        instructionLabel = new Label("Assigning route for: ?", skin);
        instructionLabel.setColor(Color.YELLOW);

        stopCountLabel = new Label("Stops selected: 0", skin);
        stopCountLabel.setColor(Color.WHITE);

        confirmButton = new TextButton("Confirm Route", skin, "confirm");
        confirmButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!confirmButton.isDisabled() && confirmListener != null) {
                    confirmListener.onConfirm();
                }
            }
        });

        TextButton cancelButton = new TextButton("Cancel", skin);
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (cancelListener != null) cancelListener.onCancel();
            }
        });

        panel.add(instructionLabel);
        panel.add(stopCountLabel);
        panel.add(confirmButton).width(150).height(36);
        panel.add(cancelButton).width(90).height(36).padRight(0);

        root.add(panel);
    }

    public void refresh(RouteAssignmentMode mode) {
        if (mode == null) return;
        instructionLabel.setText("Assigning route for: " + mode.getVehicle().getName());
        int count = mode.getSelectedStops().size();
        stopCountLabel.setText("Stops selected: " + count);
        confirmButton.setDisabled(!mode.canConfirm());
    }

    public void show(RouteAssignmentMode mode) {
        refresh(mode);
        visible = true;
    }

    public void hide() {
        visible = false;
    }

    public boolean isVisible() { return visible; }
    public Stage getStage()    { return stage;   }

    public void render() {
        if (!visible) return;
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    private Skin createBasicSkin() {
        Skin s = new Skin();
        s.add("default", new BitmapFont());

        Pixmap px = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        px.setColor(Color.WHITE);
        px.fill();
        s.add("bg", new Texture(px));
        px.dispose();

        Label.LabelStyle ls = new Label.LabelStyle();
        ls.font      = s.getFont("default");
        ls.fontColor = Color.WHITE;
        s.add("default", ls);

        TextButton.TextButtonStyle normal = new TextButton.TextButtonStyle();
        normal.up   = s.newDrawable("bg", Color.DARK_GRAY);
        normal.down = s.newDrawable("bg", Color.GRAY);
        normal.over = s.newDrawable("bg", Color.LIGHT_GRAY);
        normal.font = s.getFont("default");
        s.add("default", normal);

        TextButton.TextButtonStyle confirm = new TextButton.TextButtonStyle();
        confirm.up       = s.newDrawable("bg", new Color(0.1f, 0.55f, 0.1f, 1f));
        confirm.down     = s.newDrawable("bg", new Color(0.15f, 0.65f, 0.15f, 1f));
        confirm.over     = s.newDrawable("bg", new Color(0.15f, 0.65f, 0.15f, 1f));
        confirm.disabled = s.newDrawable("bg", new Color(0.25f, 0.25f, 0.25f, 1f));
        confirm.font     = s.getFont("default");
        s.add("confirm", confirm);

        return s;
    }
}
