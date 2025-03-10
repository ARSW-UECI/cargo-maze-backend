package com.cargomaze.cargo_maze.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;

import com.cargomaze.cargo_maze.model.*;
import com.cargomaze.cargo_maze.persistance.exceptions.CargoMazePersistanceException;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.query.Criteria;

@Repository
public class CargoMazeDALImpl implements CargoMazeDAL {

    private MongoTemplate mongoTemplate;

    private static final String GAME_SESSION_ID = "sessionId";
    private static final String PLAYER_ID = "nickname";
    private static final String LOCKED_KEY = "locked";
    private static final String LOCKED_KEY_DOT = ".locked";
    private static final String GAME_SESSION_KEY = "gameSession";
    private static final String BOARD_CELLS_DOT = "board.cells.";
    private static final String BOARD_BOXES_DOT =  "board.boxes.";
    private static final String BOARD_BOXES = "$board.boxes";
    @Autowired
    public CargoMazeDALImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        GameSession baseSession = new GameSession("1");
        addSession(baseSession);
    }

    @Override
    public int getPlayerCount(String gameSessionId) throws CargoMazePersistanceException { // cambiar
        Query query = new Query(Criteria.where(GAME_SESSION_ID).is(gameSessionId));
        GameSession session = mongoTemplate.findOne(query, GameSession.class);

        if (session == null) {
            throw new CargoMazePersistanceException(CargoMazePersistanceException.GAME_SESSION_NOT_FOUND);
        }
        return session.getPlayerCount();
    }

    @Override
    public Player getPlayer(String playerId) throws CargoMazePersistanceException { // cambiar - colocar return directamente
        Query query = new Query(Criteria.where(PLAYER_ID).is(playerId));
        Player player = mongoTemplate.findOne(query, Player.class);
        if (player == null) {
            throw new CargoMazePersistanceException(CargoMazePersistanceException.PLAYER_NOT_FOUND);
        }
        return player;
    }

    @Override
    public List<Player> getPlayers() {
        return mongoTemplate.findAll(Player.class); 
    }

    @Override
    public List<Player> getPlayersInSession(String sessionId) {
        Query query = new Query(Criteria.where(GAME_SESSION_ID).is(sessionId));
        GameSession session = mongoTemplate.findOne(query, GameSession.class); // return directamente

        if (session == null) {
            throw new IllegalArgumentException("Session not found with ID: " + sessionId);
        }

        return session.getPlayers();
    }

    @Override
    public Player getPlayerInSessionBlockingIt(String sessionId, String playerId) throws CargoMazePersistanceException {
        Query lockQuery = new Query(Criteria.where(PLAYER_ID).is(playerId).and(LOCKED_KEY).is(false)); // no creo usarlo eliminar
        Update lockUpdate = new Update().set(LOCKED_KEY, true);
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(false);
        Player lockedPlayer = mongoTemplate.findAndModify(lockQuery, lockUpdate, options, Player.class);

        if (lockedPlayer == null) {
            throw new CargoMazePersistanceException(CargoMazePersistanceException.PLAYER_NOT_FOUND);
        }
        if (lockedPlayer.getGameSession() == null || !lockedPlayer.getGameSession().equals(sessionId)) {
            throw new CargoMazePersistanceException(CargoMazePersistanceException.PLAYER_NOT_IN_SESSION);
        }
        return lockedPlayer;

    }

    @Override
    public GameSession addSession(GameSession session) { // dejar, pero solo para guardar futuras sesiones
        Query query = new Query(Criteria.where(GAME_SESSION_ID).is(session.getSessionId()));
        GameSession sessionInDataBase = mongoTemplate.findOne(query, GameSession.class);
        if (sessionInDataBase == null) {
            mongoTemplate.save(session);
        }
        return sessionInDataBase;
    }

    @Override
    public GameSession getSession(String sessionId) throws CargoMazePersistanceException {
        Query query = new Query(Criteria.where(GAME_SESSION_ID).is(sessionId));
        GameSession sessionInDB = mongoTemplate.findOne(query, GameSession.class); // return directamente
        if (sessionInDB == null) {
            throw new CargoMazePersistanceException(CargoMazePersistanceException.GAME_SESSION_NOT_FOUND);
        }
        return sessionInDB;
    }

    @Override
    public GameStatus getGameSessionStatus(String sessionId) throws CargoMazePersistanceException { // cambiar
        Aggregation aggregation = Aggregation.newAggregation(
            Aggregation.match(Criteria.where("_id").is("1")),
            Aggregation.project("status").andExclude("_id") 
        );
        return mongoTemplate.aggregate(aggregation, GAME_SESSION_KEY, GameStatus.class).getUniqueMappedResult();

    }


    @Override
    public Player addPlayer(Player player) throws CargoMazePersistanceException { // quitar lo de la validacion y solo actualizar si no se encuentra
        Query query = new Query(Criteria.where(PLAYER_ID).is(player.getNickname()));
        Player playerInDataBase = mongoTemplate.findOne(query, Player.class);
        if (playerInDataBase != null) {
            throw new CargoMazePersistanceException(CargoMazePersistanceException.PLAYER_ALREADY_EXISTS);
        } else {
            return mongoTemplate.save(player);
        }
    }

    @Override
    public void deletePlayer(Player player) throws CargoMazePersistanceException { // remover directamente y no lanzar excepcion
        Query query = new Query(Criteria.where(PLAYER_ID).is(player.getNickname()));
        Player playerInDataBase = mongoTemplate.findOne(query, Player.class);
        if (playerInDataBase == null) {
            throw new CargoMazePersistanceException(CargoMazePersistanceException.PLAYER_NOT_FOUND);
        }
        mongoTemplate.remove(query, Player.class);
    }

    @Override
    public void deletePlayers() throws CargoMazePersistanceException { // dejar
        mongoTemplate.dropCollection(Player.class);
    }

    @Override
    public Player updatePlayerById(String playerId) throws CargoMazePersistanceException { // quitar 
        Query query = new Query(Criteria.where(PLAYER_ID).is(playerId));
        Player playerInDataBase = mongoTemplate.findOne(query, Player.class);
        if (playerInDataBase == null) {
            throw new CargoMazePersistanceException(CargoMazePersistanceException.PLAYER_NOT_FOUND);
        } else {
            return mongoTemplate.save(playerInDataBase);
        }
    }

    @Override
    public Player updatePlayer(Player player) throws CargoMazePersistanceException { // quitar y cambiar
        return mongoTemplate.save(player);
    }

    @Override
    public Player updatePlayerPosition(String playerId, Position newPosition) throws CargoMazePersistanceException { //dejar
        Query query = new Query(Criteria.where(PLAYER_ID).is(playerId));
        Update updatePosition = new Update().set("position", newPosition).set(LOCKED_KEY, false);
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(false);
        return mongoTemplate.findAndModify(query, updatePosition, options, Player.class);
    }

    @Override
    public Player updatePlayerLocked(String playerId, boolean locked) throws CargoMazePersistanceException{ // dejar
        Query query = new Query(Criteria.where(PLAYER_ID).is(playerId));
        Update updateLocked = new Update().set(LOCKED_KEY, locked);
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(false);
        return mongoTemplate.findAndModify(query, updateLocked, options, Player.class);
    }

    @Override
    public GameSession updateGameSessionById(String sessionId) throws CargoMazePersistanceException { //quitar
        Query query = new Query(Criteria.where(GAME_SESSION_ID).is(sessionId));
        GameSession sessionInDataBase = mongoTemplate.findOne(query, GameSession.class);
        if (sessionInDataBase == null) {
            throw new CargoMazePersistanceException(CargoMazePersistanceException.GAME_SESSION_NOT_FOUND);
        } else {
            return mongoTemplate.save(sessionInDataBase);
        }
    }

    @Override
    public GameSession updateGameSession(GameSession session) throws CargoMazePersistanceException { //quitary hacer meotod update para cada elemento en especifico
        return mongoTemplate.save(session);
    }

    @Override
    public GameSession updateGameSessionStatus(String sessionId, GameStatus status) //dejar
            throws CargoMazePersistanceException {
        Query query = new Query(Criteria.where(GAME_SESSION_ID).is(sessionId));
        Update updateStatus = new Update().set("status", status);
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(false);
        return mongoTemplate.findAndModify(query, updateStatus, options, GameSession.class);
    }

    @Override
    public void deletePlayer(String playerId) throws CargoMazePersistanceException { // quitar validacion y ponerla en services
        Query query = new Query(Criteria.where(PLAYER_ID).is(playerId));
        Player player = mongoTemplate.findOne(query, Player.class);
        if (player == null) {
            throw new CargoMazePersistanceException(CargoMazePersistanceException.PLAYER_NOT_FOUND);
        }
        mongoTemplate.remove(query, Player.class);
    }

    @Override
    public void removePlayerFromSession(String playerId, String sessionId) throws CargoMazePersistanceException { //quitar validacion y ponerla en una query
        Query query = new Query(Criteria.where(PLAYER_ID).is(playerId));
        Player player = mongoTemplate.findOne(query, Player.class);
        if (player == null) {
            throw new CargoMazePersistanceException(CargoMazePersistanceException.PLAYER_NOT_FOUND);
        }
        if (player.getGameSession() == null || !player.getGameSession().equals(sessionId)) {
            throw new CargoMazePersistanceException(CargoMazePersistanceException.PLAYER_NOT_IN_SESSION);
        }
        player.setGameSession(null);
        mongoTemplate.save(player);
    }

    @Override
    public Cell getCellAt(String gameSessionId, int x, int y) throws CargoMazePersistanceException { // dejar
        String queryString = BOARD_CELLS_DOT+x+"."+y+LOCKED_KEY_DOT;
        Query query = new Query(Criteria.where(GAME_SESSION_ID).is(gameSessionId).and(queryString).is(false)); // Filtra cajas desbloqueadas
        Update update = new Update().set(queryString, true);
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(false);
        if(mongoTemplate.findAndModify(query, update, options, GameSession.class) == null){
            throw new CargoMazePersistanceException(CargoMazePersistanceException.CELL_BLOCKED);
        }

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("_id").is(gameSessionId)),
                Aggregation.project()
                        .and(ArrayOperators.ArrayElemAt.arrayOf("$board.cells").elementAt(x)).as("cells"),
                Aggregation.project()
                        .and(ArrayOperators.ArrayElemAt.arrayOf("$cells").elementAt(y)).as("cell"),
                Aggregation.replaceRoot("cell")
        );

        AggregationResults<Cell> result = mongoTemplate.aggregate(aggregation, GAME_SESSION_KEY, Cell.class);
        Cell cell = result.getUniqueMappedResult();
        if (cell == null) {
            throw new CargoMazePersistanceException(CargoMazePersistanceException.CELL_NOT_FOUND);
        }
        return cell;
    }

    @Override
    public Box getBoxAtIndex(String gameSessionId, int index) throws CargoMazePersistanceException{ //dejar
        String queryString = BOARD_BOXES_DOT+index+LOCKED_KEY_DOT;
        Query query = new Query(Criteria.where(GAME_SESSION_ID).is(gameSessionId)
        .and(queryString).is(false)); // Filtra cajas desbloqueadas
        Update update = new Update().set(queryString, true);
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(false);

        if(mongoTemplate.findAndModify(query, update, options, GameSession.class) == null){
            throw new CargoMazePersistanceException(CargoMazePersistanceException.BOX_BLOCKED);
        }

        Aggregation aggregation = Aggregation.newAggregation(
            // Filtrar por el gameSessionId
            Aggregation.match(Criteria.where("_id").is(gameSessionId)),
            // Descomponer el array 'board.boxes'
            Aggregation.project().and(ArrayOperators.ArrayElemAt.arrayOf(BOARD_BOXES).elementAt(index)).as("box"),

            Aggregation.replaceRoot("$box")
            
        );
        // Ejecutar la consulta
        AggregationResults<Box> result = mongoTemplate.aggregate(aggregation, GAME_SESSION_KEY, Box.class);
        // Obtener el resultado único
        Box box = result.getUniqueMappedResult();
        // Si no se encuentra el box, lanzamos una excepción personalizada
        if (box == null) {
            throw new CargoMazePersistanceException(CargoMazePersistanceException.BOX_NOT_FOUND);
        }
        return box;     
    }

    @Override
    public Box getBoxAt(String gameSessionId, Position boxPosition) throws CargoMazePersistanceException { // se puede dejar
        Aggregation aggregation = Aggregation.newAggregation(
                // Filtrar por el gameSessionId
                Aggregation.match(Criteria.where("_id").is(gameSessionId)),
                // Descomponer el array 'board.boxes'
                Aggregation.unwind(BOARD_BOXES),
                // Filtrar por el boxId
                Aggregation.match(Criteria.where("board.boxes.position.x").is(boxPosition.getX())
                        .and("board.boxes.position.y").is(boxPosition.getY())),
                // Reemplazar la raíz con el objeto de la caja
                Aggregation.replaceRoot(BOARD_BOXES),

                Aggregation.match(Criteria.where(LOCKED_KEY).is(false)));

        // Ejecutar la consulta
        AggregationResults<Box> result = mongoTemplate.aggregate(aggregation, GAME_SESSION_KEY, Box.class);
        // Obtener el resultado único
        Box box = result.getUniqueMappedResult();
        // Si no se encuentra el box, lanzamos una excepción personalizada
        if (box == null) {
            throw new CargoMazePersistanceException(CargoMazePersistanceException.BOX_NOT_FOUND);
        }

        return box;
    }

    @Override
    public boolean unblockBoxAtIndex(String gameSessionId, int index) throws CargoMazePersistanceException{ //dejar
        String queryString = BOARD_BOXES_DOT+index+LOCKED_KEY_DOT;
        Query query = new Query(Criteria.where(GAME_SESSION_ID).is(gameSessionId)
        .and(queryString).is(true)); // Filtra cajas desbloqueadas
        Update update = new Update().set(queryString, false);
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(false);

        return mongoTemplate.findAndModify(query, update, options, GameSession.class) != null;
    }

    @Override 
    public boolean unBlockCellAt(String gameSessionId, int x, int y) throws CargoMazePersistanceException{ //dejar
        String queryString = BOARD_CELLS_DOT+x+"."+y+LOCKED_KEY_DOT;
        Query query = new Query(Criteria.where(GAME_SESSION_ID).is(gameSessionId).and(queryString).is(true)); // Filtra cajas desbloqueadas
        Update update = new Update().set(queryString, false);
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(false);
        return mongoTemplate.findAndModify(query, update, options, GameSession.class) != null;
    }

    @Override
    public boolean updateBoxAtIndex(String sessionId, int index, Box box){ //dejar
        Query query = new Query(Criteria.where(GAME_SESSION_ID).is(sessionId));
        Update update = new Update().set(BOARD_BOXES_DOT+index, box);
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(false);
        return mongoTemplate.findAndModify(query, update, options, GameSession.class) != null;
    }

    @Override
    public boolean updateCellStateAt(String sessionId, Position position, String state){ // dejar
        Query query = new Query(Criteria.where(GAME_SESSION_ID).is(sessionId));
        String stateQuery = BOARD_CELLS_DOT+position.getX()+"."+position.getY()+".state";
        String lockedQuery = BOARD_CELLS_DOT+position.getX()+"."+position.getY()+LOCKED_KEY_DOT;
        Update update = new Update().set(stateQuery, state).set(lockedQuery, false);
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(false);
        return mongoTemplate.findAndModify(query, update, options, GameSession.class) != null;
    }

}