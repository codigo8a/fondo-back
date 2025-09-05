package com.fondo.fondo.service;

import com.fondo.fondo.entity.Log;
import com.fondo.fondo.repository.LogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogServiceTest {

    @Mock
    private LogRepository logRepository;

    @InjectMocks
    private LogService logService;

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
    void testRegistrarMovimiento_Success() {
        // Arrange
        when(logRepository.save(any(Log.class))).thenReturn(logTest);

        // Act
        Log resultado = logService.registrarMovimiento(
            "CREAR_INSCRIPCION",
            "507f1f77bcf86cd799439012",
            "INSCRIPCION",
            "507f1f77bcf86cd799439013",
            "Inscripción creada exitosamente"
        );

        // Assert
        assertNotNull(resultado);
        assertEquals("CREAR_INSCRIPCION", resultado.getTipoOperacion());
        assertEquals("507f1f77bcf86cd799439012", resultado.getEntidadId());
        assertEquals("INSCRIPCION", resultado.getTipoEntidad());
        assertEquals("507f1f77bcf86cd799439013", resultado.getIdCliente());
        assertEquals("Inscripción creada exitosamente", resultado.getDetalles());
        verify(logRepository, times(1)).save(any(Log.class));
    }

    @Test
    void testObtenerTodosLosLogs_Success() {
        // Arrange
        when(logRepository.findAll()).thenReturn(logsTest);

        // Act
        List<Log> resultado = logService.obtenerTodosLosLogs();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(logRepository, times(1)).findAll();
    }

    @Test
    void testObtenerLogsPorTipoOperacion_Success() {
        // Arrange
        List<Log> logsCrear = Arrays.asList(logTest);
        when(logRepository.findByTipoOperacion("CREAR_INSCRIPCION")).thenReturn(logsCrear);

        // Act
        List<Log> resultado = logService.obtenerLogsPorTipoOperacion("CREAR_INSCRIPCION");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("CREAR_INSCRIPCION", resultado.get(0).getTipoOperacion());
        verify(logRepository, times(1)).findByTipoOperacion("CREAR_INSCRIPCION");
    }

    @Test
    void testObtenerLogsPorEntidad_Success() {
        // Arrange
        List<Log> logsEntidad = Arrays.asList(logTest);
        when(logRepository.findByEntidadId("507f1f77bcf86cd799439012")).thenReturn(logsEntidad);

        // Act
        List<Log> resultado = logService.obtenerLogsPorEntidad("507f1f77bcf86cd799439012");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("507f1f77bcf86cd799439012", resultado.get(0).getEntidadId());
        verify(logRepository, times(1)).findByEntidadId("507f1f77bcf86cd799439012");
    }

    @Test
    void testObtenerLogsPorTipoEntidad_Success() {
        // Arrange
        when(logRepository.findByTipoEntidad("INSCRIPCION")).thenReturn(logsTest);

        // Act
        List<Log> resultado = logService.obtenerLogsPorTipoEntidad("INSCRIPCION");

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(logRepository, times(1)).findByTipoEntidad("INSCRIPCION");
    }

    @Test
    void testObtenerLogsPorCliente_Success() {
        // Arrange
        when(logRepository.findByIdClienteOrderByFechaMovimientoDesc("507f1f77bcf86cd799439013")).thenReturn(logsTest);

        // Act
        List<Log> resultado = logService.obtenerLogsPorCliente("507f1f77bcf86cd799439013");

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(logRepository, times(1)).findByIdClienteOrderByFechaMovimientoDesc("507f1f77bcf86cd799439013");
    }

    @Test
    void testObtenerLogsPorClienteYOperacion_Success() {
        // Arrange
        List<Log> logsClienteOperacion = Arrays.asList(logTest);
        when(logRepository.findByIdClienteAndTipoOperacionOrderByFechaMovimientoDesc(
            "507f1f77bcf86cd799439013", "CREAR_INSCRIPCION")).thenReturn(logsClienteOperacion);

        // Act
        List<Log> resultado = logService.obtenerLogsPorClienteYOperacion(
            "507f1f77bcf86cd799439013", "CREAR_INSCRIPCION");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("CREAR_INSCRIPCION", resultado.get(0).getTipoOperacion());
        verify(logRepository, times(1)).findByIdClienteAndTipoOperacionOrderByFechaMovimientoDesc(
            "507f1f77bcf86cd799439013", "CREAR_INSCRIPCION");
    }

    @Test
    void testObtenerLogsPorFecha_Success() {
        // Arrange
        LocalDateTime fechaInicio = LocalDateTime.now().minusDays(1);
        LocalDateTime fechaFin = LocalDateTime.now();
        when(logRepository.findByFechaMovimientoBetween(fechaInicio, fechaFin)).thenReturn(logsTest);

        // Act
        List<Log> resultado = logService.obtenerLogsPorFecha(fechaInicio, fechaFin);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(logRepository, times(1)).findByFechaMovimientoBetween(fechaInicio, fechaFin);
    }

    @Test
    void testObtenerLogsPorClienteYFecha_Success() {
        // Arrange
        LocalDateTime fechaInicio = LocalDateTime.now().minusDays(1);
        LocalDateTime fechaFin = LocalDateTime.now();
        when(logRepository.findByIdClienteAndFechaMovimientoBetweenOrderByFechaMovimientoDesc(
            "507f1f77bcf86cd799439013", fechaInicio, fechaFin)).thenReturn(logsTest);

        // Act
        List<Log> resultado = logService.obtenerLogsPorClienteYFecha(
            "507f1f77bcf86cd799439013", fechaInicio, fechaFin);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(logRepository, times(1)).findByIdClienteAndFechaMovimientoBetweenOrderByFechaMovimientoDesc(
            "507f1f77bcf86cd799439013", fechaInicio, fechaFin);
    }

    @Test
    void testObtenerLogsInscripciones_Success() {
        // Arrange
        when(logRepository.findByTipoEntidad("INSCRIPCION")).thenReturn(logsTest);

        // Act
        List<Log> resultado = logService.obtenerLogsInscripciones();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(logRepository, times(1)).findByTipoEntidad("INSCRIPCION");
    }
}