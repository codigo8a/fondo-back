package com.fondo.fondo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO para crear una nueva sucursal")
public class SucursalCreateDto {
    
    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre de la sucursal", example = "Sucursal Centro")
    private String nombre;
    
    @NotBlank(message = "La ciudad es obligatoria")
    @Schema(description = "Ciudad de la sucursal", example = "Medellín")
    private String ciudad;
    
    // Constructor vacío
    public SucursalCreateDto() {}
    
    // Constructor con parámetros
    public SucursalCreateDto(String nombre, String ciudad) {
        this.nombre = nombre;
        this.ciudad = ciudad;
    }
    
    // Getters y Setters
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getCiudad() {
        return ciudad;
    }
    
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
}