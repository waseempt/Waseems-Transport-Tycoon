package io.github.transport_tycoon.control;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import io.github.transport_tycoon.model.GameWorld;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SaveManagerTest {

    private Files originalFiles;
    private Files files;

    @BeforeEach
    void setUp() throws Exception {
        files = mock(Files.class);
        Field field = Gdx.class.getDeclaredField("files");
        field.setAccessible(true);
        originalFiles = (Files) field.get(null);
        field.set(null, files);
    }

    @AfterEach
    void tearDown() throws Exception {
        Field field = Gdx.class.getDeclaredField("files");
        field.setAccessible(true);
        field.set(null, originalFiles);
    }

    @Test
    void testSaveGameWritesJsonFile() {
        GameWorld world = mock(GameWorld.class);
        FileHandle dir = mock(FileHandle.class);
        FileHandle file = mock(FileHandle.class);
        when(dir.exists()).thenReturn(true);
        when(files.external(".transport_tycoon/saves/")).thenReturn(dir);
        when(files.external(".transport_tycoon/saves/Test.json")).thenReturn(file);

        try (MockedConstruction<Json> jsonMock = mockConstruction(Json.class,
            (mock, context) -> when(mock.prettyPrint(world)).thenReturn("{}"))) {

            SaveManager.saveGame(world, "Test");

            verify(jsonMock.constructed().get(0)).setUsePrototypes(false);
            verify(file).writeString("{}", false);
            verify(dir, never()).mkdirs();
        }
    }

    @Test
    void testSaveGameCreatesMissingDirectory() {
        GameWorld world = mock(GameWorld.class);
        FileHandle dir = mock(FileHandle.class);
        FileHandle file = mock(FileHandle.class);
        when(dir.exists()).thenReturn(false);
        when(files.external(".transport_tycoon/saves/")).thenReturn(dir);
        when(files.external(".transport_tycoon/saves/NewSave.json")).thenReturn(file);

        try (MockedConstruction<Json> jsonMock = mockConstruction(Json.class,
            (mock, context) -> when(mock.prettyPrint(world)).thenReturn("{}"))) {

            SaveManager.saveGame(world, "NewSave");

            verify(dir).mkdirs();
            verify(file).writeString("{}", false);
        }
    }

    @Test
    void testLoadGameReturnsWorldWhenFileExists() {
        GameWorld world = mock(GameWorld.class);
        FileHandle file = mock(FileHandle.class);
        when(file.exists()).thenReturn(true);
        when(file.readString()).thenReturn("{}");
        when(files.external(".transport_tycoon/saves/Save1.json")).thenReturn(file);

        try (MockedConstruction<Json> jsonMock = mockConstruction(Json.class,
            (mock, context) -> when(mock.fromJson(GameWorld.class, "{}")).thenReturn(world))) {

            assertSame(world, SaveManager.loadGame("Save1.json"));
        }
    }

    @Test
    void testLoadGameReturnsNullWhenFileMissing() {
        FileHandle file = mock(FileHandle.class);
        when(file.exists()).thenReturn(false);
        when(files.external(".transport_tycoon/saves/Missing.json")).thenReturn(file);

        assertNull(SaveManager.loadGame("Missing.json"));
    }

    @Test
    void testGetAvailableSavesReturnsJsonFiles() {
        FileHandle dir = mock(FileHandle.class);
        FileHandle first = mock(FileHandle.class);
        FileHandle second = mock(FileHandle.class);
        FileHandle[] saves = {first, second};
        when(dir.exists()).thenReturn(true);
        when(dir.isDirectory()).thenReturn(true);
        when(dir.list(".json")).thenReturn(saves);
        when(files.external(".transport_tycoon/saves/")).thenReturn(dir);

        assertArrayEquals(saves, SaveManager.getAvailableSaves());
    }

    @Test
    void testGetAvailableSavesReturnsEmptyArrayWhenMissing() {
        FileHandle dir = mock(FileHandle.class);
        when(dir.exists()).thenReturn(false);
        when(files.external(".transport_tycoon/saves/")).thenReturn(dir);

        assertEquals(0, SaveManager.getAvailableSaves().length);
    }

    @Test
    void testDeleteSaveDeletesExistingFile() {
        FileHandle file = mock(FileHandle.class);
        when(file.exists()).thenReturn(true);
        when(file.delete()).thenReturn(true);
        when(files.external(".transport_tycoon/saves/Save1.json")).thenReturn(file);

        assertTrue(SaveManager.deleteSave("Save1.json"));
        verify(file).delete();
    }

    @Test
    void testDeleteSaveReturnsFalseWhenMissing() {
        FileHandle file = mock(FileHandle.class);
        when(file.exists()).thenReturn(false);
        when(files.external(".transport_tycoon/saves/Missing.json")).thenReturn(file);

        assertFalse(SaveManager.deleteSave("Missing.json"));
        verify(file, never()).delete();
    }
}
