package com.cargomaze.cargo_maze.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import com.cargomaze.cargo_maze.model.Position;
import com.cargomaze.cargo_maze.persistance.exceptions.CargoMazePersistanceException;
import com.cargomaze.cargo_maze.services.CargoMazeServices;
import com.cargomaze.cargo_maze.services.exceptions.CargoMazeServicesException;

@Controller
public class CargoMazeStompController {

    private SimpMessagingTemplate msgt;
    private String topicUri = "/topic/sessions";
    private CargoMazeServices services;


    @Autowired
    public CargoMazeStompController (SimpMessagingTemplate msgt){
        this.msgt = msgt;
    }
    
    @Autowired
    public void setServices(CargoMazeServices services){
        this.services = services;
    }

    @MessageMapping("/sessions")
    public void handleGameSessionEvent() throws Exception {
        msgt.convertAndSend(topicUri, true);
    }

    @MessageMapping("/sessions/enterOrExitSession.{gameSessionId}")
    public void handleGeneralGameBoardEvent(@DestinationVariable String gameSessionId) throws Exception {
        msgt.convertAndSend(topicUri + "/" + gameSessionId + "/updatePlayerList", true);
        msgt.convertAndSend(topicUri + "/" + gameSessionId + "/updateBoard", true);
    }
    
    @SuppressWarnings("unchecked")
    @MessageMapping("/sessions/move.{gameSessionId}")
    public void handleMoveEvent(@DestinationVariable String gameSessionId, Map<String, Object> elements) throws CargoMazePersistanceException, CargoMazeServicesException {
        String nickname = (String) elements.get("nickname");
        Map<String, Integer> position = (Map<String, Integer>) elements.get("position");
        Position pos = new Position(position.get("x"), position.get("y"));
        try{
            services.move(nickname, gameSessionId, pos);
            if(services.isGameFinished(gameSessionId)){
                msgt.convertAndSend(topicUri + "/" + gameSessionId + "/move", false);
                msgt.convertAndSend(topicUri + "/" + gameSessionId + "/gameWon", true);
            }
            else{
                msgt.convertAndSend(topicUri + "/" + gameSessionId + "/move", true);
            }
        }
        catch (CargoMazeServicesException | CargoMazePersistanceException ex){
        }
    }

    @MessageMapping("/sessions/win.{gameSessionId}")
    public void handleWinEvent(@DestinationVariable String gameSessionId, String state) throws Exception {
        msgt.convertAndSend(topicUri + "/" + gameSessionId + "/gameWon", state);
    }
}
