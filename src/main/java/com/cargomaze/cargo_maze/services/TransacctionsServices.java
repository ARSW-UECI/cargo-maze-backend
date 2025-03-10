

package com.cargomaze.cargo_maze.services;

import java.util.List;

import com.cargomaze.cargo_maze.model.Transaction;

import jakarta.servlet.http.HttpServletRequest;

public  interface TransacctionsServices {

    public List<Transaction> getTransactions();

    public Transaction addTransaction(HttpServletRequest request, String uriPetition, String verb);

    public String transactionDetails(HttpServletRequest request);

    
}