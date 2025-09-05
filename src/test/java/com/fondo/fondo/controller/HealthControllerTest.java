package com.fondo.fondo.controller;

import com.mongodb.MongoException;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class HealthControllerTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private com.mongodb.client.MongoDatabase mongoDatabase;

    @InjectMocks
    private HealthController healthController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(healthController).build();
    }

    @Test
    void testHealth_Success() throws Exception {
        // Arrange
        when(mongoTemplate.getDb()).thenReturn(mongoDatabase);
        when(mongoDatabase.runCommand(any(Document.class))).thenReturn(new Document("ok", 1));

        // Act
        ResponseEntity<Map<String, Object>> response = healthController.health();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertEquals("UP", body.get("status"));
        assertEquals("MongoDB - Connected", body.get("database"));
        assertEquals("Fondo de Pensión API", body.get("application"));
        assertEquals("1.0.0", body.get("version"));
        assertNotNull(body.get("timestamp"));
        
        verify(mongoTemplate, times(1)).getDb();
        verify(mongoDatabase, times(1)).runCommand(any(Document.class));
    }

    @Test
    void testHealth_DatabaseConnectionFailure() {
        // Arrange
        when(mongoTemplate.getDb()).thenReturn(mongoDatabase);
        when(mongoDatabase.runCommand(any(Document.class)))
            .thenThrow(new MongoException("Connection failed"));

        // Act
        ResponseEntity<Map<String, Object>> response = healthController.health();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertEquals("DOWN", body.get("status"));
        assertEquals("MongoDB - Connection Failed", body.get("database"));
        assertEquals("Connection failed", body.get("error"));
        assertNotNull(body.get("timestamp"));
        
        verify(mongoTemplate, times(1)).getDb();
        verify(mongoDatabase, times(1)).runCommand(any(Document.class));
    }

    @Test
    void testHealth_MongoTemplateGetDbFailure() {
        // Arrange
        when(mongoTemplate.getDb()).thenThrow(new RuntimeException("Database access failed"));

        // Act
        ResponseEntity<Map<String, Object>> response = healthController.health();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> body = response.getBody();
        assertEquals("DOWN", body.get("status"));
        assertEquals("MongoDB - Connection Failed", body.get("database"));
        assertEquals("Database access failed", body.get("error"));
        assertNotNull(body.get("timestamp"));
        
        verify(mongoTemplate, times(1)).getDb();
    }

    @Test
    void testHealthEndpoint_Integration() throws Exception {
        // Arrange
        when(mongoTemplate.getDb()).thenReturn(mongoDatabase);
        when(mongoDatabase.runCommand(any(Document.class))).thenReturn(new Document("ok", 1));

        // Act & Assert
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.database").value("MongoDB - Connected"))
                .andExpect(jsonPath("$.application").value("Fondo de Pensión API"))
                .andExpect(jsonPath("$.version").value("1.0.0"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void testHealthEndpoint_DatabaseError_Integration() throws Exception {
        // Arrange
        when(mongoTemplate.getDb()).thenReturn(mongoDatabase);
        when(mongoDatabase.runCommand(any(Document.class)))
            .thenThrow(new MongoException("Connection timeout"));

        // Act & Assert
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.status").value("DOWN"))
                .andExpect(jsonPath("$.database").value("MongoDB - Connection Failed"))
                .andExpect(jsonPath("$.error").value("Connection timeout"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void testHealthResponseStructure() {
        // Arrange
        when(mongoTemplate.getDb()).thenReturn(mongoDatabase);
        when(mongoDatabase.runCommand(any(Document.class))).thenReturn(new Document("ok", 1));

        // Act
        ResponseEntity<Map<String, Object>> response = healthController.health();

        // Assert
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        
        // Verificar que contiene todas las claves esperadas
        assertTrue(body.containsKey("status"));
        assertTrue(body.containsKey("database"));
        assertTrue(body.containsKey("timestamp"));
        assertTrue(body.containsKey("application"));
        assertTrue(body.containsKey("version"));
        
        // Verificar tipos de datos
        assertTrue(body.get("status") instanceof String);
        assertTrue(body.get("database") instanceof String);
        assertTrue(body.get("application") instanceof String);
        assertTrue(body.get("version") instanceof String);
    }
}