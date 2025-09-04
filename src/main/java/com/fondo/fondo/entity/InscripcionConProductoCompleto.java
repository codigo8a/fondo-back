package com.fondo.fondo.entity;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "DTO que representa una inscripción con información completa del producto y sucursales")
public class InscripcionConProductoCompleto {

    @Schema(description = "Identificador único de la inscripción", example = "68b8f7d5eab2033f3a24f83d")
    private String id;

    @Schema(description = "Identificador del cliente", example = "64f0a1c2e4b0f1a2d3c4e5f9")
    private String idCliente;

    @Schema(description = "Identificador del producto", example = "64f0a1c2e4b0f1a2d3c4e5f6")
    private String idProducto;

    @Schema(description = "Monto invertido en el producto", example = "100000")
    private BigDecimal montoInvertido;

    @Schema(description = "Fecha y hora de la transacción", example = "2025-01-15T10:30:00")
    private LocalDateTime fechaTransaccion;

    @Schema(description = "Información completa del producto con sucursales")
    private ProductoConSucursales producto;

    // Constructores
    public InscripcionConProductoCompleto() {}

    public InscripcionConProductoCompleto(Inscripcion inscripcion, ProductoConSucursales producto) {
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

    public ProductoConSucursales getProducto() {
        return producto;
    }

    public void setProducto(ProductoConSucursales producto) {
        this.producto = producto;
    }
}