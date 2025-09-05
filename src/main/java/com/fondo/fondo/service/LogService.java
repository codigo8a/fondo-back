package com.fondo.fondo.service;

import com.fondo.fondo.entity.Log;
import com.fondo.fondo.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    public Log registrarMovimiento(String tipoOperacion, String entidadId, String tipoEntidad, String idCliente, String detalles) {
        Log log = new Log(tipoOperacion, entidadId, tipoEntidad, idCliente, detalles);
        return logRepository.save(log);
    }

    public List<Log> obtenerTodosLosLogs() {
        return logRepository.findAll();
    }

    public List<Log> obtenerLogsPorTipoOperacion(String tipoOperacion) {
        return logRepository.findByTipoOperacion(tipoOperacion);
    }

    public List<Log> obtenerLogsPorEntidad(String entidadId) {
        return logRepository.findByEntidadId(entidadId);
    }

    public List<Log> obtenerLogsPorTipoEntidad(String tipoEntidad) {
        return logRepository.findByTipoEntidad(tipoEntidad);
    }

    public List<Log> obtenerLogsPorCliente(String idCliente) {
        return logRepository.findByIdClienteOrderByFechaMovimientoDesc(idCliente);
    }

    public List<Log> obtenerLogsPorClienteYOperacion(String idCliente, String tipoOperacion) {
        return logRepository.findByIdClienteAndTipoOperacionOrderByFechaMovimientoDesc(idCliente, tipoOperacion);
    }

    public List<Log> obtenerLogsPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return logRepository.findByFechaMovimientoBetween(fechaInicio, fechaFin);
    }

    public List<Log> obtenerLogsPorClienteYFecha(String idCliente, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return logRepository.findByIdClienteAndFechaMovimientoBetweenOrderByFechaMovimientoDesc(idCliente, fechaInicio, fechaFin);
    }

    public List<Log> obtenerLogsInscripciones() {
        return logRepository.findByTipoEntidad("INSCRIPCION");
    }
}