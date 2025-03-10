package com.cargomaze.cargo_maze.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    void testConstructor() {
        Position position = new Position(3, 4);

        assertEquals(3, position.getX());
        assertEquals(4, position.getY());
        assertNotNull(position.toString());
    }
    @Test
    void testIdGeneration() {
        Position position1 = new Position(3, 4);
        Position position2 = new Position(3, 4);

        assertNotNull(position1.toString());


        assertTrue(position1.getId().matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"),
                "ID should be a valid UUID");

        assertNotEquals(position1.getId(), position2.getId(),
                "Positions with same coordinates should have unique IDs");
    }
    @Test
    void testGetters() {
        Position position = new Position(5, 7);

        assertEquals(5, position.getX());
        assertEquals(7, position.getY());
    }

    @Test
    void testIsAdjacent_AdjacentPositions() {
        // Test adjacency in all four directions
        Position center = new Position(5, 5);

        // Right
        assertTrue(center.isAdjacent(new Position(6, 5)));

        // Left
        assertTrue(center.isAdjacent(new Position(4, 5)));

        // Up
        assertTrue(center.isAdjacent(new Position(5, 6)));

        // Down
        assertTrue(center.isAdjacent(new Position(5, 4)));
    }

    @Test
    void testIsAdjacent_NonAdjacentPositions() {
        Position center = new Position(5, 5);

        // Diagonal (not adjacent)
        assertFalse(center.isAdjacent(new Position(6, 6)));

        // Far away positions
        assertFalse(center.isAdjacent(new Position(10, 5)));
        assertFalse(center.isAdjacent(new Position(5, 10)));
        assertFalse(center.isAdjacent(new Position(7, 7)));
    }

    @Test
    void testEquals_SameObject() {
        Position position = new Position(3, 4);

        assertEquals(position, position);
    }

    @Test
    void testEquals_EqualPositions() {
        Position position1 = new Position(3, 4);
        Position position2 = new Position(3, 4);

        assertEquals(position1, position2);
    }

    @Test
    void testEquals_DifferentPositions() {
        Position position1 = new Position(3, 4);
        Position position2 = new Position(5, 6);

        assertNotEquals(position1, position2);
    }

    @Test
    void testEquals_DifferentType() {
        Position position = new Position(3, 4);
        String notAPosition = "Not a position";

        assertNotEquals(position, notAPosition);
    }

    @Test
    void testEquals_Null() {
        Position position = new Position(3, 4);

        assertNotEquals(position, null);
    }

    @Test
    void testToString() {
        Position position = new Position(3, 4);

        assertEquals("(3,4)", position.toString());
    }

    @Test
    void testHashCode_SamePositions() {
        Position position1 = new Position(3, 4);
        Position position2 = new Position(3, 4);

        assertEquals(position1.hashCode(), position2.hashCode());
    }

    @Test
    void testHashCode_DifferentPositions() {
        Position position1 = new Position(3, 4);
        Position position2 = new Position(5, 6);

        assertNotEquals(position1.hashCode(), position2.hashCode());
    }

}