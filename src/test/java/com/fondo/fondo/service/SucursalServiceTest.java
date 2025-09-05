package com.fondo.fondo.service;

import com.fondo.fondo.entity.Sucursal;
import com.fondo.fondo.repository.SucursalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SucursalServiceTest {

    @Mock
    private SucursalRepository sucursalRepository;

    @InjectMocks
    private SucursalService sucursalService;

    private Sucursal sucursal;
    private List<Sucursal> sucursalesTest;

    @BeforeEach
    void setUp() {
        sucursal = new Sucursal();
        sucursal.setId("1");
        sucursal.setNombre("Sucursal Centro");
        sucursal.setCiudad("Bogotá");

        Sucursal sucursal2 = new Sucursal();
        sucursal2.setId("2");
        sucursal2.setNombre("Sucursal Norte");
        sucursal2.setCiudad("Medellín");

        sucursalesTest = Arrays.asList(sucursal, sucursal2);
    }

    // ========== TESTS PARA obtenerTodasLasSucursales ==========

    @Test
    void testObtenerTodasLasSucursales_Success() {
        // Arrange
        when(sucursalRepository.findAll()).thenReturn(sucursalesTest);

        // Act
        List<Sucursal> resultado = sucursalService.obtenerTodasLasSucursales();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Sucursal Centro", resultado.get(0).getNombre());
        assertEquals("Sucursal Norte", resultado.get(1).getNombre());
        verify(sucursalRepository, times(1)).findAll();
    }

    @Test
    void testObtenerTodasLasSucursales_EmptyList() {
        // Arrange
        when(sucursalRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Sucursal> resultado = sucursalService.obtenerTodasLasSucursales();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(sucursalRepository, times(1)).findAll();
    }

    // ========== TESTS PARA obtenerSucursalPorId ==========

    @Test
    void testObtenerSucursalPorId_Success() {
        // Arrange
        when(sucursalRepository.findById("1")).thenReturn(Optional.of(sucursal));

        // Act
        Optional<Sucursal> resultado = sucursalService.obtenerSucursalPorId("1");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("1", resultado.get().getId());
        assertEquals("Sucursal Centro", resultado.get().getNombre());
        verify(sucursalRepository, times(1)).findById("1");
    }

    @Test
    void testObtenerSucursalPorId_NotFound() {
        // Arrange
        when(sucursalRepository.findById("999")).thenReturn(Optional.empty());

        // Act
        Optional<Sucursal> resultado = sucursalService.obtenerSucursalPorId("999");

        // Assert
        assertFalse(resultado.isPresent());
        verify(sucursalRepository, times(1)).findById("999");
    }

    // ========== TESTS PARA crearSucursal ==========

    @Test
    void testCrearSucursal_Success() {
        // Arrange
        Sucursal sucursalGuardada = new Sucursal();
        sucursalGuardada.setId("generatedId");
        sucursalGuardada.setNombre("Sucursal Centro");
        sucursalGuardada.setCiudad("Bogotá");
        
        when(sucursalRepository.save(any(Sucursal.class))).thenReturn(sucursalGuardada);

        // Act
        Sucursal resultado = sucursalService.crearSucursal(sucursal);

        // Assert
        assertNotNull(resultado);
        assertEquals("generatedId", resultado.getId());
        assertEquals("Sucursal Centro", resultado.getNombre());
        assertEquals("Bogotá", resultado.getCiudad());
        assertNull(sucursal.getId()); // Verificar que se estableció a null
        verify(sucursalRepository, times(1)).save(sucursal);
    }

    @Test
    void testCrearSucursal_NullInput() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            sucursalService.crearSucursal(null);
        });
        verify(sucursalRepository, never()).save(any());
    }

    // ========== TESTS PARA actualizarSucursal ==========

    @Test
    void testActualizarSucursal_Success() {
        // Arrange
        when(sucursalRepository.findById("1")).thenReturn(Optional.of(sucursal));
        when(sucursalRepository.save(any(Sucursal.class))).thenReturn(sucursal);

        // Act
        Sucursal resultado = sucursalService.actualizarSucursal("1", sucursal);

        // Assert
        assertNotNull(resultado);
        assertEquals("1", resultado.getId());
        verify(sucursalRepository, times(1)).findById("1");
        verify(sucursalRepository, times(1)).save(sucursal);
    }

    @Test
    void testActualizarSucursal_NotFound() {
        // Arrange
        when(sucursalRepository.findById("999")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            sucursalService.actualizarSucursal("999", sucursal);
        });
        assertEquals("Sucursal no encontrada con ID: 999", exception.getMessage());
        verify(sucursalRepository, times(1)).findById("999");
        verify(sucursalRepository, never()).save(any());
    }

    // ========== TESTS PARA eliminarSucursal ==========

    @Test
    void testEliminarSucursal_Success() {
        // Arrange
        when(sucursalRepository.existsById("1")).thenReturn(true);
        doNothing().when(sucursalRepository).deleteById("1");

        // Act
        assertDoesNotThrow(() -> sucursalService.eliminarSucursal("1"));

        // Assert
        verify(sucursalRepository, times(1)).existsById("1");
        verify(sucursalRepository, times(1)).deleteById("1");
    }

    @Test
    void testEliminarSucursal_NotFound() {
        // Arrange
        when(sucursalRepository.existsById("999")).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            sucursalService.eliminarSucursal("999");
        });
        assertEquals("Sucursal no encontrada con ID: 999", exception.getMessage());
        verify(sucursalRepository, times(1)).existsById("999");
        verify(sucursalRepository, never()).deleteById(any());
    }

    // ========== TESTS PARA buscarSucursalesPorNombre ==========

    @Test
    void testBuscarSucursalesPorNombre_Success() {
        // Arrange
        List<Sucursal> sucursalesEncontradas = Arrays.asList(sucursal);
        when(sucursalRepository.findByNombreContainingIgnoreCase("Centro"))
                .thenReturn(sucursalesEncontradas);

        // Act
        List<Sucursal> resultado = sucursalService.buscarSucursalesPorNombre("Centro");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Sucursal Centro", resultado.get(0).getNombre());
        verify(sucursalRepository, times(1)).findByNombreContainingIgnoreCase("Centro");
    }

    @Test
    void testBuscarSucursalesPorNombre_EmptyResult() {
        // Arrange
        when(sucursalRepository.findByNombreContainingIgnoreCase("NoExiste"))
                .thenReturn(Arrays.asList());

        // Act
        List<Sucursal> resultado = sucursalService.buscarSucursalesPorNombre("NoExiste");

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(sucursalRepository, times(1)).findByNombreContainingIgnoreCase("NoExiste");
    }

    // ========== TESTS PARA buscarSucursalesPorCiudad ==========

    @Test
    void testBuscarSucursalesPorCiudad_Success() {
        // Arrange
        List<Sucursal> sucursalesEncontradas = Arrays.asList(sucursal);
        when(sucursalRepository.findByCiudadIgnoreCase("Bogotá"))
                .thenReturn(sucursalesEncontradas);

        // Act
        List<Sucursal> resultado = sucursalService.buscarSucursalesPorCiudad("Bogotá");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Bogotá", resultado.get(0).getCiudad());
        verify(sucursalRepository, times(1)).findByCiudadIgnoreCase("Bogotá");
    }

    @Test
    void testBuscarSucursalesPorCiudad_EmptyResult() {
        // Arrange
        when(sucursalRepository.findByCiudadIgnoreCase("Cali"))
                .thenReturn(Arrays.asList());

        // Act
        List<Sucursal> resultado = sucursalService.buscarSucursalesPorCiudad("Cali");

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(sucursalRepository, times(1)).findByCiudadIgnoreCase("Cali");
    }

    // ========== TESTS PARA existeSucursal ==========

    @Test
    void testExisteSucursal_True() {
        // Arrange
        when(sucursalRepository.existsById("1")).thenReturn(true);

        // Act
        boolean resultado = sucursalService.existeSucursal("1");

        // Assert
        assertTrue(resultado);
        verify(sucursalRepository, times(1)).existsById("1");
    }

    @Test
    void testExisteSucursal_False() {
        // Arrange
        when(sucursalRepository.existsById("999")).thenReturn(false);

        // Act
        boolean resultado = sucursalService.existeSucursal("999");

        // Assert
        assertFalse(resultado);
        verify(sucursalRepository, times(1)).existsById("999");
    }

    // ========== TESTS PARA existeSucursalPorNombre ==========

    @Test
    void testExisteSucursalPorNombre_True() {
        // Arrange
        when(sucursalRepository.existsByNombre("Sucursal Centro")).thenReturn(true);

        // Act
        boolean resultado = sucursalService.existeSucursalPorNombre("Sucursal Centro");

        // Assert
        assertTrue(resultado);
        verify(sucursalRepository, times(1)).existsByNombre("Sucursal Centro");
    }

    @Test
    void testExisteSucursalPorNombre_False() {
        // Arrange
        when(sucursalRepository.existsByNombre("Sucursal Inexistente")).thenReturn(false);

        // Act
        boolean resultado = sucursalService.existeSucursalPorNombre("Sucursal Inexistente");

        // Assert
        assertFalse(resultado);
        verify(sucursalRepository, times(1)).existsByNombre("Sucursal Inexistente");
    }

    // ========== TESTS PARA existeSucursalEnCiudad ==========

    @Test
    void testExisteSucursalEnCiudad_True() {
        // Arrange
        when(sucursalRepository.existsByCiudadIgnoreCase("Bogotá")).thenReturn(true);

        // Act
        boolean resultado = sucursalService.existeSucursalEnCiudad("Bogotá");

        // Assert
        assertTrue(resultado);
        verify(sucursalRepository, times(1)).existsByCiudadIgnoreCase("Bogotá");
    }

    @Test
    void testExisteSucursalEnCiudad_False() {
        // Arrange
        when(sucursalRepository.existsByCiudadIgnoreCase("Cali")).thenReturn(false);

        // Act
        boolean resultado = sucursalService.existeSucursalEnCiudad("Cali");

        // Assert
        assertFalse(resultado);
        verify(sucursalRepository, times(1)).existsByCiudadIgnoreCase("Cali");
    }
}