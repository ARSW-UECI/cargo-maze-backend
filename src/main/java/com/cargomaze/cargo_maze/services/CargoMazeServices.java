package com.cargomaze.cargo_maze.services;

import java.util.List;

import com.cargomaze.cargo_maze.model.Box;
import com.cargomaze.cargo_maze.model.Cell;
import com.cargomaze.cargo_maze.model.GameSession;
import com.cargomaze.cargo_maze.model.Player;
import com.cargomaze.cargo_maze.model.Position;
import com.cargomaze.cargo_maze.model.Transaction;
import com.cargomaze.cargo_maze.persistance.exceptions.CargoMazePersistanceException;
import com.cargomaze.cargo_maze.services.exceptions.CargoMazeServicesException;

public interface CargoMazeServices {
    Player createPlayer(String nickname) throws CargoMazePersistanceException, CargoMazeServicesException;

    void deletePlayer(String playerId) throws CargoMazePersistanceException;

    void deletePlayers() throws CargoMazePersistanceException;

    Player getPlayerById(String playerId) throws CargoMazePersistanceException;

    List<Player> getPlayers() throws CargoMazePersistanceException;

    List<Player> getPlayersInSession(String gameSessionId) throws CargoMazePersistanceException;

    int getPlayerCount(String gameSessionId) throws CargoMazePersistanceException;

    Player addNewPlayerToGame(String nickname, String gameSessionId) throws CargoMazePersistanceException;

    Player removePlayerFromGame(String nickname, String gameSessionId) throws CargoMazePersistanceException;

    GameSession createSession(String sessionId) throws CargoMazePersistanceException;

    GameSession getGameSession(String gameSessionId) throws CargoMazePersistanceException;

    GameSession resetGameSession(String gameSessionId) throws CargoMazePersistanceException, CargoMazeServicesException;

    String[][] getBoardState(String gameSessionId) throws CargoMazePersistanceException;

    boolean move(String playerId, String gameSessionId, Position direction) throws CargoMazePersistanceException, CargoMazeServicesException;

    boolean isGameFinished(String gameSessionid) throws CargoMazePersistanceException;

    void removePlayersFromSession(String gameSessionId) throws CargoMazePersistanceException;

    Cell getCellAt(String gameSessionId, int x, int y) throws CargoMazePersistanceException;

    Box getBoxAt(String gameSessionId, int x, int y) throws CargoMazePersistanceException;

    Box getBoxAtIndex(String gameSessionId, int index) throws CargoMazePersistanceException;

}