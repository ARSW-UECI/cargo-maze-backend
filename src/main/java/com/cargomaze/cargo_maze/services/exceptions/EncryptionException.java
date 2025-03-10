package com.cargomaze.cargo_maze.services.exceptions;

public class EncryptionException extends Exception {
    public static final String FAILED = "Encrypt failed";

    public EncryptionException(String message) {
        super(message);
    }
}