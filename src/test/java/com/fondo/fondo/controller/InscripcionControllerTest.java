package com.fondo.fondo.controller;

import com.fondo.fondo.entity.Inscripcion;
import com.fondo.fondo.service.InscripcionService;
import com.fondo.fondo.dto.InscripcionCreateDto;
import com.fondo.fondo.dto.InscripcionDetalleDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InscripcionController.class)
class InscripcionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InscripcionService inscripcionService;

    private ObjectMapper objectMapper;
    private Inscripcion inscripcion;
    private InscripcionCreateDto inscripcionCreateDto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        inscripcion = new Inscripcion();
        inscripcion.setId("1");
        inscripcion.setIdCliente("cliente1");
        inscripcion.setIdProducto("producto1");
        inscripcion.setIdSucursal("sucursal1");
        inscripcion.setMontoInvertido(new BigDecimal("100000"));
        inscripcion.setFechaTransaccion(LocalDateTime.of(2025, 9, 5, 10, 30));

        inscripcionCreateDto = new InscripcionCreateDto();
        inscripcionCreateDto.setIdCliente("cliente1");
        inscripcionCreateDto.setIdProducto("producto1");
        inscripcionCreateDto.setIdSucursal("sucursal1");
        inscripcionCreateDto.setMontoInvertido(new BigDecimal("100000"));
        inscripcionCreateDto.setFechaTransaccion(LocalDateTime.of(2025, 9, 5, 10, 30));
    }

    @Test
    void testObtenerTodasLasInscripciones() throws Exception {
        // Arrange
        List<Inscripcion> inscripciones = Arrays.asList(inscripcion);
        when(inscripcionService.obtenerTodasLasInscripciones()).thenReturn(inscripciones);

        // Act & Assert
        mockMvc.perform(get("/api/inscripcion")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].idCliente").value("cliente1"));

        verify(inscripcionService).obtenerTodasLasInscripciones();
    }

    @Test
    void testObtenerInscripcionPorIdExistente() throws Exception {
        // Arrange
        when(inscripcionService.obtenerInscripcionPorId("1")).thenReturn(Optional.of(inscripcion));

        // Act & Assert
        mockMvc.perform(get("/api/inscripcion/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.idCliente").value("cliente1"));

        verify(inscripcionService).obtenerInscripcionPorId("1");
    }

    @Test
    void testObtenerInscripcionPorIdNoExistente() throws Exception {
        // Arrange
        when(inscripcionService.obtenerInscripcionPorId("999")).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/inscripcion/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(inscripcionService).obtenerInscripcionPorId("999");
    }

    @Test
    void testCrearInscripcionExitoso() throws Exception {
        // Arrange
        when(inscripcionService.crearInscripcion(any(Inscripcion.class))).thenReturn(inscripcion);

        // Act & Assert
        mockMvc.perform(post("/api/inscripcion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inscripcionCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.idCliente").value("cliente1"));

        verify(inscripcionService).crearInscripcion(any(Inscripcion.class));
    }

    @Test
    void testCrearInscripcionConError() throws Exception {
        // Arrange
        when(inscripcionService.crearInscripcion(any(Inscripcion.class)))
                .thenThrow(new RuntimeException("El producto no existe"));

        // Act & Assert
        mockMvc.perform(post("/api/inscripcion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inscripcionCreateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("El producto no existe"));

        verify(inscripcionService).crearInscripcion(any(Inscripcion.class));
    }

    @Test
    void testActualizarInscripcionExitoso() throws Exception {
        // Arrange
        when(inscripcionService.actualizarInscripcion(eq("1"), any(Inscripcion.class))).thenReturn(inscripcion);

        // Act & Assert
        mockMvc.perform(put("/api/inscripcion/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inscripcion)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.idCliente").value("cliente1"));

        verify(inscripcionService).actualizarInscripcion(eq("1"), any(Inscripcion.class));
    }

    @Test
    void testActualizarInscripcionNoExistente() throws Exception {
        // Arrange
        when(inscripcionService.actualizarInscripcion(eq("999"), any(Inscripcion.class))).thenReturn(null);

        // Act & Assert
        mockMvc.perform(put("/api/inscripcion/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inscripcion)))
                .andExpect(status().isNotFound());

        verify(inscripcionService).actualizarInscripcion(eq("999"), any(Inscripcion.class));
    }

    @Test
    void testEliminarInscripcionExitoso() throws Exception {
        // Arrange
        when(inscripcionService.eliminarInscripcion("1")).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/api/inscripcion/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(inscripcionService).eliminarInscripcion("1");
    }

    @Test
    void testEliminarInscripcionNoExistente() throws Exception {
        // Arrange
        when(inscripcionService.eliminarInscripcion("999")).thenReturn(false);

        // Act & Assert
        mockMvc.perform(delete("/api/inscripcion/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(inscripcionService).eliminarInscripcion("999");
    }

    @Test
    void testBuscarInscripcionesPorCliente() throws Exception {
        // Arrange
        List<InscripcionDetalleDto> inscripciones = Arrays.asList();
        when(inscripcionService.buscarInscripcionesDetalladasPorCliente("cliente1")).thenReturn(inscripciones);

        // Act & Assert
        mockMvc.perform(get("/api/inscripcion/cliente/cliente1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(inscripcionService).buscarInscripcionesDetalladasPorCliente("cliente1");
    }

    @Test
    void testBuscarInscripcionesPorProducto() throws Exception {
        // Arrange
        List<Inscripcion> inscripciones = Arrays.asList(inscripcion);
        when(inscripcionService.buscarInscripcionesPorProducto("producto1")).thenReturn(inscripciones);

        // Act & Assert
        mockMvc.perform(get("/api/inscripcion/producto/producto1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].idProducto").value("producto1"));

        verify(inscripcionService).buscarInscripcionesPorProducto("producto1");
    }

    @Test
    void testBuscarInscripcionesPorFechaExitoso() throws Exception {
        // Arrange
        List<Inscripcion> inscripciones = Arrays.asList(inscripcion);
        when(inscripcionService.buscarInscripcionesPorFecha(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(inscripciones);

        // Act & Assert
        mockMvc.perform(get("/api/inscripcion/fecha")
                        .param("fechaInicio", "2025-01-01T00:00:00")
                        .param("fechaFin", "2025-12-31T23:59:59")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(inscripcionService).buscarInscripcionesPorFecha(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void testBuscarInscripcionesPorFechaFormatoInvalido() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/inscripcion/fecha")
                        .param("fechaInicio", "fecha-invalida")
                        .param("fechaFin", "2025-12-31T23:59:59")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(inscripcionService, never()).buscarInscripcionesPorFecha(any(), any());
    }

    @Test
    void testExisteInscripcion() throws Exception {
        // Arrange
        when(inscripcionService.existeInscripcion("1")).thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/api/inscripcion/existe/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(inscripcionService).existeInscripcion("1");
    }

    @Test
    void testNoExisteInscripcion() throws Exception {
        // Arrange
        when(inscripcionService.existeInscripcion("999")).thenReturn(false);

        // Act & Assert
        mockMvc.perform(get("/api/inscripcion/existe/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        verify(inscripcionService).existeInscripcion("999");
    }
}