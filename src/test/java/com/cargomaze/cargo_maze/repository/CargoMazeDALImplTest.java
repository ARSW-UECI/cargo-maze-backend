package com.cargomaze.cargo_maze.repository;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.cargomaze.cargo_maze.model.*;
import com.cargomaze.cargo_maze.persistance.exceptions.CargoMazePersistanceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

class CargoMazeDALImplTest {

    @Mock
    private MongoTemplate mongoTemplate;

    private CargoMazeDALImpl cargoMazeDAL;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cargoMazeDAL = new CargoMazeDALImpl(mongoTemplate);
    }
    @Test
    void testGetCellAtSuccess() throws CargoMazePersistanceException {
        String gameSessionId = "test-session";
        int x = 1, y = 2;
        Cell mockCell = new Cell("EMPTY");
        mockCell.setState("EMPTY");

        when(mongoTemplate.findAndModify(
                any(Query.class),
                any(Update.class),
                any(),
                eq(GameSession.class)
        )).thenReturn(new GameSession("gameSession"));

        AggregationResults<Cell> mockResults = Mockito.mock(AggregationResults.class);
        when(mockResults.getUniqueMappedResult()).thenReturn(mockCell);
        when(mongoTemplate.aggregate(any(Aggregation.class), eq("gameSession"), eq(Cell.class))).thenReturn(mockResults);

        Cell result = cargoMazeDAL.getCellAt(gameSessionId, x, y);

        assertNotNull(result);
        assertEquals("EMPTY", result.getState());

        verify(mongoTemplate, times(1)).findAndModify(any(Query.class), any(Update.class), any(), eq(GameSession.class));
        verify(mongoTemplate, times(1)).aggregate(any(Aggregation.class), eq("gameSession"), eq(Cell.class));
    }


    @Test
    void testGetPlayerCount_playerExists() throws CargoMazePersistanceException {
        // Arrange
        String gameSessionId = "1";
        GameSession gameSession = new GameSession(gameSessionId);
        when(mongoTemplate.findOne(any(Query.class), eq(GameSession.class))).thenReturn(gameSession);

        // Act
        int playerCount = cargoMazeDAL.getPlayerCount(gameSessionId);

        // Assert
        assertEquals(0, playerCount);
    }

    @Test
    void testGetPlayerCount_sessionNotFound() {
        // Arrange
        String gameSessionId = "1";
        when(mongoTemplate.findOne(any(Query.class), eq(GameSession.class))).thenReturn(null);

        // Act & Assert
        CargoMazePersistanceException exception = assertThrows(CargoMazePersistanceException.class, () -> {
            cargoMazeDAL.getPlayerCount(gameSessionId);
        });
        assertEquals(CargoMazePersistanceException.GAME_SESSION_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testGetPlayer_playerExists() throws CargoMazePersistanceException {
        // Arrange
        String playerId = "player1";
        Player player = new Player(playerId);
        when(mongoTemplate.findOne(any(Query.class), eq(Player.class))).thenReturn(player);

        // Act
        Player result = cargoMazeDAL.getPlayer(playerId);

        // Assert
        assertEquals(player, result);
    }

    @Test
    void testGetPlayer_playerNotFound() {
        // Arrange
        String playerId = "player1";
        when(mongoTemplate.findOne(any(Query.class), eq(Player.class))).thenReturn(null);

        // Act & Assert
        CargoMazePersistanceException exception = assertThrows(CargoMazePersistanceException.class, () -> {
            cargoMazeDAL.getPlayer(playerId);
        });
        assertEquals(CargoMazePersistanceException.PLAYER_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testAddPlayer_playerAlreadyExists() {
        // Arrange
        Player player = new Player("player1");
        when(mongoTemplate.findOne(any(Query.class), eq(Player.class))).thenReturn(player);

        // Act & Assert
        CargoMazePersistanceException exception = assertThrows(CargoMazePersistanceException.class, () -> {
            cargoMazeDAL.addPlayer(player);
        });
        assertEquals(CargoMazePersistanceException.PLAYER_ALREADY_EXISTS, exception.getMessage());
    }

    @Test
    void testAddPlayer_newPlayer() throws CargoMazePersistanceException {
        // Arrange
        Player player = new Player("player2");
        when(mongoTemplate.findOne(any(Query.class), eq(Player.class))).thenReturn(null);
        when(mongoTemplate.save(any(Player.class))).thenReturn(player);

        // Act
        Player result = cargoMazeDAL.addPlayer(player);

        // Assert
        assertEquals(player, result);
    }

    @Test
    void testUpdatePlayerPosition() throws CargoMazePersistanceException {
        // Arrange
        String playerId = "player1";
        Position newPosition = new Position(5, 10);
        Player player = new Player(playerId);
        player.updatePosition(new Position(0, 0));
        when(mongoTemplate.findOne(any(Query.class), eq(Player.class))).thenReturn(player);
        when(mongoTemplate.findAndModify(any(Query.class), any(Update.class), any(FindAndModifyOptions.class), eq(Player.class))).thenReturn(player);

        // Act
        Player updatedPlayer = cargoMazeDAL.updatePlayerPosition(playerId, newPosition);

        // Assert
        assertNotEquals(newPosition, updatedPlayer.getPosition());
    }

    @Test
    void testDeletePlayer_success() throws CargoMazePersistanceException {
        // Arrange
        String playerId = "player1";
        Player player = new Player(playerId);
        when(mongoTemplate.findOne(any(Query.class), eq(Player.class))).thenReturn(player);

        // Act
        cargoMazeDAL.deletePlayer(player);

        // Assert
        verify(mongoTemplate, times(1)).remove(any(Query.class), eq(Player.class));
    }

    @Test
    void testDeletePlayer_playerNotFound() {
        // Arrange
        String playerId = "player1";
        when(mongoTemplate.findOne(any(Query.class), eq(Player.class))).thenReturn(null);

        // Act & Assert
        CargoMazePersistanceException exception = assertThrows(CargoMazePersistanceException.class, () -> {
            cargoMazeDAL.deletePlayer(new Player(playerId));
        });
        assertEquals(CargoMazePersistanceException.PLAYER_NOT_FOUND, exception.getMessage());
    }
}
