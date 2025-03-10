package com.cargomaze.cargo_maze.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class BoxTest {

    private Box box;
    private Position position;

    @BeforeEach
    public void setUp() {
        position = mock(Position.class);
        box = new Box("box1", position);
    }

    @Test
    public void testMove() {
        Position newPosition = mock(Position.class);
        box.move(newPosition);
        assertEquals(newPosition, box.getPosition());
    }

    @Test
    public void testSetAtTarget() {
        box.setAtTarget(true);
        assertTrue(box.isAtTarget());
        box.setAtTarget(false);
        assertFalse(box.isAtTarget());
    }

    @Test
    public void testSetLocked() {
        assertFalse(box.isLocked());
        box.setLocked(true);
        assertTrue(box.isLocked());
        box.setLocked(false);
        assertFalse(box.isLocked());
    }

    @Test
    public void testGetId() {
        assertEquals("box1", box.getId());
    }

    @Test
    public void testEquals_SameObject() {
        assertTrue(box.equals(box));
    }

    @Test
    public void testEquals_DifferentObject() {
        String otherObject = "Not a box";
        assertFalse(box.equals(otherObject));
    }

    @Test
    public void testEquals_SameId() {
        Position anotherPosition = mock(Position.class);
        Box anotherBox = new Box("box1", anotherPosition);

        assertTrue(box.equals(anotherBox));
    }

    @Test
    public void testEquals_DifferentId() {
        Position anotherPosition = mock(Position.class);
        Box anotherBox = new Box("box2", anotherPosition);

        assertFalse(box.equals(anotherBox));
    }

    @Test
    public void testGetIndex() {
        assertEquals(0, box.getIndex());
        box.setIndex(5);
        assertEquals(5, box.getIndex());
    }
}
