package com.fondo.fondo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Health", description = "API para verificar el estado del sistema")
public class HealthController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/health")
    @Operation(summary = "Verificar estado del sistema", description = "Retorna el estado de salud de la aplicación y la conexión a MongoDB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sistema funcionando correctamente"),
            @ApiResponse(responseCode = "500", description = "Error en el sistema")
    })
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Verificar conexión a MongoDB
            mongoTemplate.getDb().runCommand(new org.bson.Document("ping", 1));
            
            response.put("status", "UP");
            response.put("database", "MongoDB - Connected");
            response.put("timestamp", java.time.LocalDateTime.now());
            response.put("application", "Fondo de Pensión API");
            response.put("version", "1.0.0");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "DOWN");
            response.put("database", "MongoDB - Connection Failed");
            response.put("error", e.getMessage());
            response.put("timestamp", java.time.LocalDateTime.now());
            
            return ResponseEntity.status(500).body(response);
        }
    }
}