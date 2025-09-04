package com.fondo.fondo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "DTO para crear un nuevo producto")
public class ProductoCreateDto {
    
    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre del producto", example = "Fondo de Inversión Agresivo")
    private String nombre;
    
    @NotBlank(message = "El tipo de producto es obligatorio")
    @Schema(description = "Tipo de producto", example = "Fondo de Inversión")
    private String tipoProducto;
    
    @NotNull(message = "El monto mínimo es obligatorio")
    @PositiveOrZero(message = "El monto mínimo debe ser mayor o igual a cero")
    @Schema(description = "Monto mínimo de inversión", example = "500000.00")
    private BigDecimal montoMinimo;
    
    @Schema(description = "Lista de IDs de sucursales donde está disponible el producto")
    private List<String> disponibleEn;
    
    // Constructor vacío
    public ProductoCreateDto() {}
    
    // Constructor con parámetros
    public ProductoCreateDto(String nombre, String tipoProducto, BigDecimal montoMinimo, List<String> disponibleEn) {
        this.nombre = nombre;
        this.tipoProducto = tipoProducto;
        this.montoMinimo = montoMinimo;
        this.disponibleEn = disponibleEn;
    }
    
    // Getters y Setters
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
    
    public List<String> getDisponibleEn() {
        return disponibleEn;
    }
    
    public void setDisponibleEn(List<String> disponibleEn) {
        this.disponibleEn = disponibleEn;
    }
}