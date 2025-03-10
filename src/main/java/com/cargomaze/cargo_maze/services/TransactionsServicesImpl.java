package com.cargomaze.cargo_maze.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import com.cargomaze.cargo_maze.model.Transaction;
import com.cargomaze.cargo_maze.repository.TransactionsDAL;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class TransactionsServicesImpl implements TransacctionsServices {

    private final TransactionsDAL persistance;

    @Autowired
    public TransactionsServicesImpl(TransactionsDAL persistance) {
        this.persistance = persistance;
    }

    @Override
    public Transaction addTransaction(HttpServletRequest request, String uriPetition, String verb){
        String id = UUID.randomUUID().toString();
        
        String ipAddress = request.getRemoteAddr();
        if (request.getHeader("X-Forwarded-For") != null) {
            ipAddress = request.getHeader("X-Forwarded-For");
        }
        String userId = getPreferredUsernameFromJwtToken();

        return persistance.addTransaction(new Transaction(id, userId, ipAddress, verb, uriPetition, LocalDateTime.now()));
    }

    @Override
    public List<Transaction> getTransactions(){
        return persistance.getTransactions();
    }


    private String getPreferredUsernameFromJwtToken() {
        // Obtener el JWT del contexto de seguridad
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        
        // Obtener el claim "preferred_username"
        return jwt.getClaimAsString("preferred_username");
    }

    @Override
    public String transactionDetails(HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        if (request.getHeader("X-Forwarded-For") != null) {
            ipAddress = request.getHeader("X-Forwarded-For");
        }

        String userId = getPreferredUsernameFromJwtToken();

        return "Transaction details: " + userId + " " + ipAddress;
        
    }
    
}