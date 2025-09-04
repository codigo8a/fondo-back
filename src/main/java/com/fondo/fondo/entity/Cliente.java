package com.fondo.fondo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "cliente")
@Schema(description = "Entidad que representa un cliente del fondo de pensión")
public class Cliente {

    @Id
    @Schema(description = "Identificador único del cliente", example = "507f1f77bcf86cd799439011")
    private String id;

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre del cliente", example = "Juan", required = true)
    private String nombre;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Schema(description = "Apellidos del cliente", example = "Pérez García", required = true)
    private String apellidos;

    @NotBlank(message = "La ciudad es obligatoria")
    @Schema(description = "Ciudad de residencia del cliente", example = "Madrid", required = true)
    private String ciudad;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El monto debe ser mayor que 0")
    @Schema(description = "Monto del fondo de pensión", example = "15000.50", required = true)
    private BigDecimal monto;

    // Constructores
    public Cliente() {}

    public Cliente(String nombre, String apellidos, String ciudad, BigDecimal monto) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.ciudad = ciudad;
        this.monto = monto;
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

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", monto=" + monto +
                '}';
    }
}