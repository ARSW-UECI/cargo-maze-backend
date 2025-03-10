package com.cargomaze.cargo_maze.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document
public class Box {
    @Id
    private String id;
    private Position position;
    private boolean isAtTarget;
    private boolean locked = false;
    private int index;
    public Box(String id, Position position) {
        this.id = id;
        this.position = position;
        this.isAtTarget = false;
    }

    public void move(Position newPosition){
        this.position = newPosition;
    }

    public void setAtTarget(boolean atTarget) {
        isAtTarget = atTarget;
    }

    // getters :)
    public String getId() {
        return id;
    }
    public Position getPosition() {
        return position;
    }
    
    public boolean isAtTarget() {
        return isAtTarget;
    }

    public void setLocked(boolean locked){
        this.locked = locked;
    }

    public boolean isLocked(){
        return locked;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object obj){
        if(obj == this){
            return true;
        }
        if(obj == null || obj.getClass() != this.getClass()){
            return false;
        }
        Box box = (Box) obj;
        return box.getId().equals(this.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}