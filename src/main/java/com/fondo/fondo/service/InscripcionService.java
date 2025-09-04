package com.fondo.fondo.service;

import com.fondo.fondo.entity.Inscripcion;
import com.fondo.fondo.entity.InscripcionConProductoCompleto;
import com.fondo.fondo.entity.Producto;
import com.fondo.fondo.entity.ProductoConSucursales;
import com.fondo.fondo.entity.Sucursal;
import com.fondo.fondo.repository.InscripcionRepository;
import com.fondo.fondo.repository.ProductoRepository;
import com.fondo.fondo.repository.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InscripcionService {
    
    @Autowired
    private InscripcionRepository inscripcionRepository;
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private SucursalRepository sucursalRepository;

    public List<Inscripcion> obtenerTodasLasInscripciones() {
        return inscripcionRepository.findAll();
    }

    public Optional<Inscripcion> obtenerInscripcionPorId(String id) {
        return inscripcionRepository.findById(id);
    }

    public Inscripcion crearInscripcion(Inscripcion inscripcion) {
        if (inscripcion.getFechaTransaccion() == null) {
            inscripcion.setFechaTransaccion(LocalDateTime.now());
        }
        return inscripcionRepository.save(inscripcion);
    }

    public Inscripcion actualizarInscripcion(String id, Inscripcion inscripcionActualizada) {
        try {
            Optional<Inscripcion> inscripcionExistente = inscripcionRepository.findById(id);
            if (inscripcionExistente.isPresent()) {
                Inscripcion inscripcion = inscripcionExistente.get();
                inscripcion.setIdCliente(inscripcionActualizada.getIdCliente());
                inscripcion.setIdProducto(inscripcionActualizada.getIdProducto());
                inscripcion.setMontoInvertido(inscripcionActualizada.getMontoInvertido());
                inscripcion.setFechaTransaccion(inscripcionActualizada.getFechaTransaccion());
                return inscripcionRepository.save(inscripcion);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean eliminarInscripcion(String id) {
        try {
            if (inscripcionRepository.existsById(id)) {
                inscripcionRepository.deleteById(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public List<InscripcionConProductoCompleto> buscarInscripcionesPorClienteCompleto(String idCliente) {
        List<Inscripcion> inscripciones = inscripcionRepository.findByIdCliente(idCliente);
        return inscripciones.stream()
                .map(this::convertirAInscripcionConProductoCompleto)
                .collect(Collectors.toList());
    }

    private InscripcionConProductoCompleto convertirAInscripcionConProductoCompleto(Inscripcion inscripcion) {
        Optional<Producto> productoOpt = productoRepository.findById(inscripcion.getIdProducto());
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            
            // Obtener información completa de las sucursales
            List<Sucursal> sucursales = producto.getDisponibleEn().stream()
                    .map(sucursalRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
            
            ProductoConSucursales productoConSucursales = new ProductoConSucursales(producto, sucursales);
            return new InscripcionConProductoCompleto(inscripcion, productoConSucursales);
        }
        return null;
    }

    public List<Inscripcion> buscarInscripcionesPorProducto(String idProducto) {
        return inscripcionRepository.findByIdProducto(idProducto);
    }

    public List<Inscripcion> buscarInscripcionesPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return inscripcionRepository.findByFechaTransaccionBetween(fechaInicio, fechaFin);
    }

    public List<Inscripcion> buscarInscripcionesPorClienteYProducto(String idCliente, String idProducto) {
        return inscripcionRepository.findByIdClienteAndIdProducto(idCliente, idProducto);
    }

    public boolean existeInscripcion(String id) {
        return inscripcionRepository.existsById(id);
    }
}