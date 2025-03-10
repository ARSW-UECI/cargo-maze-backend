package com.cargomaze.cargo_maze.persistance.exceptions;

public class CargoMazePersistanceException extends Exception {

    public static final String FULL_SESSION_EXCEPTION = "The session is full";
    public static final String GAME_SESSION_ALREADY_EXISTS = "The game session already exists";
    public static final String GAME_SESSION_NOT_FOUND = "The game session was not found";
    public static final String INVALID_NICKNAME_EXCEPTION = "The nickname is invalid";
    public static final String PLAYER_ALREADY_EXISTS = "The player already exists";
    public static final String PLAYER_NOT_FOUND = "The player was not found";
    public static final String PLAYER_NOT_IN_SESSION = "The player is not in the session";
    public static final String PLAYER_ALREADY_IN_SESSION = "The player is already in a session";
    public static final String BOX_NOT_FOUND = "The box was not found";
    public static final String BOXES_NOT_FOUND = "The boxes were not found";
    public static final String CELL_NOT_FOUND = "The cell was not found";
    public static final String FAILED_TRANSACTION = "The transaction failed";
    public static final String CELL_BLOCKED = "The cell is blocked";
    public static final String BOX_BLOCKED = "The box is blocked";
    public static final String PLAYER_BLOCKED = "The player is blocked";

    public CargoMazePersistanceException(String message) {
        super(message);
    }

    public CargoMazePersistanceException(String message, Throwable cause) {
        super(message, cause);
    }
}