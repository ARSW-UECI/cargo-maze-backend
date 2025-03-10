package com.cargomaze.cargo_maze.service;

import com.cargomaze.cargo_maze.model.*;
import com.cargomaze.cargo_maze.persistance.exceptions.CargoMazePersistanceException;
import com.cargomaze.cargo_maze.repository.CargoMazeDAL;
import com.cargomaze.cargo_maze.services.CargoMazeServicesImpl;
import com.cargomaze.cargo_maze.services.exceptions.CargoMazeServicesException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CargoMazeServicesImplTest {

    @Mock
    private CargoMazeDAL persistance;

    @InjectMocks
    private CargoMazeServicesImpl cargoMazeServices;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePlayerValid() throws CargoMazePersistanceException, CargoMazeServicesException {
        String nickname = "Player1";
        Player expectedPlayer = new Player(nickname);

        when(persistance.addPlayer(any(Player.class))).thenReturn(expectedPlayer);

        Player result = cargoMazeServices.createPlayer(nickname);

        assertEquals(expectedPlayer, result);
        verify(persistance, times(1)).addPlayer(any(Player.class));
    }

    @Test
    void testCreatePlayerInvalidNickname() {
        assertThrows(CargoMazeServicesException.class, () -> {
            cargoMazeServices.createPlayer("");
        });
    }

    @Test
    void testDeletePlayer() throws CargoMazePersistanceException {
        String playerId = "Player1";
        Player player = new Player(playerId);
        GameSession session = new GameSession("Session1");
        session.addPlayer(player);

        when(persistance.getPlayer(playerId)).thenReturn(player);
        when(persistance.getSession(player.getGameSession())).thenReturn(session);

        cargoMazeServices.deletePlayer(playerId);

        verify(persistance, times(1)).deletePlayer(player);
        verify(persistance, times(1)).updateGameSession(session);
    }

    @Test
    void testDeletePlayerNotInSession() throws CargoMazePersistanceException {
        String playerId = "Player1";
        Player player = new Player(playerId);

        when(persistance.getPlayer(playerId)).thenReturn(player);

        cargoMazeServices.deletePlayer(playerId);

        verify(persistance, times(1)).deletePlayer(player);
        verify(persistance, never()).updateGameSession(any());
    }

    @Test
    void testAddNewPlayerToGameValid() throws CargoMazePersistanceException {
        String nickname = "Player1";
        String gameSessionId = "Session1";
        Player player = new Player(nickname);
        GameSession session = new GameSession(gameSessionId);

        when(persistance.getPlayer(nickname)).thenReturn(player);
        when(persistance.getSession(gameSessionId)).thenReturn(session);

        Player result = cargoMazeServices.addNewPlayerToGame(nickname, gameSessionId);

        assertNull(result);
        verify(persistance, times(1)).updateGameSession(session);
        verify(persistance, times(1)).updatePlayer(player);
    }


}
