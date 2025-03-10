package com.cargomaze.cargo_maze.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Objects;

@Document
public class Player{
    @Id
    private String nickname;
    private int index;
    private Position position;
    private boolean isReady;
    private String gameSessionId = null; // no se sabe si es necesario (si se crean servicios directos del game session en teoria no)
    private boolean locked= false;

    public Player(String nickname) {
        this.nickname = nickname;
        this.isReady = false;
        this.index = -1;
    }

    public void setGameSession(String newGameSessionId) {
        this.gameSessionId = newGameSessionId;
    }

    public void setReady(boolean ready) {
        this.isReady = ready;
    }

    public void updatePosition(Position newPosition){
        position = newPosition;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    // getters :)
    public String getNickname() {
        return nickname;
    }

    public Position getPosition() {
        return position;
    }
    public boolean isReady() {
        return isReady;
    }

    public int getIndex() {
        return index;
    }

    public String getGameSession() {
        return gameSessionId;
    }

    public void setLocked(boolean locked){
        this.locked = locked;
    }

    public boolean isLocked(){
        return locked;
    }

    @Override 
    public boolean equals(Object obj){
        if(obj == this){
            return true;
        }
        if(!(obj instanceof Player)){
            return false;
        }
        Player player = (Player) obj;
        return player.getNickname().equals(nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname);
    }
}