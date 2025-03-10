package com.cargomaze.cargo_maze.controller;

import com.cargomaze.cargo_maze.config.Encryption;
import com.cargomaze.cargo_maze.model.GameSession;
import com.cargomaze.cargo_maze.model.GameStatus;
import com.cargomaze.cargo_maze.model.Player;
import com.cargomaze.cargo_maze.persistance.exceptions.*;
import com.cargomaze.cargo_maze.services.exceptions.EncryptionException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.cargomaze.cargo_maze.services.CargoMazeServices;
import com.cargomaze.cargo_maze.services.TransacctionsServices;
import com.cargomaze.cargo_maze.services.exceptions.CargoMazeServicesException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/")
public class CargoMazeController {
    private static final String ERROR_KEY = "error";
    private static final String NICKNAME_KEY = "nickname";
    private static final String SESSION_ID_KEY = "sessionId";
    private static final String MESSAGE_KEY = "message";
    private static final String DATA_KEY = "data";

    private final CargoMazeServices cargoMazeServices;

    private final Encryption encryption;

    private final TransacctionsServices transactionServices;

    @Autowired
    public CargoMazeController(CargoMazeServices cargoMazeServices, Encryption encryption, TransacctionsServices transactionServices) {
        this.cargoMazeServices = cargoMazeServices;
        this.encryption = encryption;
        this.transactionServices = transactionServices;
    }

    @GetMapping(value = "cargoMaze/start", produces = MediaType.TEXT_PLAIN_VALUE)
    public String startTransaction(HttpServletRequest request) {
        return transactionServices.transactionDetails(request);
    }

    @GetMapping("/cargoMaze/resource")
    public ResponseEntity<Object> getResource(Authentication authentication) {
        Map<String, String> claims = new HashMap<>();
        claims.put("Hola", "SI");
        return new ResponseEntity<>(claims, HttpStatus.ACCEPTED);
    }


    @GetMapping()
    public ResponseEntity<Object> getWelcomeMessage() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Session controller

    /**
     * Reurns the base lobby
     * 
     * @return
     */
    @GetMapping(value = "cargoMaze/sessions/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getGameSession(@PathVariable String id, HttpServletRequest request) {
        try {
            GameSession gameSession = cargoMazeServices.getGameSession(id);
            String encryptedData = encryption.encrypt(new ObjectMapper().writeValueAsString(gameSession));
            System.out.println(transactionServices.transactionDetails(request) + " GET" + " cargoMaze/sessions/{id}");
            return ResponseEntity.ok(Map.of(DATA_KEY, encryptedData));
        } catch (CargoMazePersistanceException | EncryptionException | JsonProcessingException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(ERROR_KEY, ex.getMessage()));
        }
    } //EN

    @GetMapping(value = "cargoMaze/sessions/{id}/board/state", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getBoardState(@PathVariable String id) {
        try {
            String[][] boardState = cargoMazeServices.getBoardState(id);
            String encryptedData = encryption.encrypt(new ObjectMapper().writeValueAsString(boardState));

            return ResponseEntity.ok(Map.of(DATA_KEY, encryptedData));
        } catch (CargoMazePersistanceException | EncryptionException | JsonProcessingException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(ERROR_KEY, ex.getMessage()));
        }
    } //EN

    @GetMapping(value = "cargoMaze/sessions/{id}/state", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Object> getGameSessionState(@PathVariable String id) {
        try {
            GameStatus gameSessionState = cargoMazeServices.getGameSession(id).getStatus();
            String encryptedData = encryption.encrypt(new ObjectMapper().writeValueAsString(gameSessionState));
            return ResponseEntity.ok(Map.of(DATA_KEY, encryptedData));
        } catch ( CargoMazePersistanceException | EncryptionException | JsonProcessingException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(ERROR_KEY, ex.getMessage()));
        }
    }

    // Player controller

    @GetMapping(value = "cargoMaze/players/{nickName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getPlayer(@PathVariable String nickName,HttpServletRequest request ) {
        try {
            transactionServices.addTransaction(request, "LOGIN", "GET");
            Player player = cargoMazeServices.getPlayerById(nickName);
            String encryptedData = encryption.encrypt(new ObjectMapper().writeValueAsString(player));
            return ResponseEntity.ok(Map.of(DATA_KEY, encryptedData));
        } catch ( CargoMazePersistanceException | EncryptionException | JsonProcessingException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(ERROR_KEY, ex.getMessage()));
        }
    }

    @GetMapping(value = "cargoMaze/players", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getPlayers() {
        try {
            List<Player> players = cargoMazeServices.getPlayers();
            String encryptedData = encryption.encrypt(new ObjectMapper().writeValueAsString(players));
            return ResponseEntity.ok(Map.of(DATA_KEY, encryptedData));
        } catch (CargoMazePersistanceException | EncryptionException | JsonProcessingException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(ERROR_KEY, ex.getMessage()));
        }
    }

    /**
     * Creates a new player
     */
    @PostMapping("cargoMaze/players")
    public ResponseEntity<Object> createPlayer(@RequestBody Map<String, String> nickname, HttpSession session) {
        try {
            cargoMazeServices.createPlayer(nickname.get(NICKNAME_KEY));
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (CargoMazePersistanceException | CargoMazeServicesException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(ERROR_KEY, ex.getMessage()));
        }
    }

    @GetMapping(value = "cargoMaze/sessions/{id}/players/count", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Object> getPlayerCount(@PathVariable String id) {
        try {
            String playerCount = Integer.toString(cargoMazeServices.getPlayerCount(id));
            String encryptedData = encryption.encrypt(playerCount);

            return new ResponseEntity<>(encryptedData, HttpStatus.OK);
        } catch (CargoMazePersistanceException | EncryptionException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(ERROR_KEY, ex.getMessage()));
        }
    }

    @PutMapping("cargoMaze/sessions/{id}/players")
    public ResponseEntity<Object> addPlayerToGame(@RequestBody Map<String, String> requestBody, @PathVariable String id) {
        String nickname = requestBody.get(NICKNAME_KEY);
        if (nickname == null || nickname.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(ERROR_KEY, "Nickname is required and cannot be empty"));
        }
        try {
            cargoMazeServices.addNewPlayerToGame(nickname, id);
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(Map.of(MESSAGE_KEY, "Player added to game session", SESSION_ID_KEY, id, NICKNAME_KEY, nickname));
        } catch (CargoMazePersistanceException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(ERROR_KEY, ex.getMessage()));
        }
    }

    @GetMapping(value = "cargoMaze/sessions/{id}/players", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getPlayersInSession(@PathVariable String id) {
        try {
            List<Player> players = cargoMazeServices.getPlayersInSession(id);
            String encryptedData = encryption.encrypt(new ObjectMapper().writeValueAsString(players));
            return ResponseEntity.ok(Map.of(DATA_KEY, encryptedData));
        } catch (CargoMazePersistanceException | JsonProcessingException | EncryptionException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(ERROR_KEY, ex.getMessage()));
        }
    }

    @DeleteMapping("cargoMaze/sessions/{id}/players/{nickname}")
    public ResponseEntity<Object> removePlayerFromGame(@PathVariable String id, @PathVariable String nickname) {
        try {
            cargoMazeServices.removePlayerFromGame(nickname, id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } catch (CargoMazePersistanceException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(ERROR_KEY, ex.getMessage()));
        }
    }

    @PutMapping("cargoMaze/sessions/{id}/reset")
    public ResponseEntity<Object> resetGameSession(@PathVariable String id) {
        try {
            cargoMazeServices.resetGameSession(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(Map.of(MESSAGE_KEY, "Game session reset", SESSION_ID_KEY, id));
        } catch (CargoMazePersistanceException | CargoMazeServicesException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(ERROR_KEY, ex.getMessage()));
        }
    }

    @DeleteMapping("cargoMaze/players/{id}")
    public ResponseEntity<Object> deletePlayer(@PathVariable String id) {
        try {
            cargoMazeServices.deletePlayer(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } catch (CargoMazePersistanceException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(ERROR_KEY, ex.getMessage()));
        }
    }

    @DeleteMapping("cargoMaze/players")
    public ResponseEntity<Object> deletePlayers() {
        try {
            cargoMazeServices.deletePlayers();
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } catch (CargoMazePersistanceException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(ERROR_KEY, ex.getMessage()));
        }
    }

    @PutMapping("cargoMaze/session/{sessionId}/players")
    public ResponseEntity<Object> removePlayersFromSession(@PathVariable String sessionId) {
        try {
            cargoMazeServices.removePlayersFromSession(sessionId);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } catch (CargoMazePersistanceException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(ERROR_KEY, ex.getMessage()));
        }
    }

    @GetMapping(value = "cargoMaze/sessions/{id}/boxes/{x}/{y}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getBoxAt(@PathVariable String id, @PathVariable int x, @PathVariable int y) {
        try {
            return new ResponseEntity<>(cargoMazeServices.getBoxAt(id, x, y), HttpStatus.ACCEPTED);
        } catch (CargoMazePersistanceException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(ERROR_KEY, ex.getMessage()));
        }
    }

    @GetMapping(value = "cargoMaze/sessions/{id}/cells/{x}/{y}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getCellAt(@PathVariable String id, @PathVariable int x, @PathVariable int y) {
        try {
            return new ResponseEntity<>(cargoMazeServices.getCellAt(id, x, y), HttpStatus.ACCEPTED);
        } catch (CargoMazePersistanceException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(ERROR_KEY, ex.getMessage()));
        }
    }

    @GetMapping(value = "cargoMaze/sessions/{id}/boxes/index/{index}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getBoxAtIndex(@PathVariable String id, @PathVariable int index) {
        try {
            return new ResponseEntity<>(cargoMazeServices.getBoxAtIndex(id, index), HttpStatus.ACCEPTED);
        } catch (CargoMazePersistanceException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(ERROR_KEY, ex.getMessage()));
        }
    }

    @GetMapping(value = "cargoMaze/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getTransactions() {
        return new ResponseEntity<>(transactionServices.getTransactions(), HttpStatus.ACCEPTED);
    }
}