package com.cargomaze.cargo_maze.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class GameSession {
    @Id
    private String sessionId;
    private List<Player> players;
    private GameStatus status;
    private Board board;
    private LinkedList<Integer> indexes = new LinkedList<>(Arrays.asList(0, 1, 2, 3));

    public GameSession(String sessionId) {
        this.sessionId = sessionId;
        players = new ArrayList<>();
        status = GameStatus.WAITING_FOR_PLAYERS;
        board = new Board(); //Sera una instancia inyectada (para más mapas en el futuro)
    }


    public void addPlayer(Player player) {
        player.setIndex(indexes.poll());
        player.setGameSession(sessionId);
        player.setReady(true);
        players.add(player);
        assignPlayerStartPosition(player);
    }

    public int getPlayerCount() {
        return players.size();
    }

    private void assignPlayerStartPosition(Player player) {
        Position startPosition = board.getPlayerStartPosition(player.getIndex());
        player.updatePosition(startPosition);
        board.setPlayerInBoard(startPosition);
    }


    public void startGame() {
        if (players.size() == 4 && players.stream().allMatch(Player::isReady)) {
            status = GameStatus.IN_PROGRESS;
        }
    }

    public void updateGameStatus() {
        if (board.isComplete()) {
            status = GameStatus.COMPLETED;
        }
    }

    public Player findPlayerByIndex(Player player) {
        if (player.getIndex() < 0 || player.getIndex() >= players.size()) {
            return null;  // Si el índice está fuera del rango de jugadores
        }
        return players.get(player.getIndex());
    }

    public String getSessionId() {
        return sessionId;
    }



    public List<Player> getPlayers() {
        return players;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public Board getBoard() {
        return board;
    }

    public String[][] getBoardState(){
        return board.getBoardState();
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void removePlayer(Player player) {
        board.setCellState(player.getPosition(), Cell.EMPTY); //problemas de concurrencia
        indexes.add(player.getIndex()); //problemas de concurrencia
        players.remove(player); //problemas de concurrencia

        //Se podria dejar y guardar unicamente el jugador, habria que bloquearlo completamente, entonces depronto haya que cambiarlo
        player.setIndex(-1);
        player.updatePosition(null);
        player.setGameSession(null);
        player.setReady(false);
        player.setLocked(false);
    }

    public void resetGame(){
        status = GameStatus.RESETING_GAME;
        board.reset(); 
    }

}

