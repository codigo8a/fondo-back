package com.fondo.fondo.controller;

import com.fondo.fondo.entity.Inscripcion;
import com.fondo.fondo.service.InscripcionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inscripciones")
@Tag(name = "Inscripciones", description = "API para gestión de inscripciones de clientes a productos")
public class InscripcionController {

    @Autowired
    private InscripcionService inscripcionService;

    @GetMapping
    @Operation(summary = "Obtener todas las inscripciones", description = "Retorna una lista de todas las inscripciones registradas")
    @ApiResponse(responseCode = "200", description = "Lista de inscripciones obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Inscripcion.class)))
    public ResponseEntity<List<Inscripcion>> obtenerTodasLasInscripciones() {
        List<Inscripcion> inscripciones = inscripcionService.obtenerTodasLasInscripciones();
        return ResponseEntity.ok(inscripciones);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener inscripción por ID", description = "Retorna una inscripción específica por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inscripción encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Inscripcion.class))),
            @ApiResponse(responseCode = "404", description = "Inscripción no encontrada")
    })
    public ResponseEntity<Inscripcion> obtenerInscripcionPorId(
            @Parameter(description = "ID de la inscripción", required = true)
            @PathVariable String id) {
        Optional<Inscripcion> inscripcion = inscripcionService.obtenerInscripcionPorId(id);
        if (inscripcion.isPresent()) {
            return ResponseEntity.ok(inscripcion.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Crear nueva inscripción", description = "Crea una nueva inscripción de cliente a producto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Inscripción creada exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Inscripcion.class))),
            @ApiResponse(responseCode = "400", description = "Datos de inscripción inválidos")
    })
    public ResponseEntity<Inscripcion> crearInscripcion(
            @Parameter(description = "Datos de la inscripción a crear", required = true)
            @Valid @RequestBody Inscripcion inscripcion) {
        try {
            Inscripcion nuevaInscripcion = inscripcionService.crearInscripcion(inscripcion);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaInscripcion);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar inscripción", description = "Actualiza una inscripción existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inscripción actualizada exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Inscripcion.class))),
            @ApiResponse(responseCode = "404", description = "Inscripción no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos de inscripción inválidos")
    })
    public ResponseEntity<Inscripcion> actualizarInscripcion(
            @Parameter(description = "ID de la inscripción", required = true)
            @PathVariable String id,
            @Parameter(description = "Datos actualizados de la inscripción", required = true)
            @Valid @RequestBody Inscripcion inscripcion) {
        try {
            Inscripcion inscripcionActualizada = inscripcionService.actualizarInscripcion(id, inscripcion);
            if (inscripcionActualizada != null) {
                return ResponseEntity.ok(inscripcionActualizada);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar inscripción", description = "Elimina una inscripción por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Inscripción eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Inscripción no encontrada")
    })
    public ResponseEntity<Void> eliminarInscripcion(
            @Parameter(description = "ID de la inscripción", required = true)
            @PathVariable String id) {
        boolean eliminado = inscripcionService.eliminarInscripcion(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/cliente/{idCliente}")
    @Operation(summary = "Buscar inscripciones por cliente", description = "Retorna todas las inscripciones de un cliente específico")
    @ApiResponse(responseCode = "200", description = "Lista de inscripciones del cliente obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Inscripcion.class)))
    public ResponseEntity<List<Inscripcion>> buscarInscripcionesPorCliente(
            @Parameter(description = "ID del cliente", required = true)
            @PathVariable String idCliente) {
        List<Inscripcion> inscripciones = inscripcionService.buscarInscripcionesPorCliente(idCliente);
        return ResponseEntity.ok(inscripciones);
    }

    @GetMapping("/producto/{idProducto}")
    @Operation(summary = "Buscar inscripciones por producto", description = "Retorna todas las inscripciones de un producto específico")
    @ApiResponse(responseCode = "200", description = "Lista de inscripciones del producto obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Inscripcion.class)))
    public ResponseEntity<List<Inscripcion>> buscarInscripcionesPorProducto(
            @Parameter(description = "ID del producto", required = true)
            @PathVariable String idProducto) {
        List<Inscripcion> inscripciones = inscripcionService.buscarInscripcionesPorProducto(idProducto);
        return ResponseEntity.ok(inscripciones);
    }

    @GetMapping("/fecha")
    @Operation(summary = "Buscar inscripciones por rango de fechas", description = "Retorna inscripciones dentro de un rango de fechas")
    @ApiResponse(responseCode = "200", description = "Lista de inscripciones en el rango de fechas obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Inscripcion.class)))
    public ResponseEntity<List<Inscripcion>> buscarInscripcionesPorFecha(
            @Parameter(description = "Fecha de inicio (formato: yyyy-MM-ddTHH:mm:ss)", required = true)
            @RequestParam String fechaInicio,
            @Parameter(description = "Fecha de fin (formato: yyyy-MM-ddTHH:mm:ss)", required = true)
            @RequestParam String fechaFin) {
        try {
            LocalDateTime inicio = LocalDateTime.parse(fechaInicio);
            LocalDateTime fin = LocalDateTime.parse(fechaFin);
            List<Inscripcion> inscripciones = inscripcionService.buscarInscripcionesPorFecha(inicio, fin);
            return ResponseEntity.ok(inscripciones);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/existe/{id}")
    @Operation(summary = "Verificar existencia de inscripción", description = "Verifica si existe una inscripción con el ID especificado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verificación completada"),
            @ApiResponse(responseCode = "404", description = "Inscripción no encontrada")
    })
    public ResponseEntity<Boolean> existeInscripcion(
            @Parameter(description = "ID de la inscripción", required = true)
            @PathVariable String id) {
        boolean existe = inscripcionService.existeInscripcion(id);
        return ResponseEntity.ok(existe);
    }
}