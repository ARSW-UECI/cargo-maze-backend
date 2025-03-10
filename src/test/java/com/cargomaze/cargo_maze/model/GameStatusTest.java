package com.cargomaze.cargo_maze.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameStatusTest {

    @Test
    void testEnumValues() {
        GameStatus[] expectedValues = {
                GameStatus.WAITING_FOR_PLAYERS,
                GameStatus.IN_PROGRESS,
                GameStatus.COMPLETED,
                GameStatus.RESETING_GAME
        };

        assertEquals(4, GameStatus.values().length, "Should have exactly 4 enum values");

        for (GameStatus status : expectedValues) {
            assertNotNull(status, "Enum value should not be null");
        }
    }

    @Test
    void testEnumNames() {
        assertEquals("WAITING_FOR_PLAYERS", GameStatus.WAITING_FOR_PLAYERS.name());
        assertEquals("IN_PROGRESS", GameStatus.IN_PROGRESS.name());
        assertEquals("COMPLETED", GameStatus.COMPLETED.name());
        assertEquals("RESETING_GAME", GameStatus.RESETING_GAME.name());
    }

    @Test
    void testValueOf() {

        assertEquals(GameStatus.WAITING_FOR_PLAYERS, GameStatus.valueOf("WAITING_FOR_PLAYERS"));
        assertEquals(GameStatus.IN_PROGRESS, GameStatus.valueOf("IN_PROGRESS"));
        assertEquals(GameStatus.COMPLETED, GameStatus.valueOf("COMPLETED"));
        assertEquals(GameStatus.RESETING_GAME, GameStatus.valueOf("RESETING_GAME"));
    }

    @Test
    void testEnumOrder() {
        assertEquals(0, GameStatus.WAITING_FOR_PLAYERS.ordinal());
        assertEquals(1, GameStatus.IN_PROGRESS.ordinal());
        assertEquals(2, GameStatus.COMPLETED.ordinal());
        assertEquals(3, GameStatus.RESETING_GAME.ordinal());
    }

    @Test
    void testEnumSerialization() {
        for (GameStatus status : GameStatus.values()) {
            assertEquals(status, GameStatus.valueOf(status.name()));
        }
    }
}