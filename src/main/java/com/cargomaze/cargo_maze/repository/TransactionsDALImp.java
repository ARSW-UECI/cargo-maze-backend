package com.cargomaze.cargo_maze.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.cargomaze.cargo_maze.model.Transaction;

@Repository
public class TransactionsDALImp implements TransactionsDAL {

    private MongoTemplate mongoTemplate;
    @Autowired
    public TransactionsDALImp(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    @Override
    public List<Transaction> getTransactions(){
        return mongoTemplate.findAll(Transaction.class);
    }

    @Override
    public Transaction addTransaction(Transaction transaction){
        return mongoTemplate.save(transaction);
    }
    
}
