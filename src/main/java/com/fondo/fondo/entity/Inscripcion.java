package com.fondo.fondo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "inscripcion")
@Schema(description = "Entidad que representa una inscripción de cliente a un producto")
public class Inscripcion {

    @Id
    @Schema(description = "Identificador único de la inscripción", example = "507f1f77bcf86cd799439011")
    private String id;

    @NotNull(message = "El ID del cliente es obligatorio")
    @Schema(description = "Identificador del cliente", example = "64f0a1c2e4b0f1a2d3c4e5f9", required = true)
    private String idCliente;

    @NotNull(message = "El ID del producto es obligatorio")
    @Schema(description = "Identificador del producto", example = "64f0a1c2e4b0f1a2d3c4e5f6", required = true)
    private String idProducto;

    @NotNull(message = "El ID de la sucursal es obligatorio")
    @Schema(description = "Identificador de la sucursal", example = "64f0a1c2e4b0f1a2d3c4e5f7", required = true)
    private String idSucursal;

    @NotNull(message = "El monto invertido es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El monto invertido debe ser mayor que 0")
    @Schema(description = "Monto invertido en el producto", example = "100000", required = true)
    private BigDecimal montoInvertido;

    @NotNull(message = "La fecha de transacción es obligatoria")
    @Schema(description = "Fecha y hora de la transacción", example = "2025-09-02T22:30:00", required = true)
    private LocalDateTime fechaTransaccion;

    // Constructores
    public Inscripcion() {}

    public Inscripcion(String idCliente, String idProducto, String idSucursal, BigDecimal montoInvertido, LocalDateTime fechaTransaccion) {
        this.idCliente = idCliente;
        this.idProducto = idProducto;
        this.idSucursal = idSucursal;
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

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(String idSucursal) {
        this.idSucursal = idSucursal;
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

    @Override
    public String toString() {
        return "Inscripcion{" +
                "id='" + id + '\'' +
                ", idCliente='" + idCliente + '\'' +
                ", idProducto='" + idProducto + '\'' +
                ", idSucursal='" + idSucursal + '\'' +
                ", montoInvertido=" + montoInvertido +
                ", fechaTransaccion=" + fechaTransaccion +
                '}';
    }
}