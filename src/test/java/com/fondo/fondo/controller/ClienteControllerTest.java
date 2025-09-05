package com.fondo.fondo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fondo.fondo.entity.Cliente;
import com.fondo.fondo.dto.ClienteCreateDto;
import com.fondo.fondo.dto.ClienteUpdateMontoDto;
import com.fondo.fondo.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Cliente clienteTest;
    private ClienteCreateDto clienteCreateDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(clienteController).build();
        objectMapper = new ObjectMapper();
        
        // Cliente de prueba
        clienteTest = new Cliente();
        clienteTest.setId("507f1f77bcf86cd799439011");
        clienteTest.setNombre("Juan");
        clienteTest.setApellidos("Pérez García");
        clienteTest.setCiudad("Bogotá");
        clienteTest.setMonto(new BigDecimal("1000000"));
        
        // DTO de creación
        clienteCreateDto = new ClienteCreateDto();
        clienteCreateDto.setNombre("Juan");
        clienteCreateDto.setApellidos("Pérez García");
        clienteCreateDto.setCiudad("Bogotá");
        clienteCreateDto.setMonto(new BigDecimal("1000000"));
    }

    // ========== TESTS PARA GET /api/clientes ==========
    
    @Test
    void testObtenerTodosLosClientes_Success() throws Exception {
        // Arrange
        List<Cliente> clientes = Arrays.asList(clienteTest);
        when(clienteService.obtenerTodosLosClientes()).thenReturn(clientes);

        // Act & Assert
        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(clienteTest.getId()))
                .andExpect(jsonPath("$[0].nombre").value(clienteTest.getNombre()))
                .andExpect(jsonPath("$[0].apellidos").value(clienteTest.getApellidos()))
                .andExpect(jsonPath("$[0].ciudad").value(clienteTest.getCiudad()))
                .andExpect(jsonPath("$[0].monto").value(1000000));
        
        verify(clienteService, times(1)).obtenerTodosLosClientes();
    }

    @Test
    void testObtenerTodosLosClientes_EmptyList() throws Exception {
        // Arrange
        when(clienteService.obtenerTodosLosClientes()).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    // ========== TESTS PARA GET /api/clientes/{id} ==========
    
    @Test
    void testObtenerClientePorId_Success() throws Exception {
        // Arrange
        when(clienteService.obtenerClientePorId("507f1f77bcf86cd799439011"))
                .thenReturn(Optional.of(clienteTest));

        // Act & Assert
        mockMvc.perform(get("/api/clientes/507f1f77bcf86cd799439011"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(clienteTest.getId()))
                .andExpect(jsonPath("$.nombre").value(clienteTest.getNombre()))
                .andExpect(jsonPath("$.apellidos").value(clienteTest.getApellidos()));
        
        verify(clienteService, times(1)).obtenerClientePorId("507f1f77bcf86cd799439011");
    }

    @Test
    void testObtenerClientePorId_NotFound() throws Exception {
        // Arrange
        when(clienteService.obtenerClientePorId("507f1f77bcf86cd799439011"))
                .thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/clientes/507f1f77bcf86cd799439011"))
                .andExpect(status().isNotFound());
        
        verify(clienteService, times(1)).obtenerClientePorId("507f1f77bcf86cd799439011");
    }

    @Test
    void testObtenerClientePorId_UnitTest() {
        // Arrange
        when(clienteService.obtenerClientePorId("507f1f77bcf86cd799439011"))
                .thenReturn(Optional.of(clienteTest));

        // Act
        ResponseEntity<Cliente> response = clienteController.obtenerClientePorId("507f1f77bcf86cd799439011");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(clienteTest.getId(), response.getBody().getId());
        assertEquals(clienteTest.getNombre(), response.getBody().getNombre());
    }

    // ========== TESTS PARA POST /api/clientes ==========
    
    @Test
    void testCrearCliente_Success() throws Exception {
        // Arrange
        when(clienteService.crearCliente(any(Cliente.class))).thenReturn(clienteTest);

        // Act & Assert
        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(clienteTest.getId()))
                .andExpect(jsonPath("$.nombre").value(clienteTest.getNombre()))
                .andExpect(jsonPath("$.apellidos").value(clienteTest.getApellidos()));
        
        verify(clienteService, times(1)).crearCliente(any(Cliente.class));
    }

    @Test
    void testCrearCliente_UnitTest() {
        // Arrange
        when(clienteService.crearCliente(any(Cliente.class))).thenReturn(clienteTest);

        // Act
        ResponseEntity<Cliente> response = clienteController.crearCliente(clienteCreateDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(clienteTest.getId(), response.getBody().getId());
        verify(clienteService, times(1)).crearCliente(any(Cliente.class));
    }

    // ========== TESTS PARA PUT /api/clientes/{id} ==========
    
    @Test
    void testActualizarCliente_Success() throws Exception {
        // Arrange
        Cliente clienteActualizado = new Cliente();
        clienteActualizado.setId("507f1f77bcf86cd799439011");
        clienteActualizado.setNombre("Juan Carlos");
        clienteActualizado.setApellidos("Pérez García");
        clienteActualizado.setCiudad("Medellín");
        clienteActualizado.setMonto(new BigDecimal("1500000"));
        
        when(clienteService.actualizarCliente(eq("507f1f77bcf86cd799439011"), any(Cliente.class)))
                .thenReturn(clienteActualizado);

        // Act & Assert
        mockMvc.perform(put("/api/clientes/507f1f77bcf86cd799439011")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteActualizado)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("Juan Carlos"))
                .andExpect(jsonPath("$.ciudad").value("Medellín"))
                .andExpect(jsonPath("$.monto").value(1500000));
        
        verify(clienteService, times(1)).actualizarCliente(eq("507f1f77bcf86cd799439011"), any(Cliente.class));
    }

    @Test
    void testActualizarCliente_NotFound() throws Exception {
        // Arrange
        when(clienteService.actualizarCliente(eq("507f1f77bcf86cd799439011"), any(Cliente.class)))
                .thenThrow(new RuntimeException("Cliente no encontrado"));

        // Act & Assert
        mockMvc.perform(put("/api/clientes/507f1f77bcf86cd799439011")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteTest)))
                .andExpect(status().isNotFound());
        
        verify(clienteService, times(1)).actualizarCliente(eq("507f1f77bcf86cd799439011"), any(Cliente.class));
    }

    // ========== TESTS PARA DELETE /api/clientes/{id} ==========
    
    @Test
    void testEliminarCliente_Success() throws Exception {
        // Arrange
        when(clienteService.eliminarCliente("507f1f77bcf86cd799439011")).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/api/clientes/507f1f77bcf86cd799439011"))
                .andExpect(status().isOk());
        
        verify(clienteService, times(1)).eliminarCliente("507f1f77bcf86cd799439011");
    }

    @Test
    void testEliminarCliente_NotFound() throws Exception {
        // Arrange
        when(clienteService.eliminarCliente("507f1f77bcf86cd799439011")).thenReturn(false);

        // Act & Assert
        mockMvc.perform(delete("/api/clientes/507f1f77bcf86cd799439011"))
                .andExpect(status().isNotFound());
        
        verify(clienteService, times(1)).eliminarCliente("507f1f77bcf86cd799439011");
    }

    // ========== TESTS PARA GET /api/clientes/ciudad/{ciudad} ==========
    
    @Test
    void testBuscarClientesPorCiudad_Success() throws Exception {
        // Arrange
        List<Cliente> clientesBogota = Arrays.asList(clienteTest);
        when(clienteService.buscarClientesPorCiudad("Bogotá")).thenReturn(clientesBogota);

        // Act & Assert
        mockMvc.perform(get("/api/clientes/ciudad/Bogotá"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].ciudad").value("Bogotá"))
                .andExpect(jsonPath("$[0].nombre").value("Juan"));
        
        verify(clienteService, times(1)).buscarClientesPorCiudad("Bogotá");
    }

    @Test
    void testBuscarClientesPorCiudad_EmptyResult() throws Exception {
        // Arrange
        when(clienteService.buscarClientesPorCiudad("Cali")).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/clientes/ciudad/Cali"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    // ========== TESTS PARA GET /api/clientes/existe/{id} ==========
    
    @Test
    void testExisteCliente_True() throws Exception {
        // Arrange
        when(clienteService.existeCliente("507f1f77bcf86cd799439011")).thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/api/clientes/existe/507f1f77bcf86cd799439011"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(true));
        
        verify(clienteService, times(1)).existeCliente("507f1f77bcf86cd799439011");
    }

    @Test
    void testExisteCliente_False() throws Exception {
        // Arrange
        when(clienteService.existeCliente("507f1f77bcf86cd799439011")).thenReturn(false);

        // Act & Assert
        mockMvc.perform(get("/api/clientes/existe/507f1f77bcf86cd799439011"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(false));
    }

    // ========== TESTS PARA PUT /api/clientes/{id}/monto ==========
    
    @Test
    void testActualizarMontoCliente_Success() throws Exception {
        // Arrange
        ClienteUpdateMontoDto montoDto = new ClienteUpdateMontoDto();
        montoDto.setMonto(new BigDecimal("2000000"));
        
        Cliente clienteActualizado = new Cliente();
        clienteActualizado.setId("507f1f77bcf86cd799439011");
        clienteActualizado.setNombre("Juan");
        clienteActualizado.setApellidos("Pérez García");
        clienteActualizado.setCiudad("Bogotá");
        clienteActualizado.setMonto(new BigDecimal("2000000"));
        
        when(clienteService.actualizarMontoCliente("507f1f77bcf86cd799439011", new BigDecimal("2000000")))
                .thenReturn(clienteActualizado);

        // Act & Assert
        mockMvc.perform(patch("/api/clientes/507f1f77bcf86cd799439011/monto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(montoDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.monto").value(2000000));
        
        verify(clienteService, times(1)).actualizarMontoCliente("507f1f77bcf86cd799439011", new BigDecimal("2000000"));
    }

    @Test
    void testActualizarMontoCliente_NotFound() throws Exception {
        // Arrange
        ClienteUpdateMontoDto montoDto = new ClienteUpdateMontoDto();
        montoDto.setMonto(new BigDecimal("2000000"));
        
        when(clienteService.actualizarMontoCliente("507f1f77bcf86cd799439011", new BigDecimal("2000000")))
                .thenThrow(new RuntimeException("Cliente no encontrado"));

        // Act & Assert
        mockMvc.perform(patch("/api/clientes/507f1f77bcf86cd799439011/monto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(montoDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Cliente no encontrado"));
        
        verify(clienteService, times(1)).actualizarMontoCliente("507f1f77bcf86cd799439011", new BigDecimal("2000000"));
    }

    // ========== TESTS DE VALIDACIÓN ==========
    
    @Test
    void testCrearCliente_ValidationError() throws Exception {
        // Arrange - DTO con datos inválidos
        ClienteCreateDto dtoInvalido = new ClienteCreateDto();
        // Nombre vacío debería fallar la validación
        dtoInvalido.setNombre("");
        dtoInvalido.setApellidos("Pérez");
        dtoInvalido.setCiudad("Bogotá");
        dtoInvalido.setMonto(new BigDecimal("1000000"));

        // Act & Assert
        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testActualizarCliente_ValidationError() throws Exception {
        // Arrange - Cliente con datos inválidos
        Cliente clienteInvalido = new Cliente();
        clienteInvalido.setNombre(""); // Nombre vacío
        clienteInvalido.setApellidos("Pérez");
        clienteInvalido.setCiudad("Bogotá");
        clienteInvalido.setMonto(new BigDecimal("-1000")); // Monto negativo

        // Act & Assert
        mockMvc.perform(put("/api/clientes/507f1f77bcf86cd799439011")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteInvalido)))
                .andExpect(status().isBadRequest());
    }
}