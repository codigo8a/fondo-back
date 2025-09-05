package com.fondo.fondo.controller;

import com.fondo.fondo.entity.Log;
import com.fondo.fondo.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
@Tag(name = "Logs", description = "API para consultar logs de movimientos")
public class LogController {

    @Autowired
    private LogService logService;

    @GetMapping
    @Operation(summary = "Obtener todos los logs", description = "Retorna una lista de todos los logs registrados")
    @ApiResponse(responseCode = "200", description = "Lista de logs obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Log.class)))
    public ResponseEntity<List<Log>> obtenerTodosLosLogs() {
        List<Log> logs = logService.obtenerTodosLosLogs();
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/cliente/{idCliente}")
    @Operation(summary = "Obtener logs por cliente", description = "Retorna todos los logs de movimientos de un cliente específico")
    @ApiResponse(responseCode = "200", description = "Logs del cliente obtenidos exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Log.class)))
    public ResponseEntity<List<Log>> obtenerLogsPorCliente(
            @Parameter(description = "ID del cliente", example = "507f1f77bcf86cd799439013")
            @PathVariable String idCliente) {
        List<Log> logs = logService.obtenerLogsPorCliente(idCliente);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/cliente/{idCliente}/operacion/{tipoOperacion}")
    @Operation(summary = "Obtener logs por cliente y operación", description = "Retorna logs de un cliente filtrados por tipo de operación")
    @ApiResponse(responseCode = "200", description = "Logs filtrados obtenidos exitosamente")
    public ResponseEntity<List<Log>> obtenerLogsPorClienteYOperacion(
            @Parameter(description = "ID del cliente", example = "507f1f77bcf86cd799439013")
            @PathVariable String idCliente,
            @Parameter(description = "Tipo de operación (ej: CREAR_INSCRIPCION, ELIMINAR_INSCRIPCION)")
            @PathVariable String tipoOperacion) {
        List<Log> logs = logService.obtenerLogsPorClienteYOperacion(idCliente, tipoOperacion);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/inscripciones")
    @Operation(summary = "Obtener logs de inscripciones", description = "Retorna todos los logs relacionados con inscripciones")
    @ApiResponse(responseCode = "200", description = "Logs de inscripciones obtenidos exitosamente")
    public ResponseEntity<List<Log>> obtenerLogsInscripciones() {
        List<Log> logs = logService.obtenerLogsInscripciones();
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/operacion/{tipoOperacion}")
    @Operation(summary = "Obtener logs por tipo de operación", description = "Retorna logs filtrados por tipo de operación")
    public ResponseEntity<List<Log>> obtenerLogsPorOperacion(
            @Parameter(description = "Tipo de operación (ej: CREAR_INSCRIPCION, ELIMINAR_INSCRIPCION)")
            @PathVariable String tipoOperacion) {
        List<Log> logs = logService.obtenerLogsPorTipoOperacion(tipoOperacion);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/entidad/{entidadId}")
    @Operation(summary = "Obtener logs por ID de entidad", description = "Retorna todos los logs de una entidad específica")
    public ResponseEntity<List<Log>> obtenerLogsPorEntidad(
            @Parameter(description = "ID de la entidad")
            @PathVariable String entidadId) {
        List<Log> logs = logService.obtenerLogsPorEntidad(entidadId);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/cliente/{idCliente}/fecha")
    @Operation(summary = "Obtener logs por cliente y rango de fechas", description = "Retorna logs de un cliente en un rango de fechas específico")
    @ApiResponse(responseCode = "200", description = "Logs del cliente por fecha obtenidos exitosamente")
    public ResponseEntity<List<Log>> obtenerLogsPorClienteYFecha(
            @Parameter(description = "ID del cliente", example = "507f1f77bcf86cd799439013")
            @PathVariable String idCliente,
            @Parameter(description = "Fecha de inicio (formato: yyyy-MM-ddTHH:mm:ss)", required = true)
            @RequestParam String fechaInicio,
            @Parameter(description = "Fecha de fin (formato: yyyy-MM-ddTHH:mm:ss)", required = true)
            @RequestParam String fechaFin) {
        try {
            LocalDateTime inicio = LocalDateTime.parse(fechaInicio);
            LocalDateTime fin = LocalDateTime.parse(fechaFin);
            List<Log> logs = logService.obtenerLogsPorClienteYFecha(idCliente, inicio, fin);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}