package com.fondo.fondo.service;

import com.fondo.fondo.entity.Inscripcion;
import com.fondo.fondo.repository.InscripcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InscripcionService {

    @Autowired
    private InscripcionRepository inscripcionRepository;

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

    public List<Inscripcion> buscarInscripcionesPorCliente(String idCliente) {
        return inscripcionRepository.findByIdCliente(idCliente);
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