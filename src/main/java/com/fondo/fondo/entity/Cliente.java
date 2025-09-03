package com.fondo.fondo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Document(collection = "cliente")
public class Cliente {
    
    @Id
    private String id;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Field("nombre")
    private String nombre;
    
    @NotBlank(message = "Los apellidos son obligatorios")
    @Field("apellidos")
    private String apellidos;
    
    @NotBlank(message = "La ciudad es obligatoria")
    @Field("ciudad")
    private String ciudad;
    
    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El monto debe ser mayor que cero")
    @Field("monto")
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