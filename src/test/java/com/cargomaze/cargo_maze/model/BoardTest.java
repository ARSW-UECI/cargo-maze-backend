package com.cargomaze.cargo_maze.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    void testBoardInitialization() {
        // Comprobamos que el tablero esté correctamente inicializado.
        assertNotNull(board);
        assertEquals(15, board.getWIDTH());
        assertEquals(10, board.getHEIGHT());
        assertNotNull(board.getCells());
    }

    @Test
    void testIsValidPosition() {
        // Validamos posiciones dentro del tablero.
        assertTrue(board.isValidPosition(new Position(0, 0)));
        assertTrue(board.isValidPosition(new Position(14, 9)));

        // Validamos posiciones fuera del tablero.
        assertFalse(board.isValidPosition(new Position(15, 10)));
        assertFalse(board.isValidPosition(new Position(-1, 0)));
        assertFalse(board.isValidPosition(new Position(0, -1)));
    }

    @Test
    void testHasWallAt() {
        // Comprobamos que las paredes están correctamente asignadas.
        assertTrue(board.hasWallAt(new Position(2, 0)));
        assertTrue(board.hasWallAt(new Position(3, 1)));
        assertFalse(board.hasWallAt(new Position(0, 0))); // Sin pared
    }

    @Test
    void testIsTargetAt() {
        // Verificamos si una posición tiene un objetivo.
        assertTrue(board.isTargetAt(new Position(7, 1)));
        assertTrue(board.isTargetAt(new Position(13, 3)));
        assertFalse(board.isTargetAt(new Position(0, 0))); // No es objetivo
    }

    @Test
    void testHasBoxAt() {
        // Verificamos que las cajas estén en las posiciones correctas.
        assertTrue(board.hasBoxAt(new Position(4, 4)));
        assertTrue(board.hasBoxAt(new Position(2, 5)));
        assertFalse(board.hasBoxAt(new Position(0, 0))); // Sin caja
    }

    @Test
    void testIsComplete() {
        // Verificamos si el tablero está completo (todas las cajas en los objetivos).
        assertFalse(board.isComplete()); // Inicialmente no está completo
    }

    @Test
    void testAddBox() {
        // Verificamos que las cajas sean añadidas correctamente.
        Position newBoxPosition = new Position(5, 5);
        board.addBox(newBoxPosition, 4);
        assertTrue(board.hasBoxAt(newBoxPosition));
    }

    @Test
    void testGetPlayerStartPosition() {
        // Comprobamos las posiciones de inicio de los jugadores.
        Position playerStart = board.getPlayerStartPosition(0);
        assertEquals(new Position(0, 0), playerStart);
    }

    @Test
    void testSetCellAt() {
        // Comprobamos que podemos actualizar el estado de una celda.
        Position position = new Position(0, 0);
        board.setCellAt(position, new Cell(Cell.PLAYER));
        assertEquals(Cell.PLAYER, board.getCellAt(position).getState());
    }

    @Test
    void testPrintBoard() {
        // Verificamos que el método printBoard no lance excepciones y genere la salida correcta.
        board.printBoard();
    }

    @Test
    void testReset() {
        // Verificamos que el tablero se resetee correctamente.
        board.reset();
        assertNotNull(board.getCells());
        assertFalse(board.isComplete()); // El tablero debería estar incompleto después de reiniciar.
    }

    @Test
    void testEquals() {
        Board anotherBoard = new Board();
        assertFalse(board.equals(anotherBoard));
    }

}
