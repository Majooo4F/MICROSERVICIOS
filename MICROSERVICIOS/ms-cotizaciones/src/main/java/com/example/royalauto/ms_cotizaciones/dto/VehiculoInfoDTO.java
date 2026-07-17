package com.example.royalauto.ms_cotizaciones.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Representación parcial del VehiculoDTO que expone ms-comercial.
 * Solo se mapean los campos que ms-cotizaciones necesita mostrar.
 */
@Data
public class VehiculoInfoDTO {
    private Long id;
    private String modelo;
    private Integer anio;
    private BigDecimal precio;
    private String marcaNombre;
}
