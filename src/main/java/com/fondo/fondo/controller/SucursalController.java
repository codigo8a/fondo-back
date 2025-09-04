package com.fondo.fondo.controller;

import com.fondo.fondo.entity.Sucursal;
import com.fondo.fondo.service.SucursalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sucursal")
@Tag(name = "Sucursal", description = "API para gestión de sucursales")
public class SucursalController {
    
    @Autowired
    private SucursalService sucursalService;
    
    @GetMapping
    @Operation(summary = "Obtener todas las sucursales", description = "Retorna una lista de todas las sucursales")
    @ApiResponse(responseCode = "200", description = "Lista de sucursales obtenida exitosamente")
    public ResponseEntity<List<Sucursal>> obtenerTodasLasSucursales() {
        List<Sucursal> sucursales = sucursalService.obtenerTodasLasSucursales();
        return ResponseEntity.ok(sucursales);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener sucursal por ID", description = "Retorna una sucursal específica por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucursal encontrada"),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    public ResponseEntity<Sucursal> obtenerSucursalPorId(
            @Parameter(description = "ID de la sucursal", required = true)
            @PathVariable String id) {
        Optional<Sucursal> sucursal = sucursalService.obtenerSucursalPorId(id);
        return sucursal.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(summary = "Crear nueva sucursal", description = "Crea una nueva sucursal")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sucursal creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<Sucursal> crearSucursal(
            @Parameter(description = "Datos de la sucursal a crear", required = true)
            @Valid @RequestBody Sucursal sucursal) {
        try {
            Sucursal nuevaSucursal = sucursalService.crearSucursal(sucursal);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaSucursal);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar sucursal", description = "Actualiza una sucursal existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucursal actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<Sucursal> actualizarSucursal(
            @Parameter(description = "ID de la sucursal", required = true)
            @PathVariable String id,
            @Parameter(description = "Datos actualizados de la sucursal", required = true)
            @Valid @RequestBody Sucursal sucursal) {
        try {
            Sucursal sucursalActualizada = sucursalService.actualizarSucursal(id, sucursal);
            return ResponseEntity.ok(sucursalActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar sucursal", description = "Elimina una sucursal por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Sucursal eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    public ResponseEntity<Void> eliminarSucursal(
            @Parameter(description = "ID de la sucursal", required = true)
            @PathVariable String id) {
        try {
            sucursalService.eliminarSucursal(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Buscar sucursales por nombre", description = "Retorna sucursales que contengan el nombre especificado")
    @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente")
    public ResponseEntity<List<Sucursal>> buscarSucursalesPorNombre(
            @Parameter(description = "Nombre de la sucursal a buscar", required = true)
            @PathVariable String nombre) {
        List<Sucursal> sucursales = sucursalService.buscarSucursalesPorNombre(nombre);
        return ResponseEntity.ok(sucursales);
    }
    
    @GetMapping("/ciudad/{ciudad}")
    @Operation(summary = "Buscar sucursales por ciudad", description = "Retorna sucursales de la ciudad especificada")
    @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente")
    public ResponseEntity<List<Sucursal>> buscarSucursalesPorCiudad(
            @Parameter(description = "Ciudad donde buscar sucursales", required = true)
            @PathVariable String ciudad) {
        List<Sucursal> sucursales = sucursalService.buscarSucursalesPorCiudad(ciudad);
        return ResponseEntity.ok(sucursales);
    }
    
    @GetMapping("/existe/{id}")
    @Operation(summary = "Verificar si existe sucursal", description = "Verifica si existe una sucursal con el ID especificado")
    @ApiResponse(responseCode = "200", description = "Verificación completada")
    public ResponseEntity<Boolean> existeSucursal(
            @Parameter(description = "ID de la sucursal", required = true)
            @PathVariable String id) {
        boolean existe = sucursalService.existeSucursal(id);
        return ResponseEntity.ok(existe);
    }
    
    @GetMapping("/existe-nombre/{nombre}")
    @Operation(summary = "Verificar si existe sucursal por nombre", description = "Verifica si existe una sucursal con el nombre especificado")
    @ApiResponse(responseCode = "200", description = "Verificación completada")
    public ResponseEntity<Boolean> existeSucursalPorNombre(
            @Parameter(description = "Nombre de la sucursal", required = true)
            @PathVariable String nombre) {
        boolean existe = sucursalService.existeSucursalPorNombre(nombre);
        return ResponseEntity.ok(existe);
    }
    
    @GetMapping("/existe-ciudad/{ciudad}")
    @Operation(summary = "Verificar si existe sucursal en ciudad", description = "Verifica si existe al menos una sucursal en la ciudad especificada")
    @ApiResponse(responseCode = "200", description = "Verificación completada")
    public ResponseEntity<Boolean> existeSucursalEnCiudad(
            @Parameter(description = "Ciudad a verificar", required = true)
            @PathVariable String ciudad) {
        boolean existe = sucursalService.existeSucursalEnCiudad(ciudad);
        return ResponseEntity.ok(existe);
    }
}