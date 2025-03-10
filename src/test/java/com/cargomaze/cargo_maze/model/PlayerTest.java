package com.cargomaze.cargo_maze.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlayerTest {
    private Player player;
    private Position mockPosition;

    @BeforeEach
    void setUp() {
        player = new Player("TestPlayer");
        mockPosition = mock(Position.class);
    }

    @Test
    void testConstructor() {
        assertEquals("TestPlayer", player.getNickname());
        assertFalse(player.isReady());
        assertEquals(-1, player.getIndex());
        assertNull(player.getPosition());
        assertNull(player.getGameSession());
        assertFalse(player.isLocked());
    }

    @Test
    void testSetGameSession() {
        player.setGameSession("TestSession");
        assertEquals("TestSession", player.getGameSession());
    }

    @Test
    void testSetReady() {
        assertFalse(player.isReady());

        player.setReady(true);
        assertTrue(player.isReady());

        player.setReady(false);
        assertFalse(player.isReady());
    }

    @Test
    void testUpdatePosition() {
        player.updatePosition(mockPosition);
        assertEquals(mockPosition, player.getPosition());
    }

    @Test
    void testSetIndex() {
        player.setIndex(5);
        assertEquals(5, player.getIndex());
    }

    @Test
    void testSetLocked() {
        assertFalse(player.isLocked());

        player.setLocked(true);
        assertTrue(player.isLocked());

        player.setLocked(false);
        assertFalse(player.isLocked());
    }

    @Test
    void testEquals_SameObject() {
        assertTrue(player.equals(player));
    }

    @Test
    void testEquals_SameNickname() {
        Player anotherPlayer = new Player("TestPlayer");
        assertTrue(player.equals(anotherPlayer));
    }

    @Test
    void testEquals_DifferentNickname() {
        Player differentPlayer = new Player("DifferentPlayer");
        assertFalse(player.equals(differentPlayer));
    }

    @Test
    void testEquals_DifferentType() {
        String notAPlayer = "Just a string";
        assertFalse(player.equals(notAPlayer));
    }

    @Test
    void testEquals_Null() {
        assertFalse(player.equals(null));
    }

    @Test
    void testGetters() {
        Player player = new Player("GetterTest");
        player.setGameSession("TestSession");
        player.setReady(true);
        player.setIndex(3);
        Position testPosition = mock(Position.class);
        player.updatePosition(testPosition);
        player.setLocked(true);

        assertEquals("GetterTest", player.getNickname());
        assertEquals("TestSession", player.getGameSession());
        assertTrue(player.isReady());
        assertEquals(3, player.getIndex());
        assertEquals(testPosition, player.getPosition());
        assertTrue(player.isLocked());
    }
}