package com.fondo.fondo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Document(collection = "producto")
@Schema(description = "Entidad que representa un producto de inversión")
public class Producto {

    @Id
    @Schema(description = "Identificador único del producto", example = "507f1f77bcf86cd799439011")
    private String id;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Schema(description = "Nombre del producto", example = "FPV_BTG_PACTUAL_ECOPETROL", required = true)
    private String nombre;

    @NotBlank(message = "El tipo de producto es obligatorio")
    @Schema(description = "Tipo de producto", example = "FPV", required = true)
    private String tipoProducto;

    @NotNull(message = "El monto mínimo es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El monto mínimo debe ser mayor que 0")
    @Schema(description = "Monto mínimo de inversión", example = "125000", required = true)
    private BigDecimal montoMinimo;

    @NotEmpty(message = "El producto debe estar disponible en al menos una sucursal")
    @Schema(description = "Lista de IDs de sucursales donde está disponible el producto", example = "[\"64f0a1c2e4b0f1a2d3c4e5f7\", \"64f0a1c2e4b0f1a2d3c4e5f8\"]", required = true)
    private List<String> disponibleEn;

    // Constructores
    public Producto() {}

    public Producto(String nombre, String tipoProducto, BigDecimal montoMinimo, List<String> disponibleEn) {
        this.nombre = nombre;
        this.tipoProducto = tipoProducto;
        this.montoMinimo = montoMinimo;
        this.disponibleEn = disponibleEn;
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

    public List<String> getDisponibleEn() {
        return disponibleEn;
    }

    public void setDisponibleEn(List<String> disponibleEn) {
        this.disponibleEn = disponibleEn;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", tipoProducto='" + tipoProducto + '\'' +
                ", montoMinimo=" + montoMinimo +
                ", disponibleEn=" + disponibleEn +
                '}';
    }
}