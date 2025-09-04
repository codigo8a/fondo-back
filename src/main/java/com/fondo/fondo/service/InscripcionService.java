package com.fondo.fondo.service;

import com.fondo.fondo.entity.Inscripcion;
import com.fondo.fondo.entity.Producto;
import com.fondo.fondo.repository.InscripcionRepository;
import com.fondo.fondo.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@Service
public class InscripcionService {

    @Autowired
    private InscripcionRepository inscripcionRepository;
    
    @Autowired
    private ProductoRepository productoRepository;

    public List<Inscripcion> obtenerTodasLasInscripciones() {
        return inscripcionRepository.findAll();
    }

    public Optional<Inscripcion> obtenerInscripcionPorId(String id) {
        return inscripcionRepository.findById(id);
    }

    public Inscripcion crearInscripcion(Inscripcion inscripcion) {
        // Validar que el producto existe
        Optional<Producto> productoOpt = productoRepository.findById(inscripcion.getIdProducto());
        if (productoOpt.isEmpty()) {
            throw new RuntimeException("El producto no existe");
        }
        
        Producto producto = productoOpt.get();
        
        // Validar que el producto está disponible en la sucursal especificada
        if (producto.getDisponibleEn() == null || !producto.getDisponibleEn().contains(inscripcion.getIdSucursal())) {
            throw new RuntimeException("Este producto no está disponible en la sucursal");
        }
        
        // Establecer ID como null para asegurar que se genere uno nuevo
        inscripcion.setId(null);
        return inscripcionRepository.save(inscripcion);
    }

    public Inscripcion actualizarInscripcion(String id, Inscripcion inscripcion) {
        if (inscripcionRepository.existsById(id)) {
            inscripcion.setId(id);
            return inscripcionRepository.save(inscripcion);
        } else {
            throw new RuntimeException("Inscripción no encontrada con ID: " + id);
        }
    }

    public boolean eliminarInscripcion(String id) {
        if (inscripcionRepository.existsById(id)) {
            inscripcionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Inscripcion> buscarInscripcionesPorCliente(String idCliente) {
        return inscripcionRepository.findByIdCliente(idCliente);
    }

    public List<Inscripcion> buscarInscripcionesPorProducto(String idProducto) {
        return inscripcionRepository.findByIdProducto(idProducto);
    }

    public List<Inscripcion> buscarInscripcionesPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return inscripcionRepository.findByFechaTransaccionBetween(fechaInicio, fechaFin);
    }

    public boolean existeInscripcion(String id) {
        return inscripcionRepository.existsById(id);
    }
}