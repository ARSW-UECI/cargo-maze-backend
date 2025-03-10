package com.cargomaze.cargo_maze.services;

import com.cargomaze.cargo_maze.model.*;
import com.cargomaze.cargo_maze.persistance.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cargomaze.cargo_maze.repository.CargoMazeDAL;
import com.cargomaze.cargo_maze.services.exceptions.CargoMazeServicesException;

import java.util.LinkedList;
import java.util.List;


@Service
public class CargoMazeServicesImpl implements CargoMazeServices {

    private final CargoMazeDAL persistance;


    @Autowired
    public CargoMazeServicesImpl(CargoMazeDAL persistance) {
        this.persistance = persistance;
    }

    @Override
    public Player createPlayer(String nickname) throws CargoMazePersistanceException, CargoMazeServicesException {
        if (nickname == null || nickname.isEmpty()) {
            throw new CargoMazeServicesException(CargoMazeServicesException.INVALID_NICKNAME);
        }

        Player player = new Player(nickname);
        return persistance.addPlayer(player);
    }

    @Override
    public void deletePlayer(String playerId) throws CargoMazePersistanceException { // CAMBIAR PARA VOLVERLO ATOMICO
        Player player = persistance.getPlayer(playerId);
        if (player.getGameSession() != null) {
            GameSession session = persistance.getSession(player.getGameSession());
            session.removePlayer(player);
            if (session.getPlayerCount() == 0) {
                if (!session.getStatus().equals(GameStatus.RESETING_GAME)) {
                    session.resetGame();
                }
                session.setStatus(GameStatus.WAITING_FOR_PLAYERS);
            }
            persistance.updateGameSession(session); // CAMBIAR Y SOLO ACTUALIZAR LOS CAMBIOS HECHOS
        }
        persistance.deletePlayer(player);
    }

    @Override
    public void deletePlayers() throws CargoMazePersistanceException {
        for (Player p : persistance.getPlayers()) {
            deletePlayer(p.getNickname());
        }
    }

    @Override
    public void removePlayersFromSession(String sessionId) throws CargoMazePersistanceException {
        GameSession session = persistance.getSession(sessionId);
        List<Player> players = session.getPlayers();
        session.setPlayers(new LinkedList<>()); // CAMBIAR PARA REMOVER LA LISTA DE MANERA ATOMICA
        for (Player p : players) {
            removePlayerFromGame(p.getNickname(), session.getSessionId());
        }

    }

    @Override
    public Player addNewPlayerToGame(String nickname, String gameSessionId) throws CargoMazePersistanceException {
        GameSession session = persistance.getSession(gameSessionId); // PODRIA DEJARSE ASI
        Player player = persistance.getPlayer(nickname);
        if (player == null) {
            throw new CargoMazePersistanceException(CargoMazePersistanceException.PLAYER_NOT_FOUND);
        }
        if (!session.getStatus().equals(GameStatus.WAITING_FOR_PLAYERS)) {
            throw new CargoMazePersistanceException("Session is not waiting for players");
        }
        if (session.getPlayers().size() >= 4) {
            throw new CargoMazePersistanceException(CargoMazePersistanceException.FULL_SESSION_EXCEPTION);
        }
        if (player.getIndex() != -1) {
            throw new CargoMazePersistanceException(CargoMazePersistanceException.PLAYER_ALREADY_IN_SESSION);
        }
        session.addPlayer(player);// habria que cambiarlo para no generar comflictos

        if (session.getPlayers().size() == 4 && session.getPlayers().stream().allMatch(Player::isReady)) {
            session.setStatus(GameStatus.IN_PROGRESS);// habria que cambiarlo para no generar comflictos
        }
        persistance.updateGameSession(session); // habria que cambiarlo para no generar comflictos
        return persistance.updatePlayer(player); // se puede dejar
    }

    @Override
    public Player removePlayerFromGame(String nickname, String gameSessionId) throws CargoMazePersistanceException {
        Player player = getPlayerById(nickname);
        GameSession session = persistance.getSession(gameSessionId);
        if (player.getGameSession().equals(gameSessionId)) {
            session.removePlayer(player);
            if (session.getPlayerCount() == 0) {
                if (!session.getStatus().equals(GameStatus.RESETING_GAME)) {
                    session.resetGame();
                }
                session.setStatus(GameStatus.WAITING_FOR_PLAYERS);
            }
            persistance.updateGameSession(session); // SE DEBE CAMBIAR PARA NO GENERAR CONFLICTOS
            return persistance.updatePlayer(player);
        } else {
            throw new CargoMazePersistanceException(CargoMazePersistanceException.PLAYER_NOT_IN_SESSION);
        }
    }

    @Override
    public GameSession resetGameSession(String gameSessionId)
            throws CargoMazePersistanceException, CargoMazeServicesException {
        GameSession session = persistance.getSession(gameSessionId);
        if (!session.getStatus().equals(GameStatus.COMPLETED)) {
            throw new CargoMazeServicesException(CargoMazeServicesException.SESSION_IS_NOT_FINISHED);
        }
        session.resetGame();
        return persistance.updateGameSession(session); // SE PUEDE DEJAR YA QUE SOLO SE HARA UNA VEZ Y N
    }

    @Override
    public List<Player> getPlayersInSession(String gameSessionId) throws CargoMazePersistanceException {
        return persistance.getPlayersInSession(gameSessionId); // CAMBIAR EN PERSISTANCE PARA QUE NO SE GENEREN
                                                               // CONFLICTOS
    }

    @Override
    public boolean isGameFinished(String gameSessionid) throws CargoMazePersistanceException {
        return persistance.getSession(gameSessionid).getStatus().equals(GameStatus.COMPLETED); // CAMBIAR POR METODO
                                                                                               // ATOMICO Y VALIDAR AQUI
    }

    @Override
    public GameSession createSession(String sessionId) throws CargoMazePersistanceException {
        GameSession session = new GameSession(sessionId);
        return persistance.addSession(session);
    }

    @Override
    public GameSession getGameSession(String gameSessionId) throws CargoMazePersistanceException {
        return persistance.getSession(gameSessionId); // NO CREO QUE DEBA USARSE
    }

    @Override
    public Player getPlayerById(String playerId) throws CargoMazePersistanceException {
        return persistance.getPlayer(playerId); // SE PUEDE USAR
    }

    @Override
    public List<Player> getPlayers() throws CargoMazePersistanceException {
        return persistance.getPlayers(); // SE PUEDE USAR
    }

    @Override
    public Cell getCellAt(String gameSessionId, int x, int y) throws CargoMazePersistanceException {
        return persistance.getCellAt(gameSessionId, x, y);
    }

    @Override
    public int getPlayerCount(String gameSessionId) throws CargoMazePersistanceException {
        return persistance.getPlayerCount(gameSessionId); // MIRAR COMO SE HACE EN PERSISTANCE
    }

    @Override
    public String[][] getBoardState(String gameSessionId) throws CargoMazePersistanceException {
        return persistance.getSession(gameSessionId).getBoardState(); // VOLVERLO ATOMICO CON UN METODO DE PERSISTANCE
    }

    @Override
    public Box getBoxAt(String gameSessionId, int x, int y) throws CargoMazePersistanceException {
        return persistance.getBoxAt(gameSessionId, new Position(x, y)); // NO SE SI SE USE
    }

    @Override
    public Box getBoxAtIndex(String gameSessionId, int index) throws CargoMazePersistanceException {
        return persistance.getBoxAtIndex(gameSessionId, index); // BIEN
    }

    @Override
    public boolean move(String playerId, String gameSessionId, Position direction)
            throws CargoMazePersistanceException, CargoMazeServicesException {
        Player player = persistance.getPlayerInSessionBlockingIt(gameSessionId, playerId);
        Board board = persistance.getSession(gameSessionId).getBoard();
        if (persistance.getSession(gameSessionId).getStatus() != GameStatus.IN_PROGRESS) {
            persistance.updatePlayerLocked(playerId, false);
            throw new CargoMazeServicesException(CargoMazeServicesException.SESSION_IS_NOT_IN_PROGRESS); // depronto
                                                                                                         // problemas
                                                                                                         // con
                                                                                                         // condicion de
                                                                                                         // carrera
        }
        Position newPosition = new Position(player.getPosition().getX() + direction.getX(),
                player.getPosition().getY() + direction.getY());
        Position currentPos = player.getPosition();
        Position boxNewPosition = null;
        int boxIndex = -1;
        if (isValidPlayerMove(currentPos, newPosition, board)) {
            boolean hasBoxAt = board.hasBoxAt(newPosition); //
            if (hasBoxAt) {
                boxNewPosition = getPositionFromMovingABox(newPosition, currentPos);
                boxIndex = board.getBoxAt(newPosition).getIndex();
                boolean moveBox = moveBox(player, newPosition, boxNewPosition, board, gameSessionId,
                        boxIndex);
                if (!moveBox) {
                    persistance.updatePlayerLocked(playerId, false);
                    return false;
                }
            }
            return movePlayer(player, newPosition, currentPos, gameSessionId);
        }
        persistance.updatePlayerLocked(playerId, false);
        return false;
    }


    private boolean movePlayer(Player player, Position newPosition, Position currentPos, String sessionId)
            throws CargoMazePersistanceException {
        try {
            Cell cell1 = getCellAt(sessionId, currentPos.getX(), currentPos.getY());
            cell1.setState(Cell.EMPTY); // AL MOMENTO DE OBTENER LA CELDA SOLO SE OBTIENE SI NO HAY UN JUGADOR O CAJA,
                                        // IMPLEMENTAR
            Cell cell2 = getCellAt(sessionId, newPosition.getX(), newPosition.getY());
            cell2.setState(Cell.PLAYER);

            persistance.updateCellStateAt(sessionId, currentPos, cell1.getState());
            persistance.updateCellStateAt(sessionId, newPosition, cell2.getState());
            persistance.updatePlayerPosition(player.getNickname(), newPosition);

        } catch (Exception e) {
            persistance.unBlockCellAt(sessionId, currentPos.getX(), currentPos.getY());
            persistance.updatePlayerLocked(player.getNickname(), false);
            return false;
        }
        persistance.updatePlayerLocked(player.getNickname(), false);
        return true;
    }

    private boolean moveBox(Player player, Position boxPosition, Position boxNewPosition, Board board, String gameSessionId, int boxIndex) throws CargoMazePersistanceException {
        try {
            return tryToMoveBox(player,  boxPosition, boxNewPosition, board, gameSessionId, boxIndex);
        } catch (Exception e) {
            persistance.updatePlayerLocked(player.getNickname(), false);
            return false;
        }
    }

    private boolean tryToMoveBox(Player player, Position boxPosition, Position boxNewPosition, Board board, String gameSessionId, int boxIndex) throws CargoMazePersistanceException{
        Box box = getBoxAtIndex(gameSessionId, boxIndex);
            if (isValidBoxMove(player, box, boxNewPosition, board)) { // va mover la caja
                try {
                    box.move(boxNewPosition); // se cambia el lugar donde esta la caja
                    if (board.isTargetAt(boxNewPosition)) {
                        box.setAtTarget(true);
                        boolean allOtherBoxesAtTarget = board.getBoxes().stream()
                                .filter(b -> !b.equals(box))
                                .allMatch(Box::isAtTarget);
                        if (allOtherBoxesAtTarget) {
                            persistance.updateGameSessionStatus(gameSessionId, GameStatus.COMPLETED);
                        }
                    } else if (board.isTargetAt(boxPosition)) {
                        box.setAtTarget(false);
                    }
                    Cell cell2 = getCellAt(gameSessionId, boxPosition.getX(), boxPosition.getY());
                    cell2.setState(Cell.EMPTY);
                    Cell cell1 = getCellAt(gameSessionId, boxNewPosition.getX(), boxNewPosition.getY());
                    cell1.setState(Cell.BOX); // SI NO HAY UN JUGADOR O CAJA O WALL
                    box.setLocked(false);
                    persistance.updateBoxAtIndex(gameSessionId, boxIndex, box);
                    persistance.updateCellStateAt(gameSessionId, boxNewPosition, cell1.getState());
                    persistance.updateCellStateAt(gameSessionId, boxPosition, cell2.getState());
                } catch (Exception e) {
                    persistance.unblockBoxAtIndex(gameSessionId, boxIndex);
                    persistance.unBlockCellAt(gameSessionId, boxPosition.getX(), boxPosition.getY()); 
                    return false;
                }
                return true;
            }
            persistance.unblockBoxAtIndex(gameSessionId, boxIndex);
            return false;
    }

    private boolean isValidPlayerMove(Position currentPosition, Position newPosition, Board board) {
        return currentPosition.isAdjacent(newPosition) && board.isValidPosition(newPosition)
                && !board.hasWallAt(newPosition) && !board.isPlayerAt(newPosition); // aquí se presenta el problema de
                                                                                    // condicion de carrera
    } // preguntar a la session si hay un jugador en esa posicion,

    private Position getPositionFromMovingABox(Position boxPosition, Position playerPosition) {
        // Eje y del jugador es menor al de la caja
        if (playerPosition.getY() < boxPosition.getY()) {
            return new Position(boxPosition.getX(), boxPosition.getY() + 1);
        }
        // Eje y del jugador es mayor al de la caja
        else if (playerPosition.getY() > boxPosition.getY()) {
            return new Position(boxPosition.getX(), boxPosition.getY() - 1);
        }
        // Eje x del jugador es menor al de la caja
        else if (playerPosition.getX() < boxPosition.getX()) {
            return new Position(boxPosition.getX() + 1, boxPosition.getY());
        }
        return new Position(boxPosition.getX() - 1, boxPosition.getY());
    }

    private boolean isValidBoxMove(Player player, Box box, Position newPosition, Board board) {
        return player.getPosition().isAdjacent(box.getPosition()) &&
                board.isValidPosition(newPosition) &&
                !board.hasWallAt(newPosition) &&
                !board.hasBoxAt(newPosition) &&
                !board.isPlayerAt(newPosition); // aquí se presenta el problema de condicion de carrera
    }


}
