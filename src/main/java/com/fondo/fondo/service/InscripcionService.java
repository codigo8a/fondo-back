package com.fondo.fondo.service;

import com.fondo.fondo.entity.Inscripcion;
import com.fondo.fondo.entity.Producto;
import com.fondo.fondo.repository.InscripcionRepository;
import com.fondo.fondo.repository.ProductoRepository;
import com.fondo.fondo.dto.InscripcionDetalleDto;
import com.fondo.fondo.entity.Sucursal;
import com.fondo.fondo.repository.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class InscripcionService {

    @Autowired
    private InscripcionRepository inscripcionRepository;
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private SucursalRepository sucursalRepository;
    
    @Autowired
    private LogService logService;

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
        
        // Validar que no existe una inscripción duplicada para el mismo cliente y producto
        List<Inscripcion> inscripcionesExistentes = inscripcionRepository.findByIdClienteAndIdProducto(
            inscripcion.getIdCliente(), 
            inscripcion.getIdProducto()
        );
        
        if (!inscripcionesExistentes.isEmpty()) {
            throw new RuntimeException("ya tiene una incripción con este producto");
        }
        
        // Establecer ID como null para asegurar que se genere uno nuevo
        inscripcion.setId(null);
        Inscripcion nuevaInscripcion = inscripcionRepository.save(inscripcion);
        
        // Registrar el movimiento en el log incluyendo idCliente
        String detalles = String.format("Producto: %s, Sucursal: %s, Monto: %s", 
            nuevaInscripcion.getIdProducto(), 
            nuevaInscripcion.getIdSucursal(), 
            nuevaInscripcion.getMontoInvertido());
        
        logService.registrarMovimiento("CREAR_INSCRIPCION", nuevaInscripcion.getId(), "INSCRIPCION", 
            nuevaInscripcion.getIdCliente(), detalles);
        
        return nuevaInscripcion;
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
            // Obtener la inscripción antes de eliminarla para el log
            Optional<Inscripcion> inscripcionOpt = inscripcionRepository.findById(id);
            
            inscripcionRepository.deleteById(id);
            
            // Registrar el movimiento en el log incluyendo idCliente
            if (inscripcionOpt.isPresent()) {
                Inscripcion inscripcion = inscripcionOpt.get();
                String detalles = String.format("Producto: %s, Sucursal: %s, Monto: %s", 
                    inscripcion.getIdProducto(), 
                    inscripcion.getIdSucursal(), 
                    inscripcion.getMontoInvertido());
                
                logService.registrarMovimiento("ELIMINAR_INSCRIPCION", id, "INSCRIPCION", 
                    inscripcion.getIdCliente(), detalles);
            } else {
                logService.registrarMovimiento("ELIMINAR_INSCRIPCION", id, "INSCRIPCION", 
                    null, "Inscripción eliminada");
            }
            
            return true;
        }
        return false;
    }

    public List<Inscripcion> buscarInscripcionesPorCliente(String idCliente) {
        return inscripcionRepository.findByIdCliente(idCliente);
    }

    public List<InscripcionDetalleDto> buscarInscripcionesDetalladasPorCliente(String idCliente) {
        List<Inscripcion> inscripciones = inscripcionRepository.findByIdCliente(idCliente);
        List<InscripcionDetalleDto> inscripcionesDetalladas = new ArrayList<>();
        
        for (Inscripcion inscripcion : inscripciones) {
            // Obtener producto
            Optional<Producto> productoOpt = productoRepository.findById(inscripcion.getIdProducto());
            // Obtener sucursal
            Optional<Sucursal> sucursalOpt = sucursalRepository.findById(inscripcion.getIdSucursal());
            
            if (productoOpt.isPresent() && sucursalOpt.isPresent()) {
                InscripcionDetalleDto detalle = new InscripcionDetalleDto(
                    inscripcion.getId(),
                    inscripcion.getIdCliente(),
                    productoOpt.get(),
                    sucursalOpt.get(),
                    inscripcion.getMontoInvertido(),
                    inscripcion.getFechaTransaccion()
                );
                inscripcionesDetalladas.add(detalle);
            }
        }
        
        return inscripcionesDetalladas;
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