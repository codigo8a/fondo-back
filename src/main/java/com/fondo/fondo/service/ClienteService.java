package com.fondo.fondo.service;

import com.fondo.fondo.entity.Cliente;
import com.fondo.fondo.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            return clienteRepository.save(cliente);
        } else {
            throw new RuntimeException("Cliente no encontrado con ID: " + id);
        }
    }
    
    // Eliminar cliente
    public void eliminarCliente(String id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
        } else {
            throw new RuntimeException("Cliente no encontrado con ID: " + id);
        }
    }
    
    // Buscar clientes por ciudad
    public List<Cliente> buscarPorCiudad(String ciudad) {
        return clienteRepository.findByCiudadIgnoreCase(ciudad);
    }
    
    // Buscar clientes por nombre
    public List<Cliente> buscarPorNombre(String nombre) {
        return clienteRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    // Buscar clientes por apellidos
    public List<Cliente> buscarPorApellidos(String apellidos) {
        return clienteRepository.findByApellidosContainingIgnoreCase(apellidos);
    }
    
    // Verificar si existe un cliente
    public boolean existeCliente(String id) {
        return clienteRepository.existsById(id);
    }
}