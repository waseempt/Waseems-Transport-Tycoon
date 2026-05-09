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
import io.github.transport_tycoon.model.GoodType;

public class PurchaseVehicle {

    private Stage stage;
    private Skin skin;
    private boolean visible = false;
    private String selectedType = null;
    private int selectedModel = 1; // 1 = Fast/Small, 2 = Slow/Heavy
    private GoodType selectedCargo = null;
    private String vehicleNameText = "";
    private TextField nameField;
    private Label errorLabel;

    public interface ConfirmListener {
        void onConfirm(String name, String type, int modelVariant, int price, GoodType cargo);
    }

    private ConfirmListener confirmListener;

    public void setConfirmListener(ConfirmListener listener) {
        this.confirmListener = listener;
    }

    public interface CloseListener {
        void onClose();
    }

    private CloseListener closeListener;

    public void setCloseListener(CloseListener listener) {
        this.closeListener = listener;
    }

    public PurchaseVehicle(SpriteBatch batch) {
        this.stage = new Stage(new ScreenViewport(), batch);
        this.skin = createBasicSkin();
    }

    public void show() {
        selectedType = null;
        selectedModel = 1;
        selectedCargo = null;
        vehicleNameText = "";
        stage.clear();
        buildUI();
        visible = true;
    }

    public void hide() {
        visible = false;
        stage.clear();
    }

    public boolean isVisible() {
        return visible;
    }

    private void refresh() {
        if (nameField != null) {
            vehicleNameText = nameField.getText();
        }
        stage.clear();
        buildUI();
    }

