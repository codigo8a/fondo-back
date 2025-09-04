package com.fondo.fondo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "producto")
@Schema(description = "Entidad que representa un producto de inversión")
public class Producto {

    @Id
    @Schema(description = "Identificador único del producto", example = "507f1f77bcf86cd799439011")
    private String id;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Schema(description = "Nombre del producto", example = "Fondo de Inversión Agresivo", required = true)
    private String nombre;

    @NotBlank(message = "La descripción del producto es obligatoria")
    @Schema(description = "Descripción del producto", example = "Fondo de inversión con alto riesgo y alta rentabilidad", required = true)
    private String descripcion;

    @NotNull(message = "El monto mínimo es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El monto mínimo debe ser mayor que 0")
    @Schema(description = "Monto mínimo de inversión", example = "50000", required = true)
    private BigDecimal montoMinimo;

    @NotBlank(message = "La categoría del producto es obligatoria")
    @Schema(description = "Categoría del producto", example = "FPV", required = true)
    private String categoria;

    // Constructores
    public Producto() {}

    public Producto(String nombre, String descripcion, BigDecimal montoMinimo, String categoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.montoMinimo = montoMinimo;
        this.categoria = categoria;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getMontoMinimo() {
        return montoMinimo;
    }

    public void setMontoMinimo(BigDecimal montoMinimo) {
        this.montoMinimo = montoMinimo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", montoMinimo=" + montoMinimo +
                ", categoria='" + categoria + '\'' +
                '}';
    }
}