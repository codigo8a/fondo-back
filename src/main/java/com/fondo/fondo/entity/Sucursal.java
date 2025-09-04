package com.fondo.fondo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Document(collection = "sucursales")
@Schema(description = "Entidad que representa una sucursal")
public class Sucursal {
    
    @Id
    @Schema(description = "ID único de la sucursal", example = "64f0a1c2e4b0f1a2d3c4e5f8")
    private String id;
    
    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    @Schema(description = "Nombre de la sucursal", example = "Sucursal Centro")
    private String nombre;
    
    @NotBlank(message = "La ciudad es obligatoria")
    @Schema(description = "Ciudad donde se encuentra la sucursal", example = "Medellín")
    private String ciudad;
    
    // Constructor vacío
    public Sucursal() {}
    
    // Constructor con parámetros
    public Sucursal(String nombre, String ciudad) {
        this.nombre = nombre;
        this.ciudad = ciudad;
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
    
    public String getCiudad() {
        return ciudad;
    }
    
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
    
    @Override
    public String toString() {
        return "Sucursal{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", ciudad='" + ciudad + '\'' +
                '}';
    }
}