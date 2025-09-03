package com.fondo.fondo.repository;

import com.fondo.fondo.entity.Cliente;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends MongoRepository<Cliente, String> {
    
    // Buscar por ciudad (case insensitive)
    List<Cliente> findByCiudadIgnoreCase(String ciudad);
    
    // Buscar por nombre que contenga el texto (case insensitive)
    @Query("{'nombre': {$regex: ?0, $options: 'i'}}")
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar por apellidos que contenga el texto (case insensitive)
    @Query("{'apellidos': {$regex: ?0, $options: 'i'}}")
    List<Cliente> findByApellidosContainingIgnoreCase(String apellidos);
    
    // Buscar por nombre y apellidos
    @Query("{'nombre': {$regex: ?0, $options: 'i'}, 'apellidos': {$regex: ?1, $options: 'i'}}")
    List<Cliente> findByNombreAndApellidos(String nombre, String apellidos);
}