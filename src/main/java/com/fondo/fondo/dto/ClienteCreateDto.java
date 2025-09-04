package com.fondo.fondo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

@Schema(description = "DTO para crear un nuevo cliente")
public class ClienteCreateDto {
    
    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre del cliente", example = "Juan")
    private String nombre;
    
    @NotBlank(message = "Los apellidos son obligatorios")
    @Schema(description = "Apellidos del cliente", example = "Pérez García")
    private String apellidos;
    
    @NotBlank(message = "La ciudad es obligatoria")
    @Schema(description = "Ciudad del cliente", example = "Medellín")
    private String ciudad;
    
    @NotNull(message = "El monto es obligatorio")
    @PositiveOrZero(message = "El monto debe ser mayor o igual a cero")
    @Schema(description = "Monto del cliente", example = "1000000.00")
    private BigDecimal monto;
    
    // Constructor vacío
    public ClienteCreateDto() {}
    
    // Constructor con parámetros
    public ClienteCreateDto(String nombre, String apellidos, String ciudad, BigDecimal monto) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.ciudad = ciudad;
        this.monto = monto;
    }
    
    // Getters y Setters
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
}