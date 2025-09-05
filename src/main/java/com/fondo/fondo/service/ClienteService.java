package com.fondo.fondo.service;

import com.fondo.fondo.entity.Cliente;
import com.fondo.fondo.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    // Obtener todos los clientes
    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }
    
    // Obtener cliente por ID
    public Optional<Cliente> obtenerClientePorId(String id) {
        return clienteRepository.findById(id);
    }
    
    // Crear nuevo cliente
    public Cliente crearCliente(Cliente cliente) {
        // El ID será autogenerado por MongoDB
        cliente.setId(null);
        return clienteRepository.save(cliente);
    }
    
    // Actualizar cliente existente
    public Cliente actualizarCliente(String id, Cliente clienteActualizado) {
        Optional<Cliente> clienteExistente = clienteRepository.findById(id);
        
        if (clienteExistente.isPresent()) {
            Cliente cliente = clienteExistente.get();
            cliente.setNombre(clienteActualizado.getNombre());
            cliente.setApellidos(clienteActualizado.getApellidos());
            cliente.setCiudad(clienteActualizado.getCiudad());
            cliente.setMonto(clienteActualizado.getMonto());
            return clienteRepository.save(cliente);
        } else {
            return null;
        }
    }
    
    // Eliminar cliente
    public boolean eliminarCliente(String id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
    
    // Buscar clientes por ciudad
    public List<Cliente> buscarClientesPorCiudad(String ciudad) {
        return clienteRepository.findByCiudadIgnoreCase(ciudad);
    }
    
    // Buscar clientes por nombre
    public List<Cliente> buscarClientesPorNombre(String nombre) {
        return clienteRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    // Buscar clientes por apellidos
    public List<Cliente> buscarClientesPorApellidos(String apellidos) {
        return clienteRepository.findByApellidosContainingIgnoreCase(apellidos);
    }
    
    // Verificar si existe un cliente
    public boolean existeCliente(String id) {
        return clienteRepository.existsById(id);
    }
    
    // Actualizar solo el monto de un cliente
    public Cliente actualizarMontoCliente(String id, BigDecimal nuevoMonto) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if (clienteOpt.isEmpty()) {
            throw new RuntimeException("Cliente no encontrado con ID: " + id);
        }
        
        Cliente cliente = clienteOpt.get();
        // Permitir explícitamente cero
        if (nuevoMonto.compareTo(BigDecimal.ZERO) >= 0) {
            cliente.setMonto(nuevoMonto);
            return clienteRepository.save(cliente);
        } else {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }
    }
}