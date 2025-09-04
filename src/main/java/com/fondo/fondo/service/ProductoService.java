package com.fondo.fondo.service;

import com.fondo.fondo.entity.Producto;
import com.fondo.fondo.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    
    @Autowired
    private ProductoRepository productoRepository;
    
    // Obtener todos los productos
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }
    
    // Obtener producto por ID
    public Optional<Producto> obtenerProductoPorId(String id) {
        return productoRepository.findById(id);
    }
    
    // Crear nuevo producto
    public Producto crearProducto(Producto producto) {
        return productoRepository.save(producto);
    }
    
    // Actualizar producto
    public Producto actualizarProducto(String id, Producto productoActualizado) {
        return productoRepository.findById(id)
                .map(producto -> {
                    producto.setNombre(productoActualizado.getNombre());
                    producto.setTipoProducto(productoActualizado.getTipoProducto());
                    producto.setMontoMinimo(productoActualizado.getMontoMinimo());
                    producto.setDisponibleEn(productoActualizado.getDisponibleEn());
                    return productoRepository.save(producto);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }
    
    // Eliminar producto
    public void eliminarProducto(String id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }
    }
    
    // Buscar productos por nombre
    public List<Producto> buscarProductosPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    // Buscar productos por tipo
    public List<Producto> buscarProductosPorTipo(String tipoProducto) {
        return productoRepository.findByTipoProductoIgnoreCase(tipoProducto);
    }
    
    // Buscar productos por monto máximo
    public List<Producto> buscarProductosPorMontoMaximo(BigDecimal montoMaximo) {
        return productoRepository.findByMontoMinimoLessThanEqual(montoMaximo);
    }
    
    // Buscar productos disponibles en sucursal
    public List<Producto> buscarProductosEnSucursal(String idSucursal) {
        return productoRepository.findByDisponibleEnContaining(idSucursal);
    }
    
    // Buscar productos por tipo y sucursal
    public List<Producto> buscarProductosPorTipoYSucursal(String tipoProducto, String idSucursal) {
        return productoRepository.findByTipoProductoIgnoreCaseAndDisponibleEnContaining(tipoProducto, idSucursal);
    }
    
    // Verificar si existe producto
    public boolean existeProducto(String id) {
        return productoRepository.existsById(id);
    }
    
    // Verificar si existe producto por nombre
    public boolean existeProductoPorNombre(String nombre) {
        return productoRepository.existsByNombre(nombre);
    }
    
    // Verificar si existe producto en sucursal
    public boolean existeProductoEnSucursal(String idSucursal) {
        return productoRepository.existsByDisponibleEnContaining(idSucursal);
    }
}