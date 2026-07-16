package com.example.royalauto.ms_comercial.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class VehiculoDTO {
    private Long id;
    private String modelo;
    private Integer anio;
    private BigDecimal precioOriginal;
    private String imagenUrl;
    
    // --- NUEVOS CAMPOS PARA LA VISTA PREVIA ---
    private String descripcion;       
    private String marcaVehiculo;     
    private String categoriaVehiculo; 

    // Los de promoción los ignoramos para este endpoint en específico
    private Long promocionId;
    private String tipoDescuento; 
    private Double valorDescuento; 
    private BigDecimal precioFinal; 
}