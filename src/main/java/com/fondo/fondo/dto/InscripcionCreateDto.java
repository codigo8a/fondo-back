package com.fondo.fondo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "DTO para crear una nueva inscripción")
public class InscripcionCreateDto {
    
    @NotBlank(message = "El ID del cliente es obligatorio")
    @Schema(description = "ID del cliente", example = "507f1f77bcf86cd799439011")
    private String idCliente;
    
    @NotBlank(message = "El ID del producto es obligatorio")
    @Schema(description = "ID del producto", example = "507f1f77bcf86cd799439012")
    private String idProducto;
    
    @NotNull(message = "El monto invertido es obligatorio")
    @Positive(message = "El monto invertido debe ser mayor a cero")
    @Schema(description = "Monto invertido", example = "1000000.00")
    private BigDecimal montoInvertido;
    
    @Schema(description = "Fecha de la transacción", example = "2024-01-15T10:30:00")
    private LocalDateTime fechaTransaccion;
    
    // Constructor vacío
    public InscripcionCreateDto() {}
    
    // Constructor con parámetros
    public InscripcionCreateDto(String idCliente, String idProducto, BigDecimal montoInvertido, LocalDateTime fechaTransaccion) {
        this.idCliente = idCliente;
        this.idProducto = idProducto;
        this.montoInvertido = montoInvertido;
        this.fechaTransaccion = fechaTransaccion;
    }
    
    // Getters y Setters
    public String getIdCliente() {
        return idCliente;
    }
    
    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }
    
    public String getIdProducto() {
        return idProducto;
    }
    
    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }
    
    public BigDecimal getMontoInvertido() {
        return montoInvertido;
    }
    
    public void setMontoInvertido(BigDecimal montoInvertido) {
        this.montoInvertido = montoInvertido;
    }
    
    public LocalDateTime getFechaTransaccion() {
        return fechaTransaccion;
    }
    
    public void setFechaTransaccion(LocalDateTime fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }
}