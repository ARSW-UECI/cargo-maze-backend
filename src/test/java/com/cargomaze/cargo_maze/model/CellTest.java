package com.cargomaze.cargo_maze.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CellTest {

    @Test
    void testConstructorWithState() {
        Cell cell = new Cell(Cell.EMPTY);
        assertEquals(Cell.EMPTY, cell.getState());
        assertNotNull(cell.getId());
    }

    @Test
    void testSetStateBasicTransitions() {
        Cell cell = new Cell(Cell.EMPTY);

        cell.setState(Cell.WALL);
        assertEquals(Cell.WALL, cell.getState());

        cell.setState(Cell.PLAYER);
        assertEquals(Cell.PLAYER, cell.getState());

        cell.setState(Cell.BOX);
        assertEquals(Cell.BOX, cell.getState());
    }

    @Test
    void testSetStateOnTarget() {
        Cell cell = new Cell(Cell.TARGET);
        cell.setState(Cell.BOX);
        assertEquals(Cell.BOX_ON_TARGET, cell.getState());

        cell = new Cell(Cell.TARGET);
        cell.setState(Cell.PLAYER);
        assertEquals(Cell.PLAYER_ON_TARGET, cell.getState());
    }

    @Test
    void testSetStateComplexTransitions() {
        // Box on target, then empty
        Cell cell = new Cell(Cell.BOX_ON_TARGET);
        cell.setState(Cell.EMPTY);
        assertEquals(Cell.TARGET, cell.getState());

        // Player on target, then empty
        cell = new Cell(Cell.PLAYER_ON_TARGET);
        cell.setState(Cell.EMPTY);
        assertEquals(Cell.TARGET, cell.getState());
    }

    @Test
    void testLocking() {
        Cell cell = new Cell(Cell.EMPTY);

        assertFalse(cell.isLocked());

        cell.setLocked(true);
        assertTrue(cell.isLocked());

        cell.setLocked(false);
        assertFalse(cell.isLocked());
    }

    @Test
    void testEquals() {
        Cell cell1 = new Cell(Cell.EMPTY);
        Cell cell2 = new Cell(Cell.WALL);
        Cell cell3 = cell1;

        assertEquals(cell1, cell1);

        assertNotEquals(cell1, cell2);

        assertEquals(cell1, cell3);

        assertNotEquals(cell1, null);

        assertNotEquals(cell1, "Not a Cell");
    }

    @Test
    void testGetId() {
        Cell cell1 = new Cell(Cell.EMPTY);
        Cell cell2 = new Cell(Cell.EMPTY);

        assertNotEquals(cell1.getId(), cell2.getId());
    }

    @Test
    void testConstantValues() {
        String[] constants = {
                Cell.EMPTY, Cell.TARGET, Cell.WALL, Cell.PLAYER,
                Cell.BOX, Cell.BOX_ON_TARGET, Cell.PLAYER_ON_TARGET
        };

        assertEquals(constants.length, java.util.Arrays.stream(constants).distinct().count());

        for (String constant : constants) {
            assertFalse(constant.isEmpty());
            assertNotNull(constant);
        }
    }
}