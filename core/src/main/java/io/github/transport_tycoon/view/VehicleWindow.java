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
import io.github.transport_tycoon.model.GameWorld;
import io.github.transport_tycoon.model.Vehicle;

// a popup window that lists all vehicles the player owns.
public class VehicleWindow {

    private Stage stage;

    private Skin skin;

    private boolean visible = false;

    private GameWorld world;

    public interface PurchaseListener {
        void onPurchase();
    }

    public interface AssignRouteListener {
        void onAssignRoute(Vehicle vehicle);
    }
    private AssignRouteListener assignRouteListener;
    public void setAssignRouteListener(AssignRouteListener listener) {
        this.assignRouteListener = listener;
    }

    private PurchaseListener purchaseListener;

    public void setPurchaseListener(PurchaseListener listener) {
        this.purchaseListener = listener;
    }


    public VehicleWindow(SpriteBatch batch, GameWorld world) {
        this.world = world;
        this.stage = new Stage(new ScreenViewport(), batch);
        this.skin = createBasicSkin();
        System.out.println("View: VehicleWindow initialized.");
    }

    //Rebuilds and shows the vehicle list window. called each time the window is opened so the list is always up to date.
    public void show() {
        stage.clear();
        buildUI();
        visible = true;
        stage.getViewport().apply();
    }

    //hides the vehicle window
    public void hide() {
        visible = false;
        stage.clear();
    }

    //Returns whether the window is currently visible
    public boolean isVisible() {
        return visible;
    }

    private void buildUI() {
        // Outer table centers the window on screen
        Table outer = new Table();
        outer.setFillParent(true);
        outer.center();
        stage.addActor(outer);

        // Dark background panel
        Table panel = new Table(skin);
        panel.setBackground(skin.newDrawable("background", new Color(0.1f, 0.1f, 0.1f, 0.9f)));
        panel.pad(20);
        panel.defaults().padBottom(10);

        // Title
        Label title = new Label("Your Vehicles", skin, "title");
        panel.add(title).colspan(3).padBottom(15).row();

        // Column headers
        panel.add(new Label("Name", skin)).expandX().left();
        panel.add(new Label("Type / Cargo", skin)).expandX().center();
        panel.add(new Label("Action", skin)).expandX().right();
        panel.row().padBottom(5);

        // List all unassigned vehicles
        for (Vehicle vehicle : world.getUnassignedVehicles()) {
            Label nameLabel = new Label(vehicle.getName(), skin);
            String typeInfo = vehicle.getClass().getSimpleName() + " / " + vehicle.getCargoType();
            Label typeLabel = new Label(typeInfo, skin);

            // Assign Route button — wired in a future commit
            TextButton assignButton = new TextButton("Assign Route", skin);
            assignButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (assignRouteListener != null) {
                        hide();
                        assignRouteListener.onAssignRoute(vehicle);
                    }
                }
            });

            panel.add(nameLabel).expandX().left();
            panel.add(typeLabel).expandX().center();
            panel.add(assignButton).width(120).height(35).right();
            panel.row().padBottom(5);
        }

        // Show message if no vehicles owned yet
        if (world.getUnassignedVehicles().isEmpty()) {
            panel.add(new Label("No vehicles owned yet.", skin)).colspan(3).center().padBottom(10).row();
        }

        // Close button
        TextButton closeButton = new TextButton("Close", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });

        // Purchase Vehicle button — opens purchase screen
        TextButton purchaseButton = new TextButton("Purchase Vehicle", skin);
        purchaseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Purchase Button Clicked!");
                if (purchaseListener != null) {
                    purchaseListener.onPurchase();
                }else {
                    System.out.println("Error: purchaseListener is null!");
                }
            }
        });

        panel.add(purchaseButton).colspan(2).width(200).height(45).padTop(15);
        panel.add(closeButton).width(100).height(45).padTop(15);

        outer.add(panel).width(500);
    }

    //Renders the vehicle window if visible, called every frame from GameScreen after all other rendering.
    public void render() {
        if (!visible) return;
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

        // Defines how buttons look in their 3 states
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = tempSkin.newDrawable("background", Color.DARK_GRAY);
        buttonStyle.down = tempSkin.newDrawable("background", Color.GRAY);
        buttonStyle.over = tempSkin.newDrawable("background", Color.LIGHT_GRAY);
        buttonStyle.font = tempSkin.getFont("default");
        tempSkin.add("default", buttonStyle);

        return tempSkin;
    }
}
