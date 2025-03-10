package com.cargomaze.cargo_maze.repository;

import java.util.List;

import com.cargomaze.cargo_maze.model.Transaction;


public interface TransactionsDAL {
    
    Transaction addTransaction(Transaction transaction);

    List<Transaction> getTransactions();
    
}
