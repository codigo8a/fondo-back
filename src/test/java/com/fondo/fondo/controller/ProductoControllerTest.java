package com.fondo.fondo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fondo.fondo.dto.ProductoCreateDto;
import com.fondo.fondo.entity.Producto;
import com.fondo.fondo.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Producto productoTest;
    private List<Producto> productosTest;
    private ProductoCreateDto productoCreateDto;

    @BeforeEach
    void setUp() {
        productoTest = new Producto();
        productoTest.setId("507f1f77bcf86cd799439011");
        productoTest.setNombre("FPV_BTG_PACTUAL_ECOPETROL");
        productoTest.setTipoProducto("FPV");
        productoTest.setMontoMinimo(new BigDecimal("125000"));
        productoTest.setDisponibleEn(Arrays.asList("64f0a1c2e4b0f1a2d3c4e5f7", "64f0a1c2e4b0f1a2d3c4e5f8"));

        Producto producto2 = new Producto();
        producto2.setId("507f1f77bcf86cd799439012");
        producto2.setNombre("CDT_BANCO_POPULAR");
        producto2.setTipoProducto("CDT");
        producto2.setMontoMinimo(new BigDecimal("50000"));
        producto2.setDisponibleEn(Arrays.asList("64f0a1c2e4b0f1a2d3c4e5f7"));

        productosTest = Arrays.asList(productoTest, producto2);

        productoCreateDto = new ProductoCreateDto();
        productoCreateDto.setNombre("NUEVO_PRODUCTO");
        productoCreateDto.setTipoProducto("FIC");
        productoCreateDto.setMontoMinimo(new BigDecimal("100000"));
        productoCreateDto.setDisponibleEn(Arrays.asList("64f0a1c2e4b0f1a2d3c4e5f7"));
    }

    @Test
    void testObtenerTodosLosProductos_Success() throws Exception {
        // Arrange
        when(productoService.obtenerTodosLosProductos()).thenReturn(productosTest);

        // Act & Assert
        mockMvc.perform(get("/api/productos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("507f1f77bcf86cd799439011"))
                .andExpect(jsonPath("$[0].nombre").value("FPV_BTG_PACTUAL_ECOPETROL"))
                .andExpect(jsonPath("$[1].tipoProducto").value("CDT"));

        verify(productoService, times(1)).obtenerTodosLosProductos();
    }

    @Test
    void testObtenerProductoPorId_Success() throws Exception {
        // Arrange
        String id = "507f1f77bcf86cd799439011";
        when(productoService.obtenerProductoPorId(id)).thenReturn(Optional.of(productoTest));

        // Act & Assert
        mockMvc.perform(get("/api/productos/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nombre").value("FPV_BTG_PACTUAL_ECOPETROL"))
                .andExpect(jsonPath("$.tipoProducto").value("FPV"));

        verify(productoService, times(1)).obtenerProductoPorId(id);
    }

    @Test
    void testObtenerProductoPorId_NotFound() throws Exception {
        // Arrange
        String id = "507f1f77bcf86cd799439999";
        when(productoService.obtenerProductoPorId(id)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/productos/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(productoService, times(1)).obtenerProductoPorId(id);
    }

    @Test
    void testCrearProducto_Success() throws Exception {
        // Arrange
        Producto productoCreado = new Producto();
        productoCreado.setId("507f1f77bcf86cd799439013");
        productoCreado.setNombre("NUEVO_PRODUCTO");
        productoCreado.setTipoProducto("FIC");
        productoCreado.setMontoMinimo(new BigDecimal("100000"));
        productoCreado.setDisponibleEn(Arrays.asList("64f0a1c2e4b0f1a2d3c4e5f7"));

        when(productoService.crearProducto(any(Producto.class))).thenReturn(productoCreado);

        // Act & Assert
        mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productoCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("507f1f77bcf86cd799439013"))
                .andExpect(jsonPath("$.nombre").value("NUEVO_PRODUCTO"))
                .andExpect(jsonPath("$.tipoProducto").value("FIC"));

        verify(productoService, times(1)).crearProducto(any(Producto.class));
    }

    @Test
    void testCrearProducto_BadRequest() throws Exception {
        // Arrange
        when(productoService.crearProducto(any(Producto.class))).thenThrow(new RuntimeException("Error"));

        // Act & Assert
        mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productoCreateDto)))
                .andExpect(status().isBadRequest());

        verify(productoService, times(1)).crearProducto(any(Producto.class));
    }

    @Test
    void testActualizarProducto_Success() throws Exception {
        // Arrange
        String id = "507f1f77bcf86cd799439011";
        when(productoService.actualizarProducto(eq(id), any(Producto.class))).thenReturn(productoTest);

        // Act & Assert
        mockMvc.perform(put("/api/productos/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productoTest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nombre").value("FPV_BTG_PACTUAL_ECOPETROL"));

        verify(productoService, times(1)).actualizarProducto(eq(id), any(Producto.class));
    }

    @Test
    void testActualizarProducto_NotFound() throws Exception {
        // Arrange
        String id = "507f1f77bcf86cd799439999";
        when(productoService.actualizarProducto(eq(id), any(Producto.class)))
                .thenThrow(new RuntimeException("Producto no encontrado"));

        // Act & Assert
        mockMvc.perform(put("/api/productos/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productoTest)))
                .andExpect(status().isNotFound());

        verify(productoService, times(1)).actualizarProducto(eq(id), any(Producto.class));
    }

    @Test
    void testEliminarProducto_Success() throws Exception {
        // Arrange
        String id = "507f1f77bcf86cd799439011";
        doNothing().when(productoService).eliminarProducto(id);

        // Act & Assert
        mockMvc.perform(delete("/api/productos/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(productoService, times(1)).eliminarProducto(id);
    }

    @Test
    void testEliminarProducto_NotFound() throws Exception {
        // Arrange
        String id = "507f1f77bcf86cd799439999";
        doThrow(new RuntimeException("Producto no encontrado")).when(productoService).eliminarProducto(id);

        // Act & Assert
        mockMvc.perform(delete("/api/productos/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(productoService, times(1)).eliminarProducto(id);
    }

    @Test
    void testBuscarProductosPorNombre_Success() throws Exception {
        // Arrange
        String nombre = "BTG";
        List<Producto> productosEncontrados = Arrays.asList(productoTest);
        when(productoService.buscarProductosPorNombre(nombre)).thenReturn(productosEncontrados);

        // Act & Assert
        mockMvc.perform(get("/api/productos/nombre/{nombre}", nombre)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nombre").value("FPV_BTG_PACTUAL_ECOPETROL"));

        verify(productoService, times(1)).buscarProductosPorNombre(nombre);
    }

    @Test
    void testBuscarProductosPorTipo_Success() throws Exception {
        // Arrange
        String tipo = "FPV";
        List<Producto> productosEncontrados = Arrays.asList(productoTest);
        when(productoService.buscarProductosPorTipo(tipo)).thenReturn(productosEncontrados);

        // Act & Assert
        mockMvc.perform(get("/api/productos/tipo/{tipoProducto}", tipo)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].tipoProducto").value("FPV"));

        verify(productoService, times(1)).buscarProductosPorTipo(tipo);
    }

    @Test
    void testBuscarProductosPorMontoMaximo_Success() throws Exception {
        // Arrange
        BigDecimal montoMaximo = new BigDecimal("150000");
        List<Producto> productosEncontrados = Arrays.asList(productoTest);
        when(productoService.buscarProductosPorMontoMaximo(montoMaximo)).thenReturn(productosEncontrados);

        // Act & Assert
        mockMvc.perform(get("/api/productos/monto-maximo/{montoMaximo}", montoMaximo)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(productoService, times(1)).buscarProductosPorMontoMaximo(montoMaximo);
    }

    @Test
    void testBuscarProductosEnSucursal_Success() throws Exception {
        // Arrange
        String idSucursal = "64f0a1c2e4b0f1a2d3c4e5f7";
        when(productoService.buscarProductosEnSucursal(idSucursal)).thenReturn(productosTest);

        // Act & Assert
        mockMvc.perform(get("/api/productos/sucursal/{idSucursal}", idSucursal)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));

        verify(productoService, times(1)).buscarProductosEnSucursal(idSucursal);
    }

    @Test
    void testBuscarProductosPorTipoYSucursal_Success() throws Exception {
        // Arrange
        String tipo = "FPV";
        String idSucursal = "64f0a1c2e4b0f1a2d3c4e5f7";
        List<Producto> productosEncontrados = Arrays.asList(productoTest);
        when(productoService.buscarProductosPorTipoYSucursal(tipo, idSucursal)).thenReturn(productosEncontrados);

        // Act & Assert
        mockMvc.perform(get("/api/productos/tipo/{tipoProducto}/sucursal/{idSucursal}", tipo, idSucursal)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(productoService, times(1)).buscarProductosPorTipoYSucursal(tipo, idSucursal);
    }

    @Test
    void testExisteProducto_True() throws Exception {
        // Arrange
        String id = "507f1f77bcf86cd799439011";
        when(productoService.existeProducto(id)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/api/productos/existe/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));

        verify(productoService, times(1)).existeProducto(id);
    }

    @Test
    void testExisteProductoPorNombre_True() throws Exception {
        // Arrange
        String nombre = "FPV_BTG_PACTUAL_ECOPETROL";
        when(productoService.existeProductoPorNombre(nombre)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/api/productos/existe-nombre/{nombre}", nombre)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));

        verify(productoService, times(1)).existeProductoPorNombre(nombre);
    }

    @Test
    void testExisteProductoEnSucursal_True() throws Exception {
        // Arrange
        String idSucursal = "64f0a1c2e4b0f1a2d3c4e5f7";
        when(productoService.existeProductoEnSucursal(idSucursal)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/api/productos/existe-sucursal/{idSucursal}", idSucursal)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));

        verify(productoService, times(1)).existeProductoEnSucursal(idSucursal);
    }
}