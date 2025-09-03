package com.fondo.fondo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("service", "Fondo de Pension API");
        response.put("version", "1.0.0");
        
        // Test MongoDB connection
        Map<String, Object> database = new HashMap<>();
        try {
            // Try to ping MongoDB
            mongoTemplate.getCollection("test").estimatedDocumentCount();
            database.put("status", "UP");
            database.put("database", "MongoDB Atlas");
            database.put("databaseName", mongoTemplate.getDb().getName());
            database.put("connected", true);
        } catch (Exception e) {
            database.put("status", "DOWN");
            database.put("connected", false);
            database.put("error", e.getMessage());
            response.put("status", "DEGRADED");
        }
        
        response.put("database", database);
        return ResponseEntity.ok(response);
    }
}