package com.cargomaze.cargo_maze.controller;

import com.cargomaze.cargo_maze.model.Position;
import com.cargomaze.cargo_maze.persistance.exceptions.CargoMazePersistanceException;
import com.cargomaze.cargo_maze.services.CargoMazeServices;
import com.cargomaze.cargo_maze.services.exceptions.CargoMazeServicesException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import java.util.Map;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CargoMazeStompControllerTest {

    @Mock
    private SimpMessagingTemplate msgt;

    @Mock
    private CargoMazeServices services;

    @InjectMocks
    private CargoMazeStompController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleGameSessionEvent() throws Exception {
        controller.handleGameSessionEvent();
        verify(msgt, times(1)).convertAndSend("/topic/sessions", true);
    }

    @Test
    void testHandleGeneralGameBoardEvent() throws Exception {
        String gameSessionId = "session1";

        controller.handleGeneralGameBoardEvent(gameSessionId);

        verify(msgt, times(1)).convertAndSend("/topic/sessions/" + gameSessionId + "/updatePlayerList", true);
        verify(msgt, times(1)).convertAndSend("/topic/sessions/" + gameSessionId + "/updateBoard", true);
    }

    @Test
    void testHandleMoveEvent_gameFinished() throws CargoMazePersistanceException, CargoMazeServicesException {
        String gameSessionId = "session1";
        Map<String, Object> elements = Map.of(
                "nickname", "player1",
                "position", Map.of("x", 1, "y", 2)
        );
        Position pos = new Position(1, 2);

        Mockito.when(services.isGameFinished(anyString())).thenReturn(true);

        controller.handleMoveEvent(gameSessionId, elements);

        verify(msgt, times(1)).convertAndSend("/topic/sessions/" + gameSessionId + "/move", false);
        verify(msgt, times(1)).convertAndSend("/topic/sessions/" + gameSessionId + "/gameWon", true);
    }

    @Test
    void testHandleMoveEvent_gameNotFinished() throws CargoMazePersistanceException, CargoMazeServicesException {
        // Arrange
        String gameSessionId = "session1";
        Map<String, Object> elements = Map.of(
                "nickname", "player1",
                "position", Map.of("x", 1, "y", 2)
        );
        Position pos = new Position(1, 2);
        Mockito.when(services.isGameFinished(anyString())).thenReturn(false);
        controller.handleMoveEvent(gameSessionId, elements);

        verify(msgt, times(1)).convertAndSend("/topic/sessions/" + gameSessionId + "/move", true);
    }

    @Test
    void testHandleWinEvent() throws Exception {
        String gameSessionId = "session1";
        String state = "won";

        controller.handleWinEvent(gameSessionId, state);
        verify(msgt, times(1)).convertAndSend("/topic/sessions/" + gameSessionId + "/gameWon", state);
    }
}
