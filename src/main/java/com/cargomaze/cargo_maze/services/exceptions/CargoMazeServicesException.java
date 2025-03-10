package com.cargomaze.cargo_maze.services.exceptions;

public class CargoMazeServicesException extends Exception {
    public static final String PLAYER_DOES_NOT_BELONG_TO_SESSION = "Player not found";
    public static final String FULL_SESSION_EXCEPTION = "Session is full";
    public static final String SESSION_IS_NOT_FINISHED = "Session is not finished";
    public static final String SESSION_IS_NOT_IN_PROGRESS = "Session is not in progress";
    public static final String INVALID_NICKNAME = "Invalid nickname";

    public CargoMazeServicesException(String message) {
        super(message);
    }
    
}
