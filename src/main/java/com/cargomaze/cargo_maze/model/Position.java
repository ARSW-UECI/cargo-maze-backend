package com.cargomaze.cargo_maze.model;

import java.util.Objects;
import java.util.UUID;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Document
public class Position {
    @Id
    private String id;
    private int x;
    private int y;


    public Position(int x, int y) {
        this.x = x;
        this.y = y;
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }
    // getters :)
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isAdjacent(Position other){
        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y) == 1;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position position = (Position) o;
        return x == position.getX() && y == position.getY();
    }

    @Override
    public String toString(){
        return "(" + x + "," + y + ")";
    }

    @Override
    public int hashCode(){
        return Objects.hash(x,y);
    }
}
