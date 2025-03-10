package com.cargomaze.cargo_maze.repository;

import java.util.List;

import com.cargomaze.cargo_maze.model.*;
import com.cargomaze.cargo_maze.persistance.exceptions.CargoMazePersistanceException;

public interface CargoMazeDAL {

    Player addPlayer(Player player) throws CargoMazePersistanceException;

    Player getPlayer(String playerId) throws CargoMazePersistanceException;

    List<Player> getPlayers() throws CargoMazePersistanceException;

    void deletePlayer(Player player) throws CargoMazePersistanceException;

    void deletePlayers() throws CargoMazePersistanceException;


    Player getPlayerInSessionBlockingIt(String playerId, String gameSessionId) throws CargoMazePersistanceException;

    GameSession addSession(GameSession session) throws CargoMazePersistanceException;

    GameSession getSession(String gameSessionId) throws CargoMazePersistanceException;

    int getPlayerCount(String gameSessionId) throws CargoMazePersistanceException;

    List<Player> getPlayersInSession(String id) throws CargoMazePersistanceException;

    Player updatePlayerById(String playerId) throws CargoMazePersistanceException;

    Player updatePlayer(Player player) throws CargoMazePersistanceException;

    Player updatePlayerPosition(String playerId, Position position) throws CargoMazePersistanceException;

    Player updatePlayerLocked(String playerId, boolean locked) throws CargoMazePersistanceException;

    GameSession updateGameSessionById(String sessionId) throws CargoMazePersistanceException;

    GameSession updateGameSession(GameSession session) throws CargoMazePersistanceException;

    GameStatus getGameSessionStatus(String sessionId) throws CargoMazePersistanceException;

    GameSession updateGameSessionStatus(String sessionId, GameStatus status) throws CargoMazePersistanceException;

    void deletePlayer(String playerId) throws CargoMazePersistanceException;

    void removePlayerFromSession(String playerId, String sessionId) throws CargoMazePersistanceException;

    Box getBoxAt(String gameSessionId, Position boxPosition) throws CargoMazePersistanceException;

    Box getBoxAtIndex(String gameSessionId, int index) throws CargoMazePersistanceException;    

    boolean unblockBoxAtIndex(String gameSessionId, int index) throws CargoMazePersistanceException;

    boolean unBlockCellAt(String gameSessionId, int x, int y) throws CargoMazePersistanceException;

    Cell getCellAt(String gameSessionId, int x, int y) throws CargoMazePersistanceException;

    boolean updateCellStateAt(String gameSessionId, Position position, String cell) throws CargoMazePersistanceException;

    boolean updateBoxAtIndex(String gameSessionId, int index, Box box) throws CargoMazePersistanceException;
}