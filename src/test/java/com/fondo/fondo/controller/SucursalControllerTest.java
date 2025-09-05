package com.fondo.fondo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fondo.fondo.dto.SucursalCreateDto;
import com.fondo.fondo.entity.Sucursal;
import com.fondo.fondo.service.SucursalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SucursalController.class)
class SucursalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SucursalService sucursalService;

    @Autowired
    private ObjectMapper objectMapper;

    private Sucursal sucursalTest;
    private List<Sucursal> sucursalesTest;

    @BeforeEach
    void setUp() {
        sucursalTest = new Sucursal();
        sucursalTest.setId("507f1f77bcf86cd799439011");
        sucursalTest.setNombre("Sucursal Centro");
        sucursalTest.setCiudad("Bogotá");

        Sucursal sucursal2 = new Sucursal();
        sucursal2.setId("507f1f77bcf86cd799439012");
        sucursal2.setNombre("Sucursal Norte");
        sucursal2.setCiudad("Medellín");

        sucursalesTest = Arrays.asList(sucursalTest, sucursal2);
    }

    // ========== TESTS PARA GET /api/sucursal ==========

    @Test
    void testObtenerTodasLasSucursales_Success() throws Exception {
        // Arrange
        when(sucursalService.obtenerTodasLasSucursales()).thenReturn(sucursalesTest);

        // Act & Assert
        mockMvc.perform(get("/api/sucursal"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Sucursal Centro"))
                .andExpect(jsonPath("$[1].nombre").value("Sucursal Norte"));

        verify(sucursalService, times(1)).obtenerTodasLasSucursales();
    }

    @Test
    void testObtenerTodasLasSucursales_EmptyList() throws Exception {
        // Arrange
        when(sucursalService.obtenerTodasLasSucursales()).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/sucursal"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(sucursalService, times(1)).obtenerTodasLasSucursales();
    }

    // ========== TESTS PARA GET /api/sucursal/{id} ==========

    @Test
    void testObtenerSucursalPorId_Success() throws Exception {
        // Arrange
        when(sucursalService.obtenerSucursalPorId("507f1f77bcf86cd799439011"))
                .thenReturn(Optional.of(sucursalTest));

        // Act & Assert
        mockMvc.perform(get("/api/sucursal/507f1f77bcf86cd799439011"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("507f1f77bcf86cd799439011"))
                .andExpect(jsonPath("$.nombre").value("Sucursal Centro"))
                .andExpect(jsonPath("$.ciudad").value("Bogotá"));

        verify(sucursalService, times(1)).obtenerSucursalPorId("507f1f77bcf86cd799439011");
    }

    @Test
    void testObtenerSucursalPorId_NotFound() throws Exception {
        // Arrange
        when(sucursalService.obtenerSucursalPorId("507f1f77bcf86cd799439011"))
                .thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/sucursal/507f1f77bcf86cd799439011"))
                .andExpect(status().isNotFound());

        verify(sucursalService, times(1)).obtenerSucursalPorId("507f1f77bcf86cd799439011");
    }

    // ========== TESTS PARA POST /api/sucursal ==========

    @Test
    void testCrearSucursal_Success() throws Exception {
        // Arrange
        SucursalCreateDto sucursalDto = new SucursalCreateDto();
        sucursalDto.setNombre("Sucursal Centro");
        sucursalDto.setCiudad("Bogotá");

        when(sucursalService.crearSucursal(any(Sucursal.class))).thenReturn(sucursalTest);

        // Act & Assert
        mockMvc.perform(post("/api/sucursal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sucursalDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("Sucursal Centro"))
                .andExpect(jsonPath("$.ciudad").value("Bogotá"));

        verify(sucursalService, times(1)).crearSucursal(any(Sucursal.class));
    }

    @Test
    void testCrearSucursal_InvalidData() throws Exception {
        // Arrange
        SucursalCreateDto sucursalDto = new SucursalCreateDto();
        sucursalDto.setNombre(""); // Nombre vacío para provocar error de validación
        sucursalDto.setCiudad("Bogotá");

        // Act & Assert
        mockMvc.perform(post("/api/sucursal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sucursalDto)))
                .andExpect(status().isBadRequest());

        verify(sucursalService, never()).crearSucursal(any(Sucursal.class));
    }

    @Test
    void testCrearSucursal_ServiceException() throws Exception {
        // Arrange
        SucursalCreateDto sucursalDto = new SucursalCreateDto();
        sucursalDto.setNombre("Sucursal Centro");
        sucursalDto.setCiudad("Bogotá");

        when(sucursalService.crearSucursal(any(Sucursal.class)))
                .thenThrow(new RuntimeException("Error interno"));

        // Act & Assert
        mockMvc.perform(post("/api/sucursal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sucursalDto)))
                .andExpect(status().isBadRequest());

        verify(sucursalService, times(1)).crearSucursal(any(Sucursal.class));
    }

    // ========== TESTS PARA PUT /api/sucursal/{id} ==========

    @Test
    void testActualizarSucursal_Success() throws Exception {
        // Arrange
        Sucursal sucursalActualizada = new Sucursal();
        sucursalActualizada.setId("507f1f77bcf86cd799439011");
        sucursalActualizada.setNombre("Sucursal Centro Actualizada");
        sucursalActualizada.setCiudad("Bogotá");

        when(sucursalService.actualizarSucursal(eq("507f1f77bcf86cd799439011"), any(Sucursal.class)))
                .thenReturn(sucursalActualizada);

        // Act & Assert
        mockMvc.perform(put("/api/sucursal/507f1f77bcf86cd799439011")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sucursalActualizada)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("Sucursal Centro Actualizada"))
                .andExpect(jsonPath("$.ciudad").value("Bogotá"));

        verify(sucursalService, times(1)).actualizarSucursal(eq("507f1f77bcf86cd799439011"), any(Sucursal.class));
    }

    @Test
    void testActualizarSucursal_NotFound() throws Exception {
        // Arrange
        when(sucursalService.actualizarSucursal(eq("507f1f77bcf86cd799439011"), any(Sucursal.class)))
                .thenThrow(new RuntimeException("Sucursal no encontrada"));

        // Act & Assert
        mockMvc.perform(put("/api/sucursal/507f1f77bcf86cd799439011")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sucursalTest)))
                .andExpect(status().isNotFound());

        verify(sucursalService, times(1)).actualizarSucursal(eq("507f1f77bcf86cd799439011"), any(Sucursal.class));
    }

    @Test
    void testActualizarSucursal_InvalidData() throws Exception {
        // Arrange
        Sucursal sucursalInvalida = new Sucursal();
        sucursalInvalida.setNombre(""); // Nombre vacío
        sucursalInvalida.setCiudad("Bogotá");

        // Act & Assert
        mockMvc.perform(put("/api/sucursal/507f1f77bcf86cd799439011")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sucursalInvalida)))
                .andExpect(status().isBadRequest());

        verify(sucursalService, never()).actualizarSucursal(anyString(), any(Sucursal.class));
    }

    // ========== TESTS PARA DELETE /api/sucursal/{id} ==========

    @Test
    void testEliminarSucursal_Success() throws Exception {
        // Arrange
        doNothing().when(sucursalService).eliminarSucursal("507f1f77bcf86cd799439011");

        // Act & Assert
        mockMvc.perform(delete("/api/sucursal/507f1f77bcf86cd799439011"))
                .andExpect(status().isNoContent());

        verify(sucursalService, times(1)).eliminarSucursal("507f1f77bcf86cd799439011");
    }

    @Test
    void testEliminarSucursal_NotFound() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Sucursal no encontrada"))
                .when(sucursalService).eliminarSucursal("507f1f77bcf86cd799439011");

        // Act & Assert
        mockMvc.perform(delete("/api/sucursal/507f1f77bcf86cd799439011"))
                .andExpect(status().isNotFound());

        verify(sucursalService, times(1)).eliminarSucursal("507f1f77bcf86cd799439011");
    }

    // ========== TESTS PARA GET /api/sucursal/nombre/{nombre} ==========

    @Test
    void testBuscarSucursalesPorNombre_Success() throws Exception {
        // Arrange
        when(sucursalService.buscarSucursalesPorNombre("Centro"))
                .thenReturn(Arrays.asList(sucursalTest));

        // Act & Assert
        mockMvc.perform(get("/api/sucursal/nombre/Centro"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Sucursal Centro"));

        verify(sucursalService, times(1)).buscarSucursalesPorNombre("Centro");
    }

    @Test
    void testBuscarSucursalesPorNombre_EmptyResult() throws Exception {
        // Arrange
        when(sucursalService.buscarSucursalesPorNombre("NoExiste"))
                .thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/sucursal/nombre/NoExiste"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(sucursalService, times(1)).buscarSucursalesPorNombre("NoExiste");
    }

    // ========== TESTS PARA GET /api/sucursal/ciudad/{ciudad} ==========

    @Test
    void testBuscarSucursalesPorCiudad_Success() throws Exception {
        // Arrange
        when(sucursalService.buscarSucursalesPorCiudad("Bogotá"))
                .thenReturn(Arrays.asList(sucursalTest));

        // Act & Assert
        mockMvc.perform(get("/api/sucursal/ciudad/Bogotá"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].ciudad").value("Bogotá"));

        verify(sucursalService, times(1)).buscarSucursalesPorCiudad("Bogotá");
    }

    @Test
    void testBuscarSucursalesPorCiudad_EmptyResult() throws Exception {
        // Arrange
        when(sucursalService.buscarSucursalesPorCiudad("Cali"))
                .thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/sucursal/ciudad/Cali"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(sucursalService, times(1)).buscarSucursalesPorCiudad("Cali");
    }

    // ========== TESTS PARA GET /api/sucursal/existe/{id} ==========

    @Test
    void testExisteSucursal_True() throws Exception {
        // Arrange
        when(sucursalService.existeSucursal("507f1f77bcf86cd799439011")).thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/api/sucursal/existe/507f1f77bcf86cd799439011"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(true));

        verify(sucursalService, times(1)).existeSucursal("507f1f77bcf86cd799439011");
    }

    @Test
    void testExisteSucursal_False() throws Exception {
        // Arrange
        when(sucursalService.existeSucursal("507f1f77bcf86cd799439011")).thenReturn(false);

        // Act & Assert
        mockMvc.perform(get("/api/sucursal/existe/507f1f77bcf86cd799439011"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(false));

        verify(sucursalService, times(1)).existeSucursal("507f1f77bcf86cd799439011");
    }

    // ========== TESTS PARA GET /api/sucursal/existe-nombre/{nombre} ==========

    @Test
    void testExisteSucursalPorNombre_True() throws Exception {
        // Arrange
        when(sucursalService.existeSucursalPorNombre("Sucursal Centro")).thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/api/sucursal/existe-nombre/Sucursal Centro"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(true));

        verify(sucursalService, times(1)).existeSucursalPorNombre("Sucursal Centro");
    }

    @Test
    void testExisteSucursalPorNombre_False() throws Exception {
        // Arrange
        when(sucursalService.existeSucursalPorNombre("Sucursal Inexistente")).thenReturn(false);

        // Act & Assert
        mockMvc.perform(get("/api/sucursal/existe-nombre/Sucursal Inexistente"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(false));

        verify(sucursalService, times(1)).existeSucursalPorNombre("Sucursal Inexistente");
    }

    // ========== TESTS PARA GET /api/sucursal/existe-ciudad/{ciudad} ==========

    @Test
    void testExisteSucursalEnCiudad_True() throws Exception {
        // Arrange
        when(sucursalService.existeSucursalEnCiudad("Bogotá")).thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/api/sucursal/existe-ciudad/Bogotá"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(true));

        verify(sucursalService, times(1)).existeSucursalEnCiudad("Bogotá");
    }

    @Test
    void testExisteSucursalEnCiudad_False() throws Exception {
        // Arrange
        when(sucursalService.existeSucursalEnCiudad("Cartagena")).thenReturn(false);

        // Act & Assert
        mockMvc.perform(get("/api/sucursal/existe-ciudad/Cartagena"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(false));

        verify(sucursalService, times(1)).existeSucursalEnCiudad("Cartagena");
    }
}