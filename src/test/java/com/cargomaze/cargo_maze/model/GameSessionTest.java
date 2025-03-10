package com.cargomaze.cargo_maze.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameSessionTest {

    private GameSession gameSession;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;

    @BeforeEach
    void setUp() {
        gameSession = new GameSession("session1");

        player1 = new Player("player1");
        player2 = new Player("player2");
        player3 = new Player("player3");
        player4 = new Player("player4");
    }

    @Test
    void testAddPlayer() {
        gameSession.addPlayer(player1);
        assertEquals(1, gameSession.getPlayerCount());
        assertNotNull(player1.getIndex());
        assertTrue(player1.isReady());

        // Asegurarse que los demás jugadores pueden ser agregados
        gameSession.addPlayer(player2);
        assertEquals(2, gameSession.getPlayerCount());
        gameSession.addPlayer(player3);
        assertEquals(3, gameSession.getPlayerCount());
        gameSession.addPlayer(player4);
        assertEquals(4, gameSession.getPlayerCount());
    }

    @Test
    void testStartGame() {
        gameSession.addPlayer(player1);
        gameSession.addPlayer(player2);
        gameSession.addPlayer(player3);
        gameSession.addPlayer(player4);

        player1.setReady(true);
        player2.setReady(true);
        player3.setReady(true);
        player4.setReady(true);

        gameSession.startGame();
        assertEquals(GameStatus.IN_PROGRESS, gameSession.getStatus());
    }

    @Test
    void testStartGameNotEnoughPlayers() {
        gameSession.addPlayer(player1);
        gameSession.addPlayer(player2);
        gameSession.addPlayer(player3);

        gameSession.startGame();
        assertEquals(GameStatus.WAITING_FOR_PLAYERS, gameSession.getStatus());
    }

    @Test
    void testUpdateGameStatus() {
        gameSession.addPlayer(player1);
        gameSession.addPlayer(player2);
        gameSession.addPlayer(player3);
        gameSession.addPlayer(player4);

        player1.setReady(true);
        player2.setReady(true);
        player3.setReady(true);
        player4.setReady(true);

        gameSession.startGame();

        gameSession.updateGameStatus();
        assertEquals(GameStatus.IN_PROGRESS, gameSession.getStatus());
    }

    @Test
    void testFindPlayerByIndex() {
        gameSession.addPlayer(player1);
        player1.setIndex(0);
        assertEquals(player1, gameSession.findPlayerByIndex(player1));

        Player nonExistentPlayer = new Player("player");
        nonExistentPlayer.setIndex(99);
        assertNull(gameSession.findPlayerByIndex(nonExistentPlayer));
    }


    @Test
    void testRemovePlayer() {
        gameSession.addPlayer(player1);
        gameSession.addPlayer(player2);

        assertEquals(2, gameSession.getPlayerCount());

        gameSession.removePlayer(player1);
        assertEquals(1, gameSession.getPlayerCount());
        assertNull(player1.getGameSession());
        assertFalse(player1.isReady());
    }

    @Test
    void testResetGame() {
        gameSession.addPlayer(player1);
        gameSession.addPlayer(player2);
        gameSession.addPlayer(player3);
        gameSession.addPlayer(player4);

        player1.setReady(true);
        player2.setReady(true);
        player3.setReady(true);
        player4.setReady(true);

        gameSession.startGame();
        assertEquals(GameStatus.IN_PROGRESS, gameSession.getStatus());

        gameSession.resetGame();
        assertEquals(GameStatus.RESETING_GAME, gameSession.getStatus());
    }

    @Test
    void testGetBoardState() {
        String[][] boardState = gameSession.getBoardState();
        assertNotNull(boardState);
        assertEquals(10, boardState.length);
        assertEquals(15, boardState[0].length);
    }

    @Test
    void testGetSessionId() {
        assertEquals("session1", gameSession.getSessionId());
    }

    @Test
    void testSetStatus() {
        gameSession.setStatus(GameStatus.COMPLETED);
        assertEquals(GameStatus.COMPLETED, gameSession.getStatus());
    }
}

