package com.fondo.fondo.repository;

import com.fondo.fondo.entity.Producto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends MongoRepository<Producto, String> {
    
    // Buscar productos por nombre
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar productos por tipo
    List<Producto> findByTipoProductoIgnoreCase(String tipoProducto);
    
    // Buscar productos por monto mínimo menor o igual
    List<Producto> findByMontoMinimoLessThanEqual(BigDecimal montoMaximo);
    
    // Buscar productos disponibles en una sucursal específica
    List<Producto> findByDisponibleEnContaining(String idSucursal);
    
    // Buscar producto por nombre exacto
    Optional<Producto> findByNombre(String nombre);
    
    // Verificar si existe un producto por nombre
    boolean existsByNombre(String nombre);
    
    // Verificar si existe un producto en una sucursal específica
    boolean existsByDisponibleEnContaining(String idSucursal);
    
    // Buscar productos por tipo y disponibles en sucursal
    List<Producto> findByTipoProductoIgnoreCaseAndDisponibleEnContaining(String tipoProducto, String idSucursal);
}