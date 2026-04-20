package io.github.transport_tycoon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class GameScreen implements Screen {

    private final TransportTycoon game;
    private GameController controller;
    private InputHandler inputHandler;
    private VehicleWindow vehicleWindow;
    private ControlPanel controlPanel;
    private HUD hud;
    private PauseMenu pauseMenu;
    private PurchaseVehicle purchaseVehicleScreen;
    private RouteAssignmentMode routeAssignmentMode = null;
    private RouteAssignmentOverlay routeAssignmentOverlay;

    private MinimapRenderer minimapRenderer;

    private TrafficLightUI trafficLightUI;

    private boolean isBuildMode = false;

    public GameScreen(TransportTycoon game,  String tycoonName) {
        this.game = game;

        // Instantiate the Controller, which instantiates the rest
        this.controller = new GameController(game.batch, tycoonName, game);

        //fixed size panel at the bottom of the screen
        this.controlPanel = new ControlPanel(game.batch);

        // connect speed controls to simulation
        controlPanel.setSpeedChangeListener(new ControlPanel.SpeedChangeListener() {
            @Override
            public void onPauseToggle() {
                if (controller.getWorld().isPaused()) {
                    controller.getWorld().resume();
                } else {
                    controller.getWorld().pause();
                }
            }

            @Override
            public void onSpeedSelected(float speed) {
                controller.getWorld().setTimeScale(speed);
            }
        });

        //fixed size panel at the top of the screen
        this.hud = new HUD(game.batch);

        this.pauseMenu = new PauseMenu(game.batch);
        //stops the simulation and shows the pause menu when game paused
        hud.setPauseListener(() -> {
            controller.getWorld().pause();
            pauseMenu.show();
        });

        // shows all owned vehicles
        this.vehicleWindow = new VehicleWindow(game.batch, controller.getWorld());

        // Opens vehicle window when Vehicles button clicked
        controlPanel.setVehicleWindowListener(() -> {
            vehicleWindow.show();
        });

        //restores simulation speed and hides the pause menu when resumed
        pauseMenu.setResumeListener(() -> {
            controller.getWorld().resume();
            pauseMenu.hide(inputHandler, hud.getStage(), controlPanel.getStage());
        });

        this.purchaseVehicleScreen = new PurchaseVehicle(game.batch);

        vehicleWindow.setPurchaseListener(() -> {
            System.out.println("Switching to Purchase Screen");
            vehicleWindow.hide();
            purchaseVehicleScreen.show();
        });
        this.routeAssignmentOverlay = new RouteAssignmentOverlay(game.batch);

        purchaseVehicleScreen.setCloseListener(() -> {
            purchaseVehicleScreen.hide();
            vehicleWindow.show();
        });

        purchaseVehicleScreen.setConfirmListener((name, type, cargo) -> {
            GameWorld world = controller.getWorld();

            if (world.getPlayerBalance() < 200) {
                System.out.println("Model: Not enough money to build a vehicle.");
                return;
            }

            world.setPlayerBalance(world.getPlayerBalance() - 200);

            hud.showBalanceChange(-200);

            Vehicle newVehicle = type.equals("Bus") ? new Bus(name) : new Truck(name, cargo);
            world.addVehicle(newVehicle);

            System.out.println("Model: Purchased " + name + " for $200.");

            purchaseVehicleScreen.hide();
            vehicleWindow.show();
        });

        //routes the player back to the main menu when exiting
        pauseMenu.setExitListener(() -> {
            game.setScreen(new MainMenuScreen(game));
        });

        //Build mode listener
        this.controlPanel.setBuildListener(() -> {
            isBuildMode = !isBuildMode;
            if (inputHandler.getBuildStopMode()){
                inputHandler.setBuildStopMode(false);
                hud.setStopBuildModeActive(false);
            }
            hud.setBuildModeActive(isBuildMode);
            inputHandler.setBuildMode(isBuildMode);
        });

        // Activates stop-building mode when Build Stop button is clicked
        controlPanel.setBuildStopListener(() -> {
            if (isBuildMode){
                isBuildMode = false;
                hud.setBuildModeActive(false);
            }
            inputHandler.setBuildStopMode(!inputHandler.getBuildStopMode());
            hud.setStopBuildModeActive(inputHandler.getBuildStopMode());
        });

        // When controller tells the screen balance was changed, balance tells the hud to activate the animation
        controller.getWorld().setBalanceChangeListener(amount -> {
            hud.showBalanceChange(amount);
        });

        // Minimap now belongs to GameScreen
        this.minimapRenderer = new MinimapRenderer();

        OrthographicCamera camera = controller.getWorldRenderer().getMainCamera();

        // pass minimap to input handler
        this.inputHandler = new InputHandler(camera, controller.getWorld(), minimapRenderer);

        this.inputHandler.setHoverListener((zone, screenX, screenY) -> {
            hud.updateTooltip(zone, screenX, screenY);
        });

        vehicleWindow.setAssignRouteListener(vehicle -> {
            routeAssignmentMode = new RouteAssignmentMode(vehicle);
            inputHandler.setRouteAssignmentMode(true);
            routeAssignmentOverlay.show(routeAssignmentMode);
            refreshInputMultiplexer();
        });

        inputHandler.setStopClickListener(stop -> {
            if (routeAssignmentMode == null) return;
            routeAssignmentMode.toggleStop(stop);
            routeAssignmentOverlay.refresh(routeAssignmentMode);
        });

        routeAssignmentOverlay.setCancelListener(this::exitRouteAssignmentMode);

        // TrafficLight UI
        this.trafficLightUI = new TrafficLightUI(game.batch);

        trafficLightUI.setCloseListener(() -> {
            trafficLightUI.hide();
        });

        trafficLightUI.setPurchaseListener(intersection -> {
            GameWorld world = controller.getWorld();

            if (world.getPlayerBalance() >= 200) {
                // Deduct the money
                world.setPlayerBalance(world.getPlayerBalance() - 200);

                // Trigger balance hud animation
                hud.showBalanceChange(-200);

                // Install the lights
                intersection.installLights();

                // Refresh the UI to immediately show the configuration menu
                trafficLightUI.show(intersection);
            } else {
                // Tell the UI to display the error text
                trafficLightUI.showError("Not enough money! ($200 required)");
            }
        });


        this.inputHandler.setIntersectionListener(intersection -> {
            trafficLightUI.show(intersection);
        });

        Gdx.input.setInputProcessor(this.inputHandler);
    }

    private void exitRouteAssignmentMode() {
        routeAssignmentMode = null;
        inputHandler.setRouteAssignmentMode(false);
        routeAssignmentOverlay.hide();
        refreshInputMultiplexer();
    }

    private void refreshInputMultiplexer() {
        com.badlogic.gdx.InputMultiplexer multiplexer = new com.badlogic.gdx.InputMultiplexer();
        if (routeAssignmentOverlay != null && routeAssignmentOverlay.isVisible()) {
            multiplexer.addProcessor(routeAssignmentOverlay.getStage());
        }
        multiplexer.addProcessor(trafficLightUI.getStage());
        multiplexer.addProcessor(purchaseVehicleScreen.getStage());
        multiplexer.addProcessor(vehicleWindow.getStage());
        multiplexer.addProcessor(hud.getStage());
        multiplexer.addProcessor(controlPanel.getStage());
        multiplexer.addProcessor(inputHandler);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {


        // Clear screen
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        controller.render(delta);

        // Render minimap on top of world
        minimapRenderer.render(controller.getWorld());

        // Update HUD values
        float currentBalance = controller.getWorld().getPlayerBalance();

        hud.updateBalance(currentBalance);
        hud.updateTime(controller.getWorld().getFormattedGameTime());
        hud.render();
        controlPanel.render();
        pauseMenu.render();
        vehicleWindow.render();
        purchaseVehicleScreen.render();
        trafficLightUI.render();
        routeAssignmentOverlay.render();

    }

    @Override
    public void resize(int width, int height) {
        // Prevent stretching when resizing viewport
        controller.getWorldRenderer().getViewport().update(width, height, false);
        controlPanel.resize(width, height);
        hud.resize(width, height);
        minimapRenderer.resize(width, height);
        pauseMenu.resize(width, height);
        vehicleWindow.resize(width, height);
        purchaseVehicleScreen.resize(width, height);
        trafficLightUI.resize(width, height);
        routeAssignmentOverlay.resize(width, height);

    }

    //sets up InputMultiplexer  so teh HUD stage receives button clicks
    @Override
    public void show() {
        refreshInputMultiplexer();
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        controlPanel.dispose();
        hud.dispose();
        minimapRenderer.dispose();
        pauseMenu.dispose();
        vehicleWindow.dispose();
        purchaseVehicleScreen.dispose();
        trafficLightUI.dispose();
        routeAssignmentOverlay.dispose();
    }

}