    private void buildUI() {
        Table outer = new Table();
        outer.setFillParent(true);
        outer.center();
        stage.addActor(outer);

        Table panel = new Table(skin);
        panel.setBackground(skin.newDrawable("background", new Color(0.1f, 0.1f, 0.1f, 0.95f)));
        panel.pad(25);
        panel.defaults().padBottom(12).left();

        Label title = new Label("Vehicle Dealership", skin, "title");
        panel.add(title).center().padBottom(20).row();

        panel.add(new Label("Vehicle Name:", skin)).row();
        nameField = new TextField(vehicleNameText, skin);
        nameField.setMessageText("Enter vehicle name...");
        panel.add(nameField).width(350).row();

        panel.add(new Label("Vehicle Category:", skin)).padTop(5).row();
        Table typeRow = new Table();

        TextButton busButton = new TextButton("Bus", skin, "Bus".equals(selectedType) ? "selected" : "default");
        busButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedType = "Bus";
                selectedCargo = null;
                selectedModel = 1; // Default to model 1 when switching
                refresh();
            }
        });

        TextButton truckButton = new TextButton("Truck", skin, "Truck".equals(selectedType) ? "selected" : "default");
        truckButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedType = "Truck";
                selectedModel = 1;
                refresh();
            }
        });

        typeRow.add(busButton).width(170).height(40).padRight(10);
        typeRow.add(truckButton).width(170).height(40);
        panel.add(typeRow).row();

        // Dynamically show Variant options based on the chosen category
        if (selectedType != null) {
            panel.add(new Label("Vehicle Model:", skin)).padTop(5).row();
            Table modelRow = new Table();

            String m1Name = "Bus".equals(selectedType) ? "Express Bus" : "Light Freight";
            String m2Name = "Bus".equals(selectedType) ? "Heavy Transit" : "Heavy Hauler";

            TextButton m1Btn = new TextButton(m1Name, skin, selectedModel == 1 ? "selected" : "default");
            m1Btn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    selectedModel = 1;
                    refresh();
                }
            });

            TextButton m2Btn = new TextButton(m2Name, skin, selectedModel == 2 ? "selected" : "default");
            m2Btn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    selectedModel = 2;
                    refresh();
                }
            });

            modelRow.add(m1Btn).width(170).height(40).padRight(10);
            modelRow.add(m2Btn).width(170).height(40);
            panel.add(modelRow).row();

            // Dynamic Stats Label
            String stats;
            if ("Bus".equals(selectedType)) {
                stats = (selectedModel == 1) ?
                    "Capacity: 30 | Speed: Fast | Maint: $10" :
                    "Capacity: 60 | Speed: Slow | Maint: $5";
            } else {
                stats = (selectedModel == 1) ?
                    "Capacity: 40 | Speed: Fast | Maint: $10" :
                    "Capacity: 80 | Speed: Slow | Maint: $5";
            }
            Label statsLabel = new Label(stats, skin);
            statsLabel.setColor(Color.LIGHT_GRAY);
            panel.add(statsLabel).center().padTop(2).row();
        }

        if ("Truck".equals(selectedType)) {
            panel.add(new Label("Cargo Specialization:", skin)).padTop(10).row();
            Table cargoRow = new Table();
            GoodType[] cargoOptions = { GoodType.WOOD, GoodType.IRON, GoodType.STEEL, GoodType.COAL };

            for (GoodType cargoType : cargoOptions) {
                final GoodType ct = cargoType;
                TextButton cargoButton = new TextButton(ct.name(), skin, ct == selectedCargo ? "selected" : "default");
                cargoButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        selectedCargo = ct;
                        refresh();
                    }
                });
                cargoRow.add(cargoButton).width(84).height(40).padRight(4);
            }
            panel.add(cargoRow).row();
        }

        errorLabel = new Label("", skin, "error");
        panel.add(errorLabel).center().padTop(5).row();

        // Calculate dynamic price based on the selected model
        final int finalPrice = (selectedModel == 1) ? 200 : 350;

        Table buttonRow = new Table();
        TextButton confirmButton = new TextButton("Buy ($" + finalPrice + ")", skin);
        confirmButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String vehicleName = nameField.getText().trim();

                if (vehicleName.isEmpty()) {
                    errorLabel.setText("Please enter a vehicle name.");
                    return;
                }
                if (selectedType == null) {
                    errorLabel.setText("Please select a vehicle category.");
                    return;
                }
                if ("Truck".equals(selectedType) && selectedCargo == null) {
                    errorLabel.setText("Please select a cargo type.");
                    return;
                }

                GoodType cargo = "Bus".equals(selectedType) ? GoodType.PASSENGERS : selectedCargo;

                if (confirmListener != null) {
                    confirmListener.onConfirm(vehicleName, selectedType, selectedModel, finalPrice, cargo);
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

        buttonRow.add(confirmButton).width(200).height(45).padRight(10);
        buttonRow.add(cancelButton).width(140).height(45);
        panel.add(buttonRow).center().padTop(10).row();

        outer.add(panel).width(480);
    }

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

    public Stage getStage() {
        return stage;
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

        Label.LabelStyle errorStyle = new Label.LabelStyle();
        errorStyle.font = tempSkin.getFont("default");
        errorStyle.fontColor = Color.RED;
        tempSkin.add("error", errorStyle);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up   = tempSkin.newDrawable("background", Color.DARK_GRAY);
        buttonStyle.down = tempSkin.newDrawable("background", Color.GRAY);
        buttonStyle.over = tempSkin.newDrawable("background", Color.LIGHT_GRAY);
        buttonStyle.font = tempSkin.getFont("default");
        tempSkin.add("default", buttonStyle);

        TextButton.TextButtonStyle selectedStyle = new TextButton.TextButtonStyle();
        selectedStyle.up   = tempSkin.newDrawable("background", new Color(0.15f, 0.55f, 0.15f, 1f));
        selectedStyle.down = tempSkin.newDrawable("background", new Color(0.20f, 0.65f, 0.20f, 1f));
        selectedStyle.over = tempSkin.newDrawable("background", new Color(0.20f, 0.65f, 0.20f, 1f));
        selectedStyle.font = tempSkin.getFont("default");
        tempSkin.add("selected", selectedStyle);

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font             = tempSkin.getFont("default");
        textFieldStyle.fontColor        = Color.WHITE;
        textFieldStyle.background       = tempSkin.newDrawable("background", Color.DARK_GRAY);
        textFieldStyle.cursor           = tempSkin.newDrawable("background", Color.WHITE);
        textFieldStyle.messageFontColor = Color.GRAY;
        textFieldStyle.messageFont      = tempSkin.getFont("default");
        tempSkin.add("default", textFieldStyle);

        return tempSkin;
    }
}
