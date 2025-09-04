package com.fondo.fondo.controller;

import com.fondo.fondo.entity.Producto;
import com.fondo.fondo.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Producto", description = "API para gestión de productos de inversión")
public class ProductoController {
    
    @Autowired
    private ProductoService productoService;
    
    @GetMapping
    @Operation(summary = "Obtener todos los productos", description = "Retorna una lista de todos los productos")
    @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente")
    public ResponseEntity<List<Producto>> obtenerTodosLosProductos() {
        List<Producto> productos = productoService.obtenerTodosLosProductos();
        return ResponseEntity.ok(productos);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID", description = "Retorna un producto específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<Producto> obtenerProductoPorId(
            @Parameter(description = "ID del producto", required = true)
            @PathVariable String id) {
        Optional<Producto> producto = productoService.obtenerProductoPorId(id);
        return producto.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(summary = "Crear nuevo producto", description = "Crea un nuevo producto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<Producto> crearProducto(
            @Parameter(description = "Datos del producto a crear", required = true)
            @Valid @RequestBody Producto producto) {
        try {
            Producto nuevoProducto = productoService.crearProducto(producto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto", description = "Actualiza un producto existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<Producto> actualizarProducto(
            @Parameter(description = "ID del producto", required = true)
            @PathVariable String id,
            @Parameter(description = "Datos actualizados del producto", required = true)
            @Valid @RequestBody Producto producto) {
        try {
            Producto productoActualizado = productoService.actualizarProducto(id, producto);
            return ResponseEntity.ok(productoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto", description = "Elimina un producto por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<Void> eliminarProducto(
            @Parameter(description = "ID del producto", required = true)
            @PathVariable String id) {
        try {
            productoService.eliminarProducto(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Buscar productos por nombre", description = "Retorna productos que contengan el nombre especificado")
    @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente")
    public ResponseEntity<List<Producto>> buscarProductosPorNombre(
            @Parameter(description = "Nombre del producto a buscar", required = true)
            @PathVariable String nombre) {
        List<Producto> productos = productoService.buscarProductosPorNombre(nombre);
        return ResponseEntity.ok(productos);
    }
    
    @GetMapping("/tipo/{tipoProducto}")
    @Operation(summary = "Buscar productos por tipo", description = "Retorna productos del tipo especificado")
    @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente")
    public ResponseEntity<List<Producto>> buscarProductosPorTipo(
            @Parameter(description = "Tipo de producto a buscar", required = true)
            @PathVariable String tipoProducto) {
        List<Producto> productos = productoService.buscarProductosPorTipo(tipoProducto);
        return ResponseEntity.ok(productos);
    }
    
    @GetMapping("/monto-maximo/{montoMaximo}")
    @Operation(summary = "Buscar productos por monto máximo", description = "Retorna productos con monto mínimo menor o igual al especificado")
    @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente")
    public ResponseEntity<List<Producto>> buscarProductosPorMontoMaximo(
            @Parameter(description = "Monto máximo a buscar", required = true)
            @PathVariable BigDecimal montoMaximo) {
        List<Producto> productos = productoService.buscarProductosPorMontoMaximo(montoMaximo);
        return ResponseEntity.ok(productos);
    }
    
    @GetMapping("/sucursal/{idSucursal}")
    @Operation(summary = "Buscar productos disponibles en sucursal", description = "Retorna productos disponibles en la sucursal especificada")
    @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente")
    public ResponseEntity<List<Producto>> buscarProductosEnSucursal(
            @Parameter(description = "ID de la sucursal", required = true)
            @PathVariable String idSucursal) {
        List<Producto> productos = productoService.buscarProductosEnSucursal(idSucursal);
        return ResponseEntity.ok(productos);
    }
    
    @GetMapping("/tipo/{tipoProducto}/sucursal/{idSucursal}")
    @Operation(summary = "Buscar productos por tipo y sucursal", description = "Retorna productos del tipo especificado disponibles en la sucursal")
    @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente")
    public ResponseEntity<List<Producto>> buscarProductosPorTipoYSucursal(
            @Parameter(description = "Tipo de producto", required = true)
            @PathVariable String tipoProducto,
            @Parameter(description = "ID de la sucursal", required = true)
            @PathVariable String idSucursal) {
        List<Producto> productos = productoService.buscarProductosPorTipoYSucursal(tipoProducto, idSucursal);
        return ResponseEntity.ok(productos);
    }
    
    @GetMapping("/existe/{id}")
    @Operation(summary = "Verificar si existe producto", description = "Verifica si existe un producto con el ID especificado")
    @ApiResponse(responseCode = "200", description = "Verificación completada")
    public ResponseEntity<Boolean> existeProducto(
            @Parameter(description = "ID del producto", required = true)
            @PathVariable String id) {
        boolean existe = productoService.existeProducto(id);
        return ResponseEntity.ok(existe);
    }
    
    @GetMapping("/existe-nombre/{nombre}")
    @Operation(summary = "Verificar si existe producto por nombre", description = "Verifica si existe un producto con el nombre especificado")
    @ApiResponse(responseCode = "200", description = "Verificación completada")
    public ResponseEntity<Boolean> existeProductoPorNombre(
            @Parameter(description = "Nombre del producto", required = true)
            @PathVariable String nombre) {
        boolean existe = productoService.existeProductoPorNombre(nombre);
        return ResponseEntity.ok(existe);
    }
    
    @GetMapping("/existe-sucursal/{idSucursal}")
    @Operation(summary = "Verificar si existe producto en sucursal", description = "Verifica si existe al menos un producto en la sucursal especificada")
    @ApiResponse(responseCode = "200", description = "Verificación completada")
    public ResponseEntity<Boolean> existeProductoEnSucursal(
            @Parameter(description = "ID de la sucursal", required = true)
            @PathVariable String idSucursal) {
        boolean existe = productoService.existeProductoEnSucursal(idSucursal);
        return ResponseEntity.ok(existe);
    }
}