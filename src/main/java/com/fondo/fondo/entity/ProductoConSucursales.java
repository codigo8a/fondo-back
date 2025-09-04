package com.fondo.fondo.entity;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "DTO que representa un producto con información completa de sucursales")
public class ProductoConSucursales {

    @Schema(description = "Identificador único del producto", example = "507f1f77bcf86cd799439011")
    private String id;

    @Schema(description = "Nombre del producto", example = "FPV_BTG_PACTUAL_RECAUDADORA")
    private String nombre;

    @Schema(description = "Tipo de producto", example = "FPV")
    private String tipoProducto;

    @Schema(description = "Monto mínimo de inversión", example = "75000")
    private BigDecimal montoMinimo;

    @Schema(description = "Lista de sucursales donde está disponible el producto")
    private List<Sucursal> disponibleEn;

    // Constructores
    public ProductoConSucursales() {}

    public ProductoConSucursales(Producto producto, List<Sucursal> sucursales) {
        this.id = producto.getId();
        this.nombre = producto.getNombre();
        this.tipoProducto = producto.getTipoProducto();
        this.montoMinimo = producto.getMontoMinimo();
        this.disponibleEn = sucursales;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public BigDecimal getMontoMinimo() {
        return montoMinimo;
    }

    public void setMontoMinimo(BigDecimal montoMinimo) {
        this.montoMinimo = montoMinimo;
    }

    public List<Sucursal> getDisponibleEn() {
        return disponibleEn;
    }

    public void setDisponibleEn(List<Sucursal> disponibleEn) {
        this.disponibleEn = disponibleEn;
    }
}