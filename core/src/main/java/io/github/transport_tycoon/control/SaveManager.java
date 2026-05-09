package io.github.transport_tycoon.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import io.github.transport_tycoon.model.GameWorld;

public class SaveManager {

    private static final String SAVE_DIR = "saves/";

    public static void saveGame(GameWorld world, String saveName) {
        Json json = new Json();
        json.setUsePrototypes(false);

        String saveText = json.prettyPrint(world);

        FileHandle dir = Gdx.files.local(SAVE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        FileHandle file = Gdx.files.local(SAVE_DIR + saveName + ".json");
        file.writeString(saveText, false);

        System.out.println("Game saved successfully to " + file.path());
    }

    public static GameWorld loadGame(String saveName) {
        FileHandle file = Gdx.files.local(SAVE_DIR + saveName);
        if (file.exists()) {
            Json json = new Json();
            System.out.println("Game loaded successfully from " + saveName);
            return json.fromJson(GameWorld.class, file.readString());
        }
        return null;
    }

    public static FileHandle[] getAvailableSaves() {
        FileHandle dir = Gdx.files.local(SAVE_DIR);
        if (dir.exists() && dir.isDirectory()) {
            return dir.list(".json");
        }
        return new FileHandle[0]; // Return empty array if no saves exist
    }
}
