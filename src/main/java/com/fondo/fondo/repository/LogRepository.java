package com.fondo.fondo.repository;

import com.fondo.fondo.entity.Log;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogRepository extends MongoRepository<Log, String> {
    
    List<Log> findByTipoOperacion(String tipoOperacion);
    
    List<Log> findByEntidadId(String entidadId);
    
    List<Log> findByTipoEntidad(String tipoEntidad);
    
    List<Log> findByIdClienteOrderByFechaMovimientoDesc(String idCliente);
    
    List<Log> findByFechaMovimientoBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    List<Log> findByTipoOperacionAndTipoEntidad(String tipoOperacion, String tipoEntidad);
    
    List<Log> findByIdClienteAndTipoOperacionOrderByFechaMovimientoDesc(String idCliente, String tipoOperacion);
    
    List<Log> findByIdClienteAndFechaMovimientoBetweenOrderByFechaMovimientoDesc(String idCliente, LocalDateTime fechaInicio, LocalDateTime fechaFin);
}