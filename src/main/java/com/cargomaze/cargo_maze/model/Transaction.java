package com.cargomaze.cargo_maze.model;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import nonapi.io.github.classgraph.json.Id;

@Document
public class Transaction {
    @Id
    private String transactionId;
    private String userId;
    private String ipAddress;
    private String petitionType;
    private String uriRequest;
    private LocalDateTime timestamp;

    public Transaction(String transactionId, String userId, String ipAddress, String transactionType, String uriRequest,LocalDateTime timestamp) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.ipAddress = ipAddress;
        this.petitionType = transactionType;
        this.uriRequest = uriRequest;
        this.timestamp = timestamp;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getPetitionType() {
        return petitionType;
    }

    public void setPetitionType(String petitionType) {
        this.petitionType = petitionType;
    }

    public String getUriRequest() {
        return uriRequest;
    }

    public void setUriRequest(String uriRequest) {
        this.uriRequest = uriRequest;
    }

    
}
