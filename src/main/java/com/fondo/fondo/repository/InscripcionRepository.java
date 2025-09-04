package com.fondo.fondo.repository;

import com.fondo.fondo.entity.Inscripcion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InscripcionRepository extends MongoRepository<Inscripcion, String> {
    
    List<Inscripcion> findByIdCliente(String idCliente);
    
    List<Inscripcion> findByIdProducto(String idProducto);
    List<Inscripcion> findByFechaTransaccionBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    List<Inscripcion> findByIdClienteAndIdProducto(String idCliente, String idProducto);
}