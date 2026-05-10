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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.transport_tycoon.model.Intersection;

public class TrafficLightUI {

    private Stage stage;
    private Skin skin;
    private boolean visible = false;
    private Intersection activeIntersection;

    private TextField[] durationFields;
    private Label errorLabel;

    public interface CloseListener {
        void onClose();
    }
    private CloseListener closeListener;

    public void setCloseListener(CloseListener listener) {
        this.closeListener = listener;
    }

    public interface PurchaseListener {
        void onPurchase(Intersection intersection);
    }

    private PurchaseListener purchaseListener;

    public void setPurchaseListener(PurchaseListener listener) {
        this.purchaseListener = listener;
    }

    public TrafficLightUI(SpriteBatch batch) {
        this.stage = new Stage(new ScreenViewport(), batch);
        this.skin = SkinManager.getSkin();
    }

    public void show(Intersection intersection) {
        this.activeIntersection = intersection;
        stage.clear();
        buildUI();
        visible = true;
    }

    public void hide() {
        visible = false;
        stage.clear();
    }

    public void showError(String message) {
        if (errorLabel != null) {
            errorLabel.setText(message);
        }
    }

    private void buildUI() {
        Table outer = new Table();
        outer.setFillParent(true);
        outer.center();
        stage.addActor(outer);

        Table panel = new Table(skin);
        panel.setBackground(skin.newDrawable("background", new Color(0.1f, 0.1f, 0.1f, 0.92f)));
        panel.pad(25).defaults().padBottom(12);

        if (!activeIntersection.hasLights()) {
            buildPurchaseUI(panel);
        } else {
            buildConfigUI(panel);
        }

        outer.add(panel);
    }

    private void buildPurchaseUI(Table panel) {
        panel.add(new Label("Install Traffic Lights?", skin)).colspan(2).padBottom(10).row();

        errorLabel = new Label("", skin);
        errorLabel.setColor(Color.RED);
        panel.add(errorLabel).colspan(2).padBottom(15).row();

        Table buttonRow = new Table();
        TextButton buyButton = new TextButton("Purchase ($200)", skin);
        buyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (purchaseListener != null) {
                    purchaseListener.onPurchase(activeIntersection);
                }
            }
        });

        TextButton cancelButton = new TextButton("Cancel", skin);
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (closeListener != null) closeListener.onClose();
            }
        });

        buttonRow.add(buyButton).width(160).height(40).padRight(10);
        buttonRow.add(cancelButton).width(100).height(40);
        panel.add(buttonRow).colspan(2).row();
    }

    private void buildConfigUI(Table panel) {
        String titleText = activeIntersection.getPhaseCount() == 2 ? "4-Way Intersection Lights" : "3-Way Intersection Lights";
        panel.add(new Label(titleText, skin)).colspan(2).padBottom(20).row();

        int phaseCount = activeIntersection.getPhaseCount();
        durationFields = new TextField[phaseCount];

        for (int i = 0; i < phaseCount; i++) {
            String labelText = "";
            if (phaseCount == 2) {
                labelText = (i == 0) ? "Vertical Duration (sec): " : "Horizontal Duration (sec): ";
            } else {
                if (i == 0) labelText = "Left Approach (sec): ";
                else if (i == 1) labelText = "Right Approach (sec): ";
                else labelText = "Bottom Approach (sec): ";
            }

            panel.add(new Label(labelText, skin)).left();

            durationFields[i] = new TextField(String.valueOf((int)activeIntersection.getPhaseDuration(i)), skin);
            panel.add(durationFields[i]).width(100).row();
        }

        Table buttonRow = new Table();
        TextButton applyButton = new TextButton("Apply", skin);
        applyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    for (int i = 0; i < phaseCount; i++) {
                        float duration = Float.parseFloat(durationFields[i].getText());
                        if (duration < 2f) duration = 2f;
                        activeIntersection.setPhaseDuration(i, duration);
                    }
                    if (closeListener != null) closeListener.onClose();
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format entered.");
                }
            }
        });

        TextButton cancelButton = new TextButton("Cancel", skin);
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (closeListener != null) closeListener.onClose();
            }
        });

        buttonRow.add(applyButton).width(120).height(40).padRight(10);
        buttonRow.add(cancelButton).width(120).height(40);
        panel.add(buttonRow).colspan(2).padTop(10).row();
    }

    public void render() {
        if (!visible) return;
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    public Stage getStage() { return stage; }
    public boolean isVisible() { return visible; }
    public void resize(int width, int height) { stage.getViewport().update(width, height, true); }
    public void dispose() { stage.dispose(); }
}
