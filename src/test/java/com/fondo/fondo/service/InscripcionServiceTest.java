package com.fondo.fondo.service;

import com.fondo.fondo.entity.Inscripcion;
import com.fondo.fondo.entity.Producto;
import com.fondo.fondo.entity.Sucursal;
import com.fondo.fondo.repository.InscripcionRepository;
import com.fondo.fondo.repository.ProductoRepository;
import com.fondo.fondo.repository.SucursalRepository;
import com.fondo.fondo.dto.InscripcionDetalleDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InscripcionServiceTest {

    @Mock
    private InscripcionRepository inscripcionRepository;
    
    @Mock
    private ProductoRepository productoRepository;
    
    @Mock
    private SucursalRepository sucursalRepository;
    
    @Mock
    private LogService logService;

    @InjectMocks
    private InscripcionService inscripcionService;

    private Inscripcion inscripcion;
    private Producto producto;
    private Sucursal sucursal;

    @BeforeEach
    void setUp() {
        inscripcion = new Inscripcion();
        inscripcion.setId("1");
        inscripcion.setIdCliente("cliente1");
        inscripcion.setIdProducto("producto1");
        inscripcion.setIdSucursal("sucursal1");
        inscripcion.setMontoInvertido(new BigDecimal("100000"));
        inscripcion.setFechaTransaccion(LocalDateTime.now());

        producto = new Producto();
        producto.setId("producto1");
        producto.setNombre("Producto Test");
        producto.setDisponibleEn(Arrays.asList("sucursal1", "sucursal2"));

        sucursal = new Sucursal();
        sucursal.setId("sucursal1");
        sucursal.setNombre("Sucursal Test");
    }

    @Test
    void testObtenerTodasLasInscripciones() {
        // Arrange
        List<Inscripcion> inscripciones = Arrays.asList(inscripcion);
        when(inscripcionRepository.findAll()).thenReturn(inscripciones);

        // Act
        List<Inscripcion> resultado = inscripcionService.obtenerTodasLasInscripciones();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(inscripcion.getId(), resultado.get(0).getId());
        verify(inscripcionRepository).findAll();
    }

    @Test
    void testObtenerInscripcionPorId() {
        // Arrange
        when(inscripcionRepository.findById("1")).thenReturn(Optional.of(inscripcion));

        // Act
        Optional<Inscripcion> resultado = inscripcionService.obtenerInscripcionPorId("1");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(inscripcion.getId(), resultado.get().getId());
        verify(inscripcionRepository).findById("1");
    }

    @Test
    void testObtenerInscripcionPorIdNoExiste() {
        // Arrange
        when(inscripcionRepository.findById("999")).thenReturn(Optional.empty());

        // Act
        Optional<Inscripcion> resultado = inscripcionService.obtenerInscripcionPorId("999");

        // Assert
        assertFalse(resultado.isPresent());
        verify(inscripcionRepository).findById("999");
    }

    @Test
    void testCrearInscripcionExitoso() {
        // Arrange
        Inscripcion nuevaInscripcion = new Inscripcion();
        nuevaInscripcion.setIdCliente("cliente1");
        nuevaInscripcion.setIdProducto("producto1");
        nuevaInscripcion.setIdSucursal("sucursal1");
        nuevaInscripcion.setMontoInvertido(new BigDecimal("100000"));
        nuevaInscripcion.setFechaTransaccion(LocalDateTime.now());

        when(productoRepository.findById("producto1")).thenReturn(Optional.of(producto));
        when(inscripcionRepository.findByIdClienteAndIdProducto("cliente1", "producto1"))
                .thenReturn(Collections.emptyList());
        when(inscripcionRepository.save(any(Inscripcion.class))).thenReturn(inscripcion);

        // Act
        Inscripcion resultado = inscripcionService.crearInscripcion(nuevaInscripcion);

        // Assert
        assertNotNull(resultado);
        assertEquals(inscripcion.getId(), resultado.getId());
        verify(productoRepository).findById("producto1");
        verify(inscripcionRepository).save(any(Inscripcion.class));
        verify(logService).registrarMovimiento(eq("CREAR_INSCRIPCION"), any(), eq("INSCRIPCION"), eq("cliente1"), any());
    }

    @Test
    void testCrearInscripcionProductoNoExiste() {
        // Arrange
        when(productoRepository.findById("producto1")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inscripcionService.crearInscripcion(inscripcion);
        });
        assertEquals("El producto no existe", exception.getMessage());
        verify(productoRepository).findById("producto1");
        verify(inscripcionRepository, never()).save(any());
    }

    @Test
    void testCrearInscripcionProductoNoDisponibleEnSucursal() {
        // Arrange
        producto.setDisponibleEn(Arrays.asList("sucursal2", "sucursal3"));
        when(productoRepository.findById("producto1")).thenReturn(Optional.of(producto));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inscripcionService.crearInscripcion(inscripcion);
        });
        assertEquals("Este producto no está disponible en la sucursal", exception.getMessage());
        verify(productoRepository).findById("producto1");
        verify(inscripcionRepository, never()).save(any());
    }

    @Test
    void testCrearInscripcionDuplicada() {
        // Arrange
        when(productoRepository.findById("producto1")).thenReturn(Optional.of(producto));
        when(inscripcionRepository.findByIdClienteAndIdProducto("cliente1", "producto1"))
                .thenReturn(Arrays.asList(inscripcion));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inscripcionService.crearInscripcion(inscripcion);
        });
        assertEquals("ya tiene una incripción con este producto", exception.getMessage());
        verify(inscripcionRepository, never()).save(any());
    }

    @Test
    void testActualizarInscripcionExitoso() {
        // Arrange
        when(inscripcionRepository.existsById("1")).thenReturn(true);
        when(inscripcionRepository.save(any(Inscripcion.class))).thenReturn(inscripcion);

        // Act
        Inscripcion resultado = inscripcionService.actualizarInscripcion("1", inscripcion);

        // Assert
        assertNotNull(resultado);
        assertEquals("1", resultado.getId());
        verify(inscripcionRepository).existsById("1");
        verify(inscripcionRepository).save(inscripcion);
    }

    @Test
    void testActualizarInscripcionNoExiste() {
        // Arrange
        when(inscripcionRepository.existsById("999")).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inscripcionService.actualizarInscripcion("999", inscripcion);
        });
        assertEquals("Inscripción no encontrada con ID: 999", exception.getMessage());
        verify(inscripcionRepository).existsById("999");
        verify(inscripcionRepository, never()).save(any());
    }

    @Test
    void testEliminarInscripcionExitoso() {
        // Arrange
        when(inscripcionRepository.existsById("1")).thenReturn(true);
        when(inscripcionRepository.findById("1")).thenReturn(Optional.of(inscripcion));
        doNothing().when(inscripcionRepository).deleteById("1");
        // Remover la línea problemática del doNothing para logService

        // Act
        boolean resultado = inscripcionService.eliminarInscripcion("1");

        // Assert
        assertTrue(resultado);
        verify(inscripcionRepository).existsById("1");
        verify(inscripcionRepository).findById("1");
        verify(inscripcionRepository).deleteById("1");
        verify(logService).registrarMovimiento(eq("ELIMINAR_INSCRIPCION"), eq("1"), eq("INSCRIPCION"), eq("cliente1"), any());
    }

    @Test
    void testEliminarInscripcionNoExiste() {
        // Arrange
        when(inscripcionRepository.existsById("999")).thenReturn(false);

        // Act
        boolean resultado = inscripcionService.eliminarInscripcion("999");

        // Assert
        assertFalse(resultado);
        verify(inscripcionRepository).existsById("999");
        verify(inscripcionRepository, never()).findById(any());
        verify(inscripcionRepository, never()).deleteById(any());
        verify(logService, never()).registrarMovimiento(any(), any(), any(), any(), any());
    }

    @Test
    void testEliminarInscripcionExistePeroBusquedaVacia() {
        // Arrange - Simular que existe pero findById retorna vacío
        when(inscripcionRepository.existsById("1")).thenReturn(true);
        when(inscripcionRepository.findById("1")).thenReturn(Optional.empty());

        // Act
        boolean resultado = inscripcionService.eliminarInscripcion("1");

        // Assert
        assertTrue(resultado);
        verify(inscripcionRepository).existsById("1");
        verify(inscripcionRepository).findById("1");
        verify(inscripcionRepository).deleteById("1");
        // Verificar que se llama al log con null como idCliente
        verify(logService).registrarMovimiento(
            eq("ELIMINAR_INSCRIPCION"), 
            eq("1"), 
            eq("INSCRIPCION"), 
            eq(null), 
            eq("Inscripción eliminada")
        );
    }

    @Test
    void testNoExisteInscripcion() {
        // Arrange
        when(inscripcionRepository.existsById("999")).thenReturn(false);

        // Act
        boolean resultado = inscripcionService.existeInscripcion("999");

        // Assert
        assertFalse(resultado);
        verify(inscripcionRepository).existsById("999");
    }
}