package com.cargomaze.cargo_maze.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cargomaze.cargo_maze.model.GameSession;

@Repository
public interface GameSessionRepository extends MongoRepository<GameSession, String>{
    
}
