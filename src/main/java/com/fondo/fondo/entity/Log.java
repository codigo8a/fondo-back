package com.fondo.fondo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Document(collection = "log")
@Schema(description = "Entidad que representa un registro de log de movimientos")
public class Log {

    @Id
    @Schema(description = "Identificador único del log", example = "507f1f77bcf86cd799439011")
    private String id;

    @Schema(description = "Tipo de operación realizada", example = "CREAR_INSCRIPCION")
    private String tipoOperacion;

    @Schema(description = "ID de la entidad afectada", example = "507f1f77bcf86cd799439012")
    private String entidadId;

    @Schema(description = "Tipo de entidad afectada", example = "INSCRIPCION")
    private String tipoEntidad;

    @Schema(description = "ID del cliente relacionado con el movimiento", example = "507f1f77bcf86cd799439013")
    private String idCliente;

    @Schema(description = "Detalles adicionales del movimiento")
    private String detalles;

    @Schema(description = "Fecha y hora del movimiento", example = "2025-09-05T10:30:00")
    private LocalDateTime fechaMovimiento;

    @Schema(description = "Usuario que realizó la operación", example = "sistema")
    private String usuario;

    // Constructores
    public Log() {
        this.fechaMovimiento = LocalDateTime.now();
    }

    public Log(String tipoOperacion, String entidadId, String tipoEntidad, String idCliente, String detalles) {
        this();
        this.tipoOperacion = tipoOperacion;
        this.entidadId = entidadId;
        this.tipoEntidad = tipoEntidad;
        this.idCliente = idCliente;
        this.detalles = detalles;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    public String getEntidadId() {
        return entidadId;
    }

    public void setEntidadId(String entidadId) {
        this.entidadId = entidadId;
    }

    public String getTipoEntidad() {
        return tipoEntidad;
    }

    public void setTipoEntidad(String tipoEntidad) {
        this.tipoEntidad = tipoEntidad;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public LocalDateTime getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(LocalDateTime fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}