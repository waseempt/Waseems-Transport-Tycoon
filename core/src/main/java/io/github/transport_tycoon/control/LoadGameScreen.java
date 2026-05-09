package io.github.transport_tycoon.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.transport_tycoon.model.GameWorld;

public class LoadGameScreen implements Screen {

    private final TransportTycoon game;
    private Stage stage;
    private Skin skin;

    public LoadGameScreen(TransportTycoon game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport(), game.batch);
        this.skin = createBasicSkin();
        Gdx.input.setInputProcessor(stage);
        buildUI();
    }

    private void buildUI() {
        Table outer = new Table();
        outer.setFillParent(true);
        stage.addActor(outer);

        Label titleLabel = new Label("Select Save File", skin);
        titleLabel.setFontScale(1.5f);
        outer.add(titleLabel).padBottom(20).row();

        Table listTable = new Table();
        FileHandle[] saves = SaveManager.getAvailableSaves();

        if (saves.length == 0) {
            listTable.add(new Label("No save files found.", skin)).pad(20);
        } else {
            for (FileHandle saveFile : saves) {
                TextButton loadBtn = new TextButton(saveFile.nameWithoutExtension(), skin);
                loadBtn.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        GameWorld loadedWorld = SaveManager.loadGame(saveFile.name());
                        if (loadedWorld != null) {
                            game.setScreen(new GameScreen(game, loadedWorld));
                        }
                    }
                });
                listTable.add(loadBtn).width(300).height(50).padBottom(10).row();
            }
        }

        ScrollPane scrollPane = new ScrollPane(listTable, skin);
        outer.add(scrollPane).width(350).height(300).padBottom(20).row();

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        outer.add(backButton).width(200).height(50);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override public void resize(int width, int height) { stage.getViewport().update(width, height, true); }
    @Override public void dispose() { stage.dispose(); skin.dispose(); }
    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    private Skin createBasicSkin() {
        Skin tempSkin = new Skin();
        tempSkin.add("default", new BitmapFont());

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.DARK_GRAY);
        pixmap.fill();
        tempSkin.add("background", new Texture(pixmap));

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = tempSkin.newDrawable("background", Color.DARK_GRAY);
        buttonStyle.down = tempSkin.newDrawable("background", Color.GRAY);
        buttonStyle.over = tempSkin.newDrawable("background", Color.LIGHT_GRAY);
        buttonStyle.font = tempSkin.getFont("default");
        tempSkin.add("default", buttonStyle);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = tempSkin.getFont("default");
        tempSkin.add("default", labelStyle);

        ScrollPane.ScrollPaneStyle scrollStyle = new ScrollPane.ScrollPaneStyle();
        tempSkin.add("default", scrollStyle);

        return tempSkin;
    }
}
