package com.fondo.fondo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fondo.fondo.entity.Log;
import com.fondo.fondo.service.LogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LogController.class)
class LogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LogService logService;

    @Autowired
    private ObjectMapper objectMapper;

    private Log logTest;
    private List<Log> logsTest;

    @BeforeEach
    void setUp() {
        logTest = new Log();
        logTest.setId("507f1f77bcf86cd799439011");
        logTest.setTipoOperacion("CREAR_INSCRIPCION");
        logTest.setEntidadId("507f1f77bcf86cd799439012");
        logTest.setTipoEntidad("INSCRIPCION");
        logTest.setIdCliente("507f1f77bcf86cd799439013");
        logTest.setDetalles("Inscripción creada exitosamente");
        logTest.setFechaMovimiento(LocalDateTime.now());
        logTest.setUsuario("sistema");

        Log log2 = new Log();
        log2.setId("507f1f77bcf86cd799439014");
        log2.setTipoOperacion("ELIMINAR_INSCRIPCION");
        log2.setEntidadId("507f1f77bcf86cd799439015");
        log2.setTipoEntidad("INSCRIPCION");
        log2.setIdCliente("507f1f77bcf86cd799439013");
        log2.setDetalles("Inscripción eliminada");
        log2.setFechaMovimiento(LocalDateTime.now());

        logsTest = Arrays.asList(logTest, log2);
    }

    @Test
    void testObtenerTodosLosLogs_Success() throws Exception {
        // Arrange
        when(logService.obtenerTodosLosLogs()).thenReturn(logsTest);

        // Act & Assert
        mockMvc.perform(get("/api/logs")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("507f1f77bcf86cd799439011"))
                .andExpect(jsonPath("$[0].tipoOperacion").value("CREAR_INSCRIPCION"))
                .andExpect(jsonPath("$[1].tipoOperacion").value("ELIMINAR_INSCRIPCION"));

        verify(logService, times(1)).obtenerTodosLosLogs();
    }

    @Test
    void testObtenerLogsPorCliente_Success() throws Exception {
        // Arrange
        String idCliente = "507f1f77bcf86cd799439013";
        when(logService.obtenerLogsPorCliente(idCliente)).thenReturn(logsTest);

        // Act & Assert
        mockMvc.perform(get("/api/logs/cliente/{idCliente}", idCliente)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].idCliente").value(idCliente))
                .andExpect(jsonPath("$[1].idCliente").value(idCliente));

        verify(logService, times(1)).obtenerLogsPorCliente(idCliente);
    }

    @Test
    void testObtenerLogsPorClienteYOperacion_Success() throws Exception {
        // Arrange
        String idCliente = "507f1f77bcf86cd799439013";
        String tipoOperacion = "CREAR_INSCRIPCION";
        List<Log> logsClienteOperacion = Arrays.asList(logTest);
        when(logService.obtenerLogsPorClienteYOperacion(idCliente, tipoOperacion))
                .thenReturn(logsClienteOperacion);

        // Act & Assert
        mockMvc.perform(get("/api/logs/cliente/{idCliente}/operacion/{tipoOperacion}", 
                idCliente, tipoOperacion)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].idCliente").value(idCliente))
                .andExpect(jsonPath("$[0].tipoOperacion").value(tipoOperacion));

        verify(logService, times(1)).obtenerLogsPorClienteYOperacion(idCliente, tipoOperacion);
    }

    @Test
    void testObtenerLogsInscripciones_Success() throws Exception {
        // Arrange
        when(logService.obtenerLogsInscripciones()).thenReturn(logsTest);

        // Act & Assert
        mockMvc.perform(get("/api/logs/inscripciones")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].tipoEntidad").value("INSCRIPCION"))
                .andExpect(jsonPath("$[1].tipoEntidad").value("INSCRIPCION"));

        verify(logService, times(1)).obtenerLogsInscripciones();
    }

    @Test
    void testObtenerLogsPorOperacion_Success() throws Exception {
        // Arrange
        String tipoOperacion = "CREAR_INSCRIPCION";
        List<Log> logsOperacion = Arrays.asList(logTest);
        when(logService.obtenerLogsPorTipoOperacion(tipoOperacion)).thenReturn(logsOperacion);

        // Act & Assert
        mockMvc.perform(get("/api/logs/operacion/{tipoOperacion}", tipoOperacion)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].tipoOperacion").value(tipoOperacion));

        verify(logService, times(1)).obtenerLogsPorTipoOperacion(tipoOperacion);
    }

    @Test
    void testObtenerLogsPorEntidad_Success() throws Exception {
        // Arrange
        String entidadId = "507f1f77bcf86cd799439012";
        List<Log> logsEntidad = Arrays.asList(logTest);
        when(logService.obtenerLogsPorEntidad(entidadId)).thenReturn(logsEntidad);

        // Act & Assert
        mockMvc.perform(get("/api/logs/entidad/{entidadId}", entidadId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].entidadId").value(entidadId));

        verify(logService, times(1)).obtenerLogsPorEntidad(entidadId);
    }

    @Test
    void testObtenerLogsPorClienteYFecha_Success() throws Exception {
        // Arrange
        String idCliente = "507f1f77bcf86cd799439013";
        String fechaInicio = "2025-01-01T00:00:00";
        String fechaFin = "2025-12-31T23:59:59";
        when(logService.obtenerLogsPorClienteYFecha(eq(idCliente), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(logsTest);

        // Act & Assert
        mockMvc.perform(get("/api/logs/cliente/{idCliente}/fecha", idCliente)
                .param("fechaInicio", fechaInicio)
                .param("fechaFin", fechaFin)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));

        verify(logService, times(1)).obtenerLogsPorClienteYFecha(eq(idCliente), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void testObtenerLogsPorClienteYFecha_BadRequest() throws Exception {
        // Arrange
        String idCliente = "507f1f77bcf86cd799439013";
        String fechaInicio = "fecha-invalida";
        String fechaFin = "2025-12-31T23:59:59";

        // Act & Assert
        mockMvc.perform(get("/api/logs/cliente/{idCliente}/fecha", idCliente)
                .param("fechaInicio", fechaInicio)
                .param("fechaFin", fechaFin)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(logService, never()).obtenerLogsPorClienteYFecha(anyString(), any(LocalDateTime.class), any(LocalDateTime.class));
    }
}