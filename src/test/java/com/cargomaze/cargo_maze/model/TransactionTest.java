package com.cargomaze.cargo_maze.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;



class TransactionTest {
    
        private Transaction transaction;
    
        @BeforeEach
        void setUp() {
            transaction = new Transaction("1", "1", "127.0.0.1", "GET", "/api/transactions", LocalDateTime.now());
        }
    
        @Test
        void testGetTransactionId() {
            assertEquals("1", transaction.getTransactionId());
        }
    
        @Test
        void testSetTransactionId() {
            transaction.setTransactionId("2");
            assertEquals("2", transaction.getTransactionId());
        }
    
        @Test
        void testGetUserId() {
            assertEquals("1", transaction.getUserId());
        }
    
        @Test
        void testSetUserId() {
            transaction.setUserId("2");
            assertEquals("2", transaction.getUserId());
        }
    
        @Test
        void testGetIpAddress() {
            assertEquals("127.0.0.1", transaction.getIpAddress());
        }
    
        @Test
        void testSetIpAddress() {
            transaction.setIpAddress("192.168.0.1");
            assertEquals("192.168.0.1", transaction.getIpAddress());
        }
    
        @Test
        void testGetPetitionType() {
            assertEquals("GET", transaction.getPetitionType());
        }
    
        @Test
        void testSetPetitionType() {
            transaction.setPetitionType("POST");
            assertEquals("POST", transaction.getPetitionType());
        }
    
        @Test
        void testGetUriRequest() {
            assertEquals("/api/transactions", transaction.getUriRequest());
        }
    
        @Test
        void testSetUriRequest() {
            transaction.setUriRequest("/api/users");
            assertEquals("/api/users", transaction.getUriRequest());
        }
    
        @Test
        void testGetTimestamp() {
            LocalDateTime now = LocalDateTime.now();
            transaction.setTimestamp(now);
            assertEquals(now, transaction.getTimestamp());
        }
    
        @Test
        void testSetTimestamp() {
            LocalDateTime now = LocalDateTime.now();
            transaction.setTimestamp(now);
            assertEquals(now, transaction.getTimestamp());
        }
    }






















































































