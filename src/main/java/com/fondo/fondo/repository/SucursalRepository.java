package com.fondo.fondo.repository;

import com.fondo.fondo.entity.Sucursal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SucursalRepository extends MongoRepository<Sucursal, String> {
    
    // Buscar sucursales por nombre
    List<Sucursal> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar sucursales por ciudad
    List<Sucursal> findByCiudadIgnoreCase(String ciudad);
    
    // Buscar sucursal por nombre exacto
    Optional<Sucursal> findByNombre(String nombre);
    
    // Verificar si existe una sucursal por nombre
    boolean existsByNombre(String nombre);
    
    // Verificar si existe una sucursal en una ciudad
    boolean existsByCiudadIgnoreCase(String ciudad);
}