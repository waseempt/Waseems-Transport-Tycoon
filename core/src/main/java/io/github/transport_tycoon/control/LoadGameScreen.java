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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        // Clear the stage in case we are refreshing after a delete
        stage.clear();

        Table outer = new Table();
        outer.setFillParent(true);
        stage.addActor(outer);

        Label titleLabel = new Label("Save Manager", skin);
        titleLabel.setFontScale(1.5f);
        outer.add(titleLabel).padBottom(20).row();

        // The inner table that holds the grid
        Table listTable = new Table();
        listTable.defaults().pad(10); // Standard padding for all cells

        FileHandle[] saves = SaveManager.getAvailableSaves();

        if (saves.length == 0) {
            listTable.add(new Label("No save files found.", skin)).pad(20);
        } else {
            // Headers
            listTable.add(new Label("Save Name", skin)).left().width(150);
            listTable.add(new Label("Created On", skin)).left().width(150);
            listTable.add(new Label("Last Modified", skin)).left().width(150);
            listTable.add(new Label("Actions", skin)).center().colspan(2).row();

            // Date formatter
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            // Rows
            for (FileHandle saveFile : saves) {
                String createdStr = "Unknown";
                String modifiedStr = "Unknown";

                // Grab the OS-level file attributes for accurate timestamps
                try {
                    Path filePath = saveFile.file().toPath();
                    BasicFileAttributes attr = Files.readAttributes(filePath, BasicFileAttributes.class);

                    createdStr = dateFormat.format(new Date(attr.creationTime().toMillis()));
                    modifiedStr = dateFormat.format(new Date(attr.lastModifiedTime().toMillis()));
                } catch (Exception e) {
                    // Fallback to basic modification time if OS blocks deep attribute reading
                    modifiedStr = dateFormat.format(new Date(saveFile.lastModified()));
                }

                // Data
                listTable.add(new Label(saveFile.nameWithoutExtension(), skin)).left();
                listTable.add(new Label(createdStr, skin)).left();
                listTable.add(new Label(modifiedStr, skin)).left();

                // Action
                TextButton loadBtn = new TextButton("Load", skin);
                loadBtn.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        GameWorld loadedWorld = SaveManager.loadGame(saveFile.name());
                        if (loadedWorld != null) {
                            game.setScreen(new GameScreen(game, loadedWorld));
                        }
                    }
                });
                listTable.add(loadBtn).width(80).height(40).padRight(5);

                // Delete Button
                TextButton delBtn = new TextButton("Delete", skin);
                delBtn.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        // Call the confirmation dialog instead of deleting immediately!
                        showDeleteConfirmation(saveFile.name());
                    }
                });
                listTable.add(delBtn).width(80).height(40).row();
            }
        }

        // Wrap the grid in a scroll pane
        ScrollPane scrollPane = new ScrollPane(listTable, skin);
        outer.add(scrollPane).width(750).height(400).padBottom(20).row();

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        outer.add(backButton).width(200).height(50);
    }

    private void showDeleteConfirmation(String fileName) {
        Table overlay = new Table();
        overlay.setFillParent(true);
        overlay.setTouchable(com.badlogic.gdx.scenes.scene2d.Touchable.enabled);
        overlay.addListener(new ClickListener());

        Table dialog = new Table(skin);
        dialog.setBackground(skin.newDrawable("background", new Color(0.1f, 0.1f, 0.1f, 0.95f)));
        dialog.pad(30);

        Label prompt = new Label("Are you sure you want to delete '" + fileName.replace(".json", "") + "'?", skin);
        dialog.add(prompt).colspan(2).padBottom(30).row();

        TextButton yesBtn = new TextButton("Delete", skin);
        yesBtn.setColor(Color.FIREBRICK); // Tints the button red for danger!

        TextButton noBtn = new TextButton("Cancel", skin);

        yesBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (SaveManager.deleteSave(fileName)) {
                    buildUI();
                }
            }
        });

        noBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
            }
        });

        dialog.add(yesBtn).width(120).height(40).padRight(20);
        dialog.add(noBtn).width(120).height(40);

        overlay.add(dialog);
        stage.addActor(overlay);
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
