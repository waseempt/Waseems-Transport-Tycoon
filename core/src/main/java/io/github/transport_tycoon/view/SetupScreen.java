package io.github.transport_tycoon.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.transport_tycoon.control.GameScreen;
import io.github.transport_tycoon.control.TransportTycoon;


public class SetupScreen implements Screen {

    private final TransportTycoon game;
    private Stage stage;
    private Skin skin;

    //stores the tycoon name
    private TextField tycoonField;
    //displays error when the player try to start the game without a name
    private Label errorLabel;



    public SetupScreen(TransportTycoon game) {
        this.game = game;
        this.stage = new Stage(new ExtendViewport(1280, 720), game.batch);
        this.skin = SkinManager.getSkin();
        buildUI();
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    private void buildUI() {
        //fills the entire screen and centers all the elements
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Title
        Label titleLabel = new Label("Game Setup", skin, "title");
        table.add(titleLabel).padBottom(40).row();

        // Tycoon name
        Label tycoonLabel = new Label("Tycoon Name:", skin);
        table.add(tycoonLabel).left().padBottom(5).row();
        tycoonField = new TextField("", skin);
        tycoonField.setMessageText("Enter tycoon name...");
        table.add(tycoonField).width(300).padBottom(20).row();

        // Buttons
        TextButton startButton = new TextButton("Start Game", skin);
        TextButton menuButton = new TextButton("Return to Menu", skin);

        //makes sure that the name is not empty before starting the game
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String tycoonName = tycoonField.getText().trim();

                if (tycoonName.isEmpty()) {
                    errorLabel.setText("Please enter your tycoon name.");
                    return;
                }

                System.out.println("Starting game as: " + tycoonName);
                game.setScreen(new GameScreen(game, tycoonName));
            }
        });

        //returns the player to the main menu
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        table.add(startButton).width(300).height(50).padBottom(15).row();
        table.add(menuButton).width(300).height(50).row();

        errorLabel = new Label("", skin, "error");
        table.add(errorLabel).padTop(10).row();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override public void show() {
        Gdx.input.setInputProcessor(stage);
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
