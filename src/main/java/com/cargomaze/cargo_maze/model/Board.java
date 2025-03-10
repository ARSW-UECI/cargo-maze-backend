package com.cargomaze.cargo_maze.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Board {

    @Id
    private String id;
    private static final int WIDTH = 15;
    private  static final int HEIGHT = 10;
    private Cell[][] cells;
    private List<Position> targetPositions;
    private List<Box> boxes;
    private List<Position> playerStartPositions;
     
    public Board() {
        this.id = UUID.randomUUID().toString();
        initializeBoard();
    }

    private void initializeBoard(){
        cells = new Cell[WIDTH][HEIGHT];
        targetPositions = new ArrayList<>();
        boxes = new ArrayList<>();
        playerStartPositions = new ArrayList<>();
        loadDefaultLayout();
    }

    private void loadDefaultLayout() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                cells[x][y] = new Cell(Cell.EMPTY);
            }
        }

        // walls

        cells[2][0] = new Cell(Cell.WALL);
        cells[3][0] = new Cell(Cell.WALL);
        cells[9][0] = new Cell(Cell.WALL);
        cells[10][0] = new Cell(Cell.WALL);
        cells[11][0] = new Cell(Cell.WALL);
        cells[12][0] = new Cell(Cell.WALL);
        cells[13][0] = new Cell(Cell.WALL);

        cells[0][1] = new Cell(Cell.WALL);
        cells[3][1] = new Cell(Cell.WALL);
        cells[10][1] = new Cell(Cell.WALL);

        cells[0][2] = new Cell(Cell.WALL);
        cells[5][2] = new Cell(Cell.WALL);

        cells[0][3] = new Cell(Cell.WALL);
        cells[1][3] = new Cell(Cell.WALL);
        cells[5][3] = new Cell(Cell.WALL);

        cells[1][4] = new Cell(Cell.WALL);
        cells[7][4] = new Cell(Cell.WALL);

        cells[7][5] = new Cell(Cell.WALL);
        cells[8][5] = new Cell(Cell.WALL);
        cells[14][5] = new Cell(Cell.WALL);

        cells[0][6] = new Cell(Cell.WALL);
        cells[6][6] = new Cell(Cell.WALL);
        cells[14][6] = new Cell(Cell.WALL);

        cells[6][7] = new Cell(Cell.WALL);
        
        cells[2][8] = new Cell(Cell.WALL);
        cells[10][8] = new Cell(Cell.WALL);
        cells[11][8] = new Cell(Cell.WALL);

        cells[2][9] = new Cell(Cell.WALL);
        cells[3][9] = new Cell(Cell.WALL);
        cells[4][9] = new Cell(Cell.WALL);
        cells[10][9] = new Cell(Cell.WALL);



        // target positions
        addTarget(new Position(7, 1));
        addTarget(new Position(13, 3));
        addTarget(new Position(7, 6));
        addTarget(new Position(8, 9));

        // boxes
        addBox(new Position(4, 4), 0);
        addBox(new Position(2, 5), 1);
        addBox(new Position(4, 6), 2);
        addBox(new Position(7, 3), 3);

        // player start positions
        playerStartPositions.add(new Position(0, 0));
        playerStartPositions.add(new Position(0, HEIGHT-1));
        playerStartPositions.add(new Position(WIDTH-1, 0));
        playerStartPositions.add(new Position(WIDTH-1, HEIGHT-1));

    }

    public boolean isValidPosition(Position position) {
        return position.getX() >= 0 && position.getX() < WIDTH &&
                position.getY() >= 0 && position.getY() < HEIGHT;
    }

    public boolean isTargetAt(Position position) {
        return targetPositions.contains(position);
    }

    public boolean hasWallAt(Position position) {
        String state= cells[position.getX()][position.getY()].getState();
        return state.equals(Cell.WALL);
    }

    public boolean hasBoxAt(Position position) {
        String state= cells[position.getX()][position.getY()].getState();
        return state.equals(Cell.BOX) || state.equals(Cell.BOX_ON_TARGET);
    }

    public boolean isPlayerAt(Position position){
        String state = cells[position.getX()][position.getY()].getState();
        return state.equals(Cell.PLAYER) || state.equals(Cell.PLAYER_ON_TARGET);
    }

    public Box getBoxAt(Position position) {
        return boxes.stream()
                .filter(box -> box.getPosition().equals(position))
                .findFirst()
                .orElse(null);
    }

    public Position getPlayerStartPosition(int playerIndex) {
        return playerStartPositions.get(playerIndex);
    }

    public boolean isComplete() {
        return boxes.stream().allMatch(Box::isAtTarget);
    }

    public void setCellAt(Position position, Cell cell) {
        cells[position.getX()][position.getY()] = cell;
    }

    private void addTarget(Position position) {
        cells[position.getX()][position.getY()] = new Cell(Cell.TARGET);
        targetPositions.add(position);
    }

    public void addBox(Position position, int index) {
        cells[position.getX()][position.getY()] = new Cell(Cell.BOX);
        Box box = new Box(UUID.randomUUID().toString(), position);
        box.setIndex(index);
        boxes.add(box);
    }

    // getters :)
    public List<Box> getBoxes() { return new ArrayList<>(boxes); }

    public void setBoxInList(Box box){
        boxes.remove(box.getIndex());
        boxes.add(box.getIndex(), box);
    }
    // printing the board :o
    public void printBoard() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                System.out.print(getCellSymbol(cells[x][y]) + " ");
            }
            System.out.println();
        }
    }


    private String getCellSymbol(Cell cell) {
        switch (cell.getState()) {
            case Cell.EMPTY: return ".";
            case Cell.WALL: return "#";
            case Cell.TARGET: return "T";
            case Cell.PLAYER: return "P";
            case Cell.BOX: return "B";
            case Cell.BOX_ON_TARGET: return "BT";
            case Cell.PLAYER_ON_TARGET: return "PT";
            default: return "?";
        }
    }

    public void setPlayerInBoard(Position position){
        cells[position.getX()][position.getY()] = new Cell(Cell.PLAYER);
    }

    public Cell getCellAt(Position position){
        return cells[position.getX()][position.getY()];
    }

    public void setCellState(Position position, String state){
        cells[position.getX()][position.getY()].setState(state);
    }



    public String[][] getBoardState(){
        String[][] boardState = new String[HEIGHT][WIDTH];
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                boardState[y][x] = getCellSymbol(cells[x][y]);
            }
        }
        return boardState;
    }

    public void reset(){
        cells = new Cell[WIDTH][HEIGHT];
        targetPositions.clear();
        boxes.clear();
        playerStartPositions.clear();
        initializeBoard();
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    public Cell[][] getCells(){
        return cells;
    }

    public String getId(){
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Board board = (Board) obj;
        return id.equals(board.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
