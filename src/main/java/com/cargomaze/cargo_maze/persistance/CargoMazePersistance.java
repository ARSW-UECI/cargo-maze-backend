package com.cargomaze.cargo_maze.persistance;


import com.cargomaze.cargo_maze.model.GameSession;
import com.cargomaze.cargo_maze.model.Player;
import com.cargomaze.cargo_maze.persistance.exceptions.*;

import java.util.List;


public interface CargoMazePersistance {

    void addPlayer(Player player) throws CargoMazePersistanceException;

    Player getPlayer(String playerId) throws CargoMazePersistanceException;

    Player getPlayer(String playerId, String gameSession) throws CargoMazePersistanceException;

    void addSession(GameSession session) throws CargoMazePersistanceException;

    GameSession getSession(String gameSessionId) throws CargoMazePersistanceException;

    int getPlayerCount(String gameSessionId) throws CargoMazePersistanceException;

    List<Player> getPlayersInSession(String id) throws CargoMazePersistanceException;

}