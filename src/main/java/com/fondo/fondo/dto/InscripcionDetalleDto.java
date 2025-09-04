package com.fondo.fondo.dto;

import com.fondo.fondo.entity.Producto;
import com.fondo.fondo.entity.Sucursal;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "DTO que contiene los detalles completos de una inscripción con información del producto y sucursal")
public class InscripcionDetalleDto {

    @Schema(description = "ID de la inscripción", example = "64f0a1c2e4b0f1a2d3c4e5f9")
    private String id;

    @Schema(description = "ID del cliente", example = "64f0a1c2e4b0f1a2d3c4e5f6")
    private String idCliente;

    @Schema(description = "Información completa del producto")
    private Producto producto;

    @Schema(description = "Información completa de la sucursal")
    private Sucursal sucursal;

    @Schema(description = "Monto invertido", example = "500000")
    private BigDecimal montoInvertido;

    @Schema(description = "Fecha y hora de la transacción")
    private LocalDateTime fechaTransaccion;

    // Constructores
    public InscripcionDetalleDto() {}

    public InscripcionDetalleDto(String id, String idCliente, Producto producto, Sucursal sucursal, 
                                BigDecimal montoInvertido, LocalDateTime fechaTransaccion) {
        this.id = id;
        this.idCliente = idCliente;
        this.producto = producto;
        this.sucursal = sucursal;
        this.montoInvertido = montoInvertido;
        this.fechaTransaccion = fechaTransaccion;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
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