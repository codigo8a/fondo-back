package com.fondo.fondo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ClienteUpdateMontoDto {
    
    @NotNull(message = "El monto no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = true, message = "El monto debe ser mayor o igual a cero")
    private BigDecimal monto;
    
    // Constructores, getters y setters
    public ClienteUpdateMontoDto() {}
    
    public ClienteUpdateMontoDto(BigDecimal monto) {
        this.monto = monto;
    }
    
    public BigDecimal getMonto() {
        return monto;
    }
    
    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
}