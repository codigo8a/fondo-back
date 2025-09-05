package com.fondo.fondo.service;

import com.fondo.fondo.entity.Cliente;
import com.fondo.fondo.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;
    private Cliente clienteActualizado;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId("1");
        cliente.setNombre("Juan");
        cliente.setApellidos("Pérez García");
        cliente.setCiudad("Madrid");
        cliente.setMonto(new BigDecimal("15000.50"));

        clienteActualizado = new Cliente();
        clienteActualizado.setNombre("Carlos");
        clienteActualizado.setApellidos("López Martín");
        clienteActualizado.setCiudad("Barcelona");
        clienteActualizado.setMonto(new BigDecimal("20000.00"));
    }

    @Test
    void testObtenerTodosLosClientes() {
        // Arrange
        List<Cliente> clientes = Arrays.asList(cliente, clienteActualizado);
        when(clienteRepository.findAll()).thenReturn(clientes);

        // Act
        List<Cliente> resultado = clienteService.obtenerTodosLosClientes();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(clienteRepository).findAll();
    }

    @Test
    void testObtenerClientePorIdExitoso() {
        // Arrange
        when(clienteRepository.findById("1")).thenReturn(Optional.of(cliente));

        // Act
        Optional<Cliente> resultado = clienteService.obtenerClientePorId("1");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("Juan", resultado.get().getNombre());
        verify(clienteRepository).findById("1");
    }

    @Test
    void testObtenerClientePorIdNoExiste() {
        // Arrange
        when(clienteRepository.findById("999")).thenReturn(Optional.empty());

        // Act
        Optional<Cliente> resultado = clienteService.obtenerClientePorId("999");

        // Assert
        assertFalse(resultado.isPresent());
        verify(clienteRepository).findById("999");
    }

    @Test
    void testCrearClienteExitoso() {
        // Arrange
        Cliente clienteNuevo = new Cliente("Ana", "González", "Valencia", new BigDecimal("12000.00"));
        Cliente clienteGuardado = new Cliente("Ana", "González", "Valencia", new BigDecimal("12000.00"));
        clienteGuardado.setId("2");
        
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteGuardado);

        // Act
        Cliente resultado = clienteService.crearCliente(clienteNuevo);

        // Assert
        assertNotNull(resultado);
        assertEquals("Ana", resultado.getNombre());
        assertEquals("2", resultado.getId());
        assertNull(clienteNuevo.getId()); // Verificar que se estableció a null
        verify(clienteRepository).save(clienteNuevo);
    }

    @Test
    void testActualizarClienteExitoso() {
        // Arrange
        when(clienteRepository.findById("1")).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // Act
        Cliente resultado = clienteService.actualizarCliente("1", clienteActualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals("Carlos", cliente.getNombre());
        assertEquals("López Martín", cliente.getApellidos());
        assertEquals("Barcelona", cliente.getCiudad());
        assertEquals(new BigDecimal("20000.00"), cliente.getMonto());
        verify(clienteRepository).findById("1");
        verify(clienteRepository).save(cliente);
    }

    @Test
    void testActualizarClienteNoExiste() {
        // Arrange
        when(clienteRepository.findById("999")).thenReturn(Optional.empty());

        // Act
        Cliente resultado = clienteService.actualizarCliente("999", clienteActualizado);

        // Assert
        assertNull(resultado);
        verify(clienteRepository).findById("999");
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void testEliminarClienteExitoso() {
        // Arrange
        when(clienteRepository.existsById("1")).thenReturn(true);

        // Act
        boolean resultado = clienteService.eliminarCliente("1");

        // Assert
        assertTrue(resultado);
        verify(clienteRepository).existsById("1");
        verify(clienteRepository).deleteById("1");
    }

    @Test
    void testEliminarClienteNoExiste() {
        // Arrange
        when(clienteRepository.existsById("999")).thenReturn(false);

        // Act
        boolean resultado = clienteService.eliminarCliente("999");

        // Assert
        assertFalse(resultado);
        verify(clienteRepository).existsById("999");
        verify(clienteRepository, never()).deleteById(anyString());
    }

    @Test
    void testBuscarClientesPorCiudad() {
        // Arrange
        List<Cliente> clientesMadrid = Arrays.asList(cliente);
        when(clienteRepository.findByCiudadIgnoreCase("Madrid")).thenReturn(clientesMadrid);

        // Act
        List<Cliente> resultado = clienteService.buscarClientesPorCiudad("Madrid");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Madrid", resultado.get(0).getCiudad());
        verify(clienteRepository).findByCiudadIgnoreCase("Madrid");
    }

    @Test
    void testBuscarClientesPorNombre() {
        // Arrange
        List<Cliente> clientesJuan = Arrays.asList(cliente);
        when(clienteRepository.findByNombreContainingIgnoreCase("Juan")).thenReturn(clientesJuan);

        // Act
        List<Cliente> resultado = clienteService.buscarClientesPorNombre("Juan");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombre());
        verify(clienteRepository).findByNombreContainingIgnoreCase("Juan");
    }

    @Test
    void testBuscarClientesPorApellidos() {
        // Arrange
        List<Cliente> clientesPerez = Arrays.asList(cliente);
        when(clienteRepository.findByApellidosContainingIgnoreCase("Pérez")).thenReturn(clientesPerez);

        // Act
        List<Cliente> resultado = clienteService.buscarClientesPorApellidos("Pérez");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Pérez García", resultado.get(0).getApellidos());
        verify(clienteRepository).findByApellidosContainingIgnoreCase("Pérez");
    }

    @Test
    void testExisteClienteTrue() {
        // Arrange
        when(clienteRepository.existsById("1")).thenReturn(true);

        // Act
        boolean resultado = clienteService.existeCliente("1");

        // Assert
        assertTrue(resultado);
        verify(clienteRepository).existsById("1");
    }

    @Test
    void testExisteClienteFalse() {
        // Arrange
        when(clienteRepository.existsById("999")).thenReturn(false);

        // Act
        boolean resultado = clienteService.existeCliente("999");

        // Assert
        assertFalse(resultado);
        verify(clienteRepository).existsById("999");
    }

    @Test
    void testActualizarMontoClienteExitoso() {
        // Arrange
        BigDecimal nuevoMonto = new BigDecimal("25000.00");
        when(clienteRepository.findById("1")).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // Act
        Cliente resultado = clienteService.actualizarMontoCliente("1", nuevoMonto);

        // Assert
        assertNotNull(resultado);
        assertEquals(nuevoMonto, cliente.getMonto());
        verify(clienteRepository).findById("1");
        verify(clienteRepository).save(cliente);
    }

    @Test
    void testActualizarMontoClienteConCero() {
        // Arrange
        BigDecimal montoCero = BigDecimal.ZERO;
        when(clienteRepository.findById("1")).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // Act
        Cliente resultado = clienteService.actualizarMontoCliente("1", montoCero);

        // Assert
        assertNotNull(resultado);
        assertEquals(montoCero, cliente.getMonto());
        verify(clienteRepository).findById("1");
        verify(clienteRepository).save(cliente);
    }

    @Test
    void testActualizarMontoClienteNoExiste() {
        // Arrange
        BigDecimal nuevoMonto = new BigDecimal("25000.00");
        when(clienteRepository.findById("999")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            clienteService.actualizarMontoCliente("999", nuevoMonto);
        });
        
        assertEquals("Cliente no encontrado con ID: 999", exception.getMessage());
        verify(clienteRepository).findById("999");
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void testActualizarMontoClienteNegativo() {
        // Arrange
        BigDecimal montoNegativo = new BigDecimal("-1000.00");
        when(clienteRepository.findById("1")).thenReturn(Optional.of(cliente));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.actualizarMontoCliente("1", montoNegativo);
        });
        
        assertEquals("El monto no puede ser negativo", exception.getMessage());
        verify(clienteRepository).findById("1");
        verify(clienteRepository, never()).save(any(Cliente.class));
    }
}