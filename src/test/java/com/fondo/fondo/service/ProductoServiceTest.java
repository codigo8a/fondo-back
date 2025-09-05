package com.fondo.fondo.service;

import com.fondo.fondo.entity.Producto;
import com.fondo.fondo.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto productoTest;
    private List<Producto> productosTest;

    @BeforeEach
    void setUp() {
        productoTest = new Producto();
        productoTest.setId("507f1f77bcf86cd799439011");
        productoTest.setNombre("FPV_BTG_PACTUAL_ECOPETROL");
        productoTest.setTipoProducto("FPV");
        productoTest.setMontoMinimo(new BigDecimal("125000"));
        productoTest.setDisponibleEn(Arrays.asList("64f0a1c2e4b0f1a2d3c4e5f7", "64f0a1c2e4b0f1a2d3c4e5f8"));

        Producto producto2 = new Producto();
        producto2.setId("507f1f77bcf86cd799439012");
        producto2.setNombre("CDT_BANCO_POPULAR");
        producto2.setTipoProducto("CDT");
        producto2.setMontoMinimo(new BigDecimal("50000"));
        producto2.setDisponibleEn(Arrays.asList("64f0a1c2e4b0f1a2d3c4e5f7"));

        productosTest = Arrays.asList(productoTest, producto2);
    }

    @Test
    void testObtenerTodosLosProductos_Success() {
        // Arrange
        when(productoRepository.findAll()).thenReturn(productosTest);

        // Act
        List<Producto> resultado = productoService.obtenerTodosLosProductos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void testObtenerProductoPorId_Success() {
        // Arrange
        String id = "507f1f77bcf86cd799439011";
        when(productoRepository.findById(id)).thenReturn(Optional.of(productoTest));

        // Act
        Optional<Producto> resultado = productoService.obtenerProductoPorId(id);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("FPV_BTG_PACTUAL_ECOPETROL", resultado.get().getNombre());
        verify(productoRepository, times(1)).findById(id);
    }

    @Test
    void testObtenerProductoPorId_NotFound() {
        // Arrange
        String id = "507f1f77bcf86cd799439999";
        when(productoRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Producto> resultado = productoService.obtenerProductoPorId(id);

        // Assert
        assertFalse(resultado.isPresent());
        verify(productoRepository, times(1)).findById(id);
    }

    @Test
    void testCrearProducto_Success() {
        // Arrange
        Producto nuevoProducto = new Producto();
        nuevoProducto.setNombre("NUEVO_PRODUCTO");
        nuevoProducto.setTipoProducto("FIC");
        nuevoProducto.setMontoMinimo(new BigDecimal("100000"));
        nuevoProducto.setDisponibleEn(Arrays.asList("64f0a1c2e4b0f1a2d3c4e5f7"));

        Producto productoGuardado = new Producto();
        productoGuardado.setId("507f1f77bcf86cd799439013");
        productoGuardado.setNombre("NUEVO_PRODUCTO");
        productoGuardado.setTipoProducto("FIC");
        productoGuardado.setMontoMinimo(new BigDecimal("100000"));
        productoGuardado.setDisponibleEn(Arrays.asList("64f0a1c2e4b0f1a2d3c4e5f7"));

        when(productoRepository.save(any(Producto.class))).thenReturn(productoGuardado);

        // Act
        Producto resultado = productoService.crearProducto(nuevoProducto);

        // Assert
        assertNotNull(resultado);
        assertEquals("507f1f77bcf86cd799439013", resultado.getId());
        assertEquals("NUEVO_PRODUCTO", resultado.getNombre());
        assertNull(nuevoProducto.getId()); // Verificar que se estableció a null
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void testActualizarProducto_Success() {
        // Arrange
        String id = "507f1f77bcf86cd799439011";
        Producto productoActualizado = new Producto();
        productoActualizado.setNombre("PRODUCTO_ACTUALIZADO");
        productoActualizado.setTipoProducto("FIC");
        productoActualizado.setMontoMinimo(new BigDecimal("200000"));
        productoActualizado.setDisponibleEn(Arrays.asList("64f0a1c2e4b0f1a2d3c4e5f9"));

        when(productoRepository.findById(id)).thenReturn(Optional.of(productoTest));
        when(productoRepository.save(any(Producto.class))).thenReturn(productoTest);

        // Act
        Producto resultado = productoService.actualizarProducto(id, productoActualizado);

        // Assert
        assertNotNull(resultado);
        verify(productoRepository, times(1)).findById(id);
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void testActualizarProducto_NotFound() {
        // Arrange
        String id = "507f1f77bcf86cd799439999";
        Producto productoActualizado = new Producto();
        when(productoRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoService.actualizarProducto(id, productoActualizado);
        });

        assertEquals("Producto no encontrado con ID: " + id, exception.getMessage());
        verify(productoRepository, times(1)).findById(id);
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void testEliminarProducto_Success() {
        // Arrange
        String id = "507f1f77bcf86cd799439011";
        when(productoRepository.existsById(id)).thenReturn(true);
        doNothing().when(productoRepository).deleteById(id);

        // Act
        assertDoesNotThrow(() -> productoService.eliminarProducto(id));

        // Assert
        verify(productoRepository, times(1)).existsById(id);
        verify(productoRepository, times(1)).deleteById(id);
    }

    @Test
    void testEliminarProducto_NotFound() {
        // Arrange
        String id = "507f1f77bcf86cd799439999";
        when(productoRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoService.eliminarProducto(id);
        });

        assertEquals("Producto no encontrado con ID: " + id, exception.getMessage());
        verify(productoRepository, times(1)).existsById(id);
        verify(productoRepository, never()).deleteById(id);
    }

    @Test
    void testBuscarProductosPorNombre_Success() {
        // Arrange
        String nombre = "BTG";
        List<Producto> productosEncontrados = Arrays.asList(productoTest);
        when(productoRepository.findByNombreContainingIgnoreCase(nombre)).thenReturn(productosEncontrados);

        // Act
        List<Producto> resultado = productoService.buscarProductosPorNombre(nombre);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(productoRepository, times(1)).findByNombreContainingIgnoreCase(nombre);
    }

    @Test
    void testBuscarProductosPorTipo_Success() {
        // Arrange
        String tipo = "FPV";
        List<Producto> productosEncontrados = Arrays.asList(productoTest);
        when(productoRepository.findByTipoProductoIgnoreCase(tipo)).thenReturn(productosEncontrados);

        // Act
        List<Producto> resultado = productoService.buscarProductosPorTipo(tipo);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("FPV", resultado.get(0).getTipoProducto());
        verify(productoRepository, times(1)).findByTipoProductoIgnoreCase(tipo);
    }

    @Test
    void testBuscarProductosPorMontoMaximo_Success() {
        // Arrange
        BigDecimal montoMaximo = new BigDecimal("150000");
        List<Producto> productosEncontrados = Arrays.asList(productoTest);
        when(productoRepository.findByMontoMinimoLessThanEqual(montoMaximo)).thenReturn(productosEncontrados);

        // Act
        List<Producto> resultado = productoService.buscarProductosPorMontoMaximo(montoMaximo);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(productoRepository, times(1)).findByMontoMinimoLessThanEqual(montoMaximo);
    }

    @Test
    void testBuscarProductosEnSucursal_Success() {
        // Arrange
        String idSucursal = "64f0a1c2e4b0f1a2d3c4e5f7";
        when(productoRepository.findByDisponibleEnContaining(idSucursal)).thenReturn(productosTest);

        // Act
        List<Producto> resultado = productoService.buscarProductosEnSucursal(idSucursal);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(productoRepository, times(1)).findByDisponibleEnContaining(idSucursal);
    }

    @Test
    void testBuscarProductosPorTipoYSucursal_Success() {
        // Arrange
        String tipo = "FPV";
        String idSucursal = "64f0a1c2e4b0f1a2d3c4e5f7";
        List<Producto> productosEncontrados = Arrays.asList(productoTest);
        when(productoRepository.findByTipoProductoIgnoreCaseAndDisponibleEnContaining(tipo, idSucursal))
                .thenReturn(productosEncontrados);

        // Act
        List<Producto> resultado = productoService.buscarProductosPorTipoYSucursal(tipo, idSucursal);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(productoRepository, times(1)).findByTipoProductoIgnoreCaseAndDisponibleEnContaining(tipo, idSucursal);
    }

    @Test
    void testExisteProducto_True() {
        // Arrange
        String id = "507f1f77bcf86cd799439011";
        when(productoRepository.existsById(id)).thenReturn(true);

        // Act
        boolean resultado = productoService.existeProducto(id);

        // Assert
        assertTrue(resultado);
        verify(productoRepository, times(1)).existsById(id);
    }

    @Test
    void testExisteProducto_False() {
        // Arrange
        String id = "507f1f77bcf86cd799439999";
        when(productoRepository.existsById(id)).thenReturn(false);

        // Act
        boolean resultado = productoService.existeProducto(id);

        // Assert
        assertFalse(resultado);
        verify(productoRepository, times(1)).existsById(id);
    }

    @Test
    void testExisteProductoPorNombre_True() {
        // Arrange
        String nombre = "FPV_BTG_PACTUAL_ECOPETROL";
        when(productoRepository.existsByNombre(nombre)).thenReturn(true);

        // Act
        boolean resultado = productoService.existeProductoPorNombre(nombre);

        // Assert
        assertTrue(resultado);
        verify(productoRepository, times(1)).existsByNombre(nombre);
    }

    @Test
    void testExisteProductoEnSucursal_True() {
        // Arrange
        String idSucursal = "64f0a1c2e4b0f1a2d3c4e5f7";
        when(productoRepository.existsByDisponibleEnContaining(idSucursal)).thenReturn(true);

        // Act
        boolean resultado = productoService.existeProductoEnSucursal(idSucursal);

        // Assert
        assertTrue(resultado);
        verify(productoRepository, times(1)).existsByDisponibleEnContaining(idSucursal);
    }
}