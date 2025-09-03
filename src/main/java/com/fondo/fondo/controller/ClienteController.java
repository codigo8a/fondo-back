package com.fondo.fondo.controller;

import com.fondo.fondo.entity.Cliente;
import com.fondo.fondo.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {
    
    @Autowired
    private ClienteService clienteService;
    
    // GET /api/clientes - Obtener todos los clientes
    @GetMapping
    public ResponseEntity<List<Cliente>> obtenerTodosLosClientes() {
        try {
            List<Cliente> clientes = clienteService.obtenerTodosLosClientes();
            return ResponseEntity.ok(clientes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // GET /api/clientes/{id} - Obtener cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerClientePorId(@PathVariable String id) {
        Optional<Cliente> cliente = clienteService.obtenerClientePorId(id);
        return cliente.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    // POST /api/clientes - Crear nuevo cliente
    @PostMapping
    public ResponseEntity<Cliente> crearCliente(@Valid @RequestBody Cliente cliente) {
        try {
            Cliente nuevoCliente = clienteService.crearCliente(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // PUT /api/clientes/{id} - Actualizar cliente existente
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizarCliente(@PathVariable String id, 
                                                   @Valid @RequestBody Cliente cliente) {
        try {
            Cliente clienteActualizado = clienteService.actualizarCliente(id, cliente);
            return ResponseEntity.ok(clienteActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable String id) {
        try {
            clienteService.eliminarCliente(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // GET /api/clientes/buscar/ciudad/{ciudad} - Buscar por ciudad
    @GetMapping("/buscar/ciudad/{ciudad}")
    public ResponseEntity<List<Cliente>> buscarPorCiudad(@PathVariable String ciudad) {
        try {
            List<Cliente> clientes = clienteService.buscarPorCiudad(ciudad);
            return ResponseEntity.ok(clientes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // GET /api/clientes/buscar/nombre/{nombre} - Buscar por nombre
    @GetMapping("/buscar/nombre/{nombre}")
    public ResponseEntity<List<Cliente>> buscarPorNombre(@PathVariable String nombre) {
        try {
            List<Cliente> clientes = clienteService.buscarPorNombre(nombre);
            return ResponseEntity.ok(clientes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // GET /api/clientes/buscar/apellidos/{apellidos} - Buscar por apellidos
    @GetMapping("/buscar/apellidos/{apellidos}")
    public ResponseEntity<List<Cliente>> buscarPorApellidos(@PathVariable String apellidos) {
        try {
            List<Cliente> clientes = clienteService.buscarPorApellidos(apellidos);
            return ResponseEntity.ok(clientes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // GET /api/clientes/existe/{id} - Verificar si existe un cliente
    @GetMapping("/existe/{id}")
    public ResponseEntity<Boolean> verificarExistenciaCliente(@PathVariable String id) {
        boolean existe = clienteService.existeCliente(id);
        return ResponseEntity.ok(existe);
    }
}