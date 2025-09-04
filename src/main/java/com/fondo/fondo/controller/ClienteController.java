package com.fondo.fondo.controller;

import com.fondo.fondo.entity.Cliente;
import com.fondo.fondo.service.ClienteService;
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

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Cliente", description = "API para gestión de clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    @Operation(summary = "Obtener todos los clientes", description = "Retorna una lista de todos los clientes registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class)))
    })
    public List<Cliente> obtenerTodosLosClientes() {
        return clienteService.obtenerTodosLosClientes();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cliente por ID", description = "Retorna un cliente específico por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<Cliente> obtenerClientePorId(
            @Parameter(description = "ID del cliente a buscar", required = true)
            @PathVariable String id) {
        Optional<Cliente> cliente = clienteService.obtenerClientePorId(id);
        if (cliente.isPresent()) {
            return ResponseEntity.ok(cliente.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Crear nuevo cliente", description = "Crea un nuevo cliente en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente creado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<Cliente> crearCliente(
            @Parameter(description = "Datos del cliente a crear", required = true)
            @Valid @RequestBody Cliente cliente) {
        Cliente nuevoCliente = clienteService.crearCliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cliente", description = "Actualiza los datos de un cliente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<Cliente> actualizarCliente(
            @Parameter(description = "ID del cliente a actualizar", required = true)
            @PathVariable String id,
            @Parameter(description = "Nuevos datos del cliente", required = true)
            @Valid @RequestBody Cliente cliente) {
        try {
            Cliente clienteActualizado = clienteService.actualizarCliente(id, cliente);
            return ResponseEntity.ok(clienteActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar cliente", description = "Elimina un cliente del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<Void> eliminarCliente(
            @Parameter(description = "ID del cliente a eliminar", required = true)
            @PathVariable String id) {
        boolean eliminado = clienteService.eliminarCliente(id);
        if (eliminado) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ciudad/{ciudad}")
    @Operation(summary = "Buscar clientes por ciudad", description = "Retorna todos los clientes de una ciudad específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clientes encontrados",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class)))
    })
    public List<Cliente> buscarClientesPorCiudad(
            @Parameter(description = "Nombre de la ciudad", required = true)
            @PathVariable String ciudad) {
        return clienteService.buscarClientesPorCiudad(ciudad);
    }

    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Buscar clientes por nombre", description = "Retorna todos los clientes con un nombre específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clientes encontrados",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class)))
    })
    public List<Cliente> buscarClientesPorNombre(
            @Parameter(description = "Nombre del cliente", required = true)
            @PathVariable String nombre) {
        return clienteService.buscarClientesPorNombre(nombre);
    }

    @GetMapping("/apellidos/{apellidos}")
    @Operation(summary = "Buscar clientes por apellidos", description = "Retorna todos los clientes con apellidos específicos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clientes encontrados",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class)))
    })
    public List<Cliente> buscarClientesPorApellidos(
            @Parameter(description = "Apellidos del cliente", required = true)
            @PathVariable String apellidos) {
        return clienteService.buscarClientesPorApellidos(apellidos);
    }

    @GetMapping("/existe/{id}")
    @Operation(summary = "Verificar si existe cliente", description = "Verifica si un cliente existe en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verificación completada")
    })
    public ResponseEntity<Boolean> existeCliente(
            @Parameter(description = "ID del cliente a verificar", required = true)
            @PathVariable String id) {
        boolean existe = clienteService.existeCliente(id);
        return ResponseEntity.ok(existe);
    }
}