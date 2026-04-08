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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

// Overlay screen where the player configures and purchases a new vehicle.
//Allows selecting vehicle type (Bus or Truck), cargo type (if Truck), and entering a name. Confirms purchase and deducts $200.

public class PurchaseVehicle {

    private Stage stage;

    private Skin skin;

    private boolean visible = false;

    private String selectedType = "Bus";

    private GoodType selectedCargo = GoodType.WOOD;

    private TextField nameField;

    private Label errorLabel;

    public interface ConfirmListener {
        void onConfirm(String name, String type, GoodType cargo);
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
        buildUI();
        System.out.println("View: PurchaseVehicleScreen initialized.");
    }

    public void show() {
        visible = true;
    }

    public void hide() {
        visible = false;
    }

    public boolean isVisible() {
        return visible;
    }

    private void buildUI() {
        Table outer = new Table();
        outer.setFillParent(true);
        outer.center();
        stage.addActor(outer);

        Table panel = new Table(skin);
        panel.setBackground(skin.newDrawable("background", new Color(0.1f, 0.1f, 0.1f, 0.92f)));
        panel.pad(25);
        panel.defaults().padBottom(12).left();

        Label title = new Label("Purchase Vehicle ($200)", skin, "title");
        panel.add(title).center().padBottom(20).row();

        // Vehicle name input
        panel.add(new Label("Vehicle Name:", skin)).row();
        nameField = new TextField("", skin);
        nameField.setMessageText("Enter vehicle name...");
        panel.add(nameField).width(300).row();

        // Vehicle type selection
        panel.add(new Label("Type:", skin)).padTop(10).row();

        Table typeRow = new Table();

        // Bus button
        TextButton busButton = new TextButton("Bus", skin);
        busButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedType = "Bus";
                System.out.println("Selected type: Bus");
            }
        });

        // Truck button
        TextButton truckButton = new TextButton("Truck", skin);
        truckButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedType = "Truck";
                System.out.println("Selected type: Truck");
            }
        });

        typeRow.add(busButton).width(130).height(40).padRight(10);
        typeRow.add(truckButton).width(130).height(40);
        panel.add(typeRow).row();

        // Cargo type selection(only for trucks)
        panel.add(new Label("Cargo (Truck only):", skin)).padTop(10).row();

        Table cargoRow = new Table();
        TextButton woodButton = new TextButton("Wood", skin);
        woodButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedCargo = GoodType.WOOD;
                System.out.println("Selected cargo: WOOD");
            }
        });

        TextButton ironButton = new TextButton("Iron", skin);
        ironButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedCargo = GoodType.IRON;
                System.out.println("Selected cargo: IRON");
            }
        });

        TextButton steelButton = new TextButton("Steel", skin);
        steelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedCargo = GoodType.STEEL;
                System.out.println("Selected cargo: STEEL");
            }
        });

        cargoRow.add(woodButton).width(85).height(40).padRight(5);
        cargoRow.add(ironButton).width(85).height(40).padRight(5);
        cargoRow.add(steelButton).width(85).height(40);
        panel.add(cargoRow).row();

        // error label shown when input is invalid
        errorLabel = new Label("", skin, "error");
        panel.add(errorLabel).center().padTop(5).row();

        Table buttonRow = new Table();

        TextButton confirmButton = new TextButton("Confirm Purchase", skin);
        confirmButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String vehicleName = nameField.getText().trim();
                if (vehicleName.isEmpty()) {
                    errorLabel.setText("Please enter a vehicle name.");
                    return;
                }
                if (confirmListener != null) {
                    confirmListener.onConfirm(vehicleName, selectedType, selectedCargo);
                }
            }
        });

        TextButton cancelButton = new TextButton("Cancel", skin);
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (closeListener != null) {
                    closeListener.onClose();
                }
            }
        });

        buttonRow.add(confirmButton).width(180).height(45).padRight(10);
        buttonRow.add(cancelButton).width(100).height(45);
        panel.add(buttonRow).center().padTop(10).row();

        outer.add(panel).width(400);
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
        buttonStyle.up = tempSkin.newDrawable("background", Color.DARK_GRAY);
        buttonStyle.down = tempSkin.newDrawable("background", Color.GRAY);
        buttonStyle.over = tempSkin.newDrawable("background", Color.LIGHT_GRAY);
        buttonStyle.font = tempSkin.getFont("default");
        tempSkin.add("default", buttonStyle);

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = tempSkin.getFont("default");
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.background = tempSkin.newDrawable("background", Color.DARK_GRAY);
        textFieldStyle.cursor = tempSkin.newDrawable("background", Color.WHITE);
        textFieldStyle.messageFontColor = Color.GRAY;
        textFieldStyle.messageFont = tempSkin.getFont("default");
        tempSkin.add("default", textFieldStyle);

        return tempSkin;
    }

    public Stage getStage() {
        return stage;
    }
}
