package io.github.transport_tycoon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


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
        this.stage = new Stage(new ScreenViewport(), game.batch);
        this.skin = createBasicSkin();
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
        skin.dispose();
    }

    //builds minimal Skin
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

        //styles for the labels
        Label.LabelStyle defaultLabelStyle = new Label.LabelStyle();
        defaultLabelStyle.font = tempSkin.getFont("default");
        defaultLabelStyle.fontColor = Color.WHITE;
        tempSkin.add("default", defaultLabelStyle);

        Label.LabelStyle titleLabelStyle = new Label.LabelStyle();
        titleLabelStyle.font = tempSkin.getFont("title");
        titleLabelStyle.fontColor = Color.WHITE;
        tempSkin.add("title", titleLabelStyle);

        //the styles for the buttons
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = tempSkin.newDrawable("background", Color.DARK_GRAY);
        buttonStyle.down = tempSkin.newDrawable("background", Color.GRAY);
        buttonStyle.over = tempSkin.newDrawable("background", Color.LIGHT_GRAY);
        buttonStyle.font = tempSkin.getFont("default");
        tempSkin.add("default", buttonStyle);

        //how the text field should look like
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = tempSkin.getFont("default");
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.background = tempSkin.newDrawable("background", Color.DARK_GRAY);
        textFieldStyle.cursor = tempSkin.newDrawable("background", Color.WHITE);
        textFieldStyle.messageFontColor = Color.GRAY;
        textFieldStyle.messageFont = tempSkin.getFont("default");
        tempSkin.add("default", textFieldStyle);

        Label.LabelStyle errorLabelStyle = new Label.LabelStyle();
        errorLabelStyle.font = tempSkin.getFont("default");
        errorLabelStyle.fontColor = Color.RED;
        tempSkin.add("error", errorLabelStyle);

        return tempSkin;
    }

    @Override public void show() {
        Gdx.input.setInputProcessor(stage);
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
