package io.github.transport_tycoon.control;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.transport_tycoon.view.MainMenuScreen;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransportTycoonTest {

    @Test
    void testCreateInitializesBatchAndMainMenu() {
        try (MockedConstruction<SpriteBatch> batchMock = mockConstruction(SpriteBatch.class);
             MockedConstruction<MainMenuScreen> menuMock = mockConstruction(MainMenuScreen.class)) {

            TransportTycoon game = spy(new TransportTycoon());
            doNothing().when(game).setScreen(any());

            game.create();

            assertNotNull(game.batch);
            assertEquals(1, batchMock.constructed().size());
            assertEquals(1, menuMock.constructed().size());
            verify(game).setScreen(any(MainMenuScreen.class));
        }
    }

    @Test
    void testDisposeDisposesBatch() {
        TransportTycoon game = new TransportTycoon();
        SpriteBatch batch = mock(SpriteBatch.class);
        game.batch = batch;

        game.dispose();

        verify(batch).dispose();
    }
}
