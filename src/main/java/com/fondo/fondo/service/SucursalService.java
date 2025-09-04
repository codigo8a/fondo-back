package com.fondo.fondo.service;

import com.fondo.fondo.entity.Sucursal;
import com.fondo.fondo.repository.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SucursalService {
    
    @Autowired
    private SucursalRepository sucursalRepository;
    
    // Obtener todas las sucursales
    public List<Sucursal> obtenerTodasLasSucursales() {
        return sucursalRepository.findAll();
    }
    
    // Obtener sucursal por ID
    public Optional<Sucursal> obtenerSucursalPorId(String id) {
        return sucursalRepository.findById(id);
    }
    
    // Crear nueva sucursal
    public Sucursal crearSucursal(Sucursal sucursal) {
        // Asegurar que el ID sea null para forzar la creación de una nueva sucursal
        sucursal.setId(null);
        return sucursalRepository.save(sucursal);
    }
    
    // Actualizar sucursal
    public Sucursal actualizarSucursal(String id, Sucursal sucursalActualizada) {
        return sucursalRepository.findById(id)
                .map(sucursal -> {
                    sucursal.setNombre(sucursalActualizada.getNombre());
                    sucursal.setCiudad(sucursalActualizada.getCiudad());
                    return sucursalRepository.save(sucursal);
                })
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada con ID: " + id));
    }
    
    // Eliminar sucursal
    public void eliminarSucursal(String id) {
        if (sucursalRepository.existsById(id)) {
            sucursalRepository.deleteById(id);
        } else {
            throw new RuntimeException("Sucursal no encontrada con ID: " + id);
        }
    }
    
    // Buscar sucursales por nombre
    public List<Sucursal> buscarSucursalesPorNombre(String nombre) {
        return sucursalRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    // Buscar sucursales por ciudad
    public List<Sucursal> buscarSucursalesPorCiudad(String ciudad) {
        return sucursalRepository.findByCiudadIgnoreCase(ciudad);
    }
    
    // Verificar si existe sucursal
    public boolean existeSucursal(String id) {
        return sucursalRepository.existsById(id);
    }
    
    // Verificar si existe sucursal por nombre
    public boolean existeSucursalPorNombre(String nombre) {
        return sucursalRepository.existsByNombre(nombre);
    }
    
    // Verificar si existe sucursal en ciudad
    public boolean existeSucursalEnCiudad(String ciudad) {
        return sucursalRepository.existsByCiudadIgnoreCase(ciudad);
    }
}