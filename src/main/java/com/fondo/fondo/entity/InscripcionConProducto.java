package com.fondo.fondo.entity;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "DTO que representa una inscripción con información del producto")
public class InscripcionConProducto {

    @Schema(description = "Identificador único de la inscripción", example = "507f1f77bcf86cd799439011")
    private String id;

    @Schema(description = "Identificador del cliente", example = "64f0a1c2e4b0f1a2d3c4e5f9")
    private String idCliente;

    @Schema(description = "Identificador del producto", example = "64f0a1c2e4b0f1a2d3c4e5f6")
    private String idProducto;

    @Schema(description = "Monto invertido en el producto", example = "100000")
    private BigDecimal montoInvertido;

    @Schema(description = "Fecha y hora de la transacción", example = "2025-09-02T22:30:00")
    private LocalDateTime fechaTransaccion;

    @Schema(description = "Información del producto")
    private Producto producto;

    // Constructores
    public InscripcionConProducto() {}

    public InscripcionConProducto(Inscripcion inscripcion, Producto producto) {
        this.id = inscripcion.getId();
        this.idCliente = inscripcion.getIdCliente();
        this.idProducto = inscripcion.getIdProducto();
        this.montoInvertido = inscripcion.getMontoInvertido();
        this.fechaTransaccion = inscripcion.getFechaTransaccion();
        this.producto = producto;
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

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}