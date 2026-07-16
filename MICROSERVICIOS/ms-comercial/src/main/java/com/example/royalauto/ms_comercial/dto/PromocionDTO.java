package com.example.royalauto.ms_comercial.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PromocionDTO {
    private Long id;
    private String imagenUrlPromo; 
    private boolean activo;
    private String tipoDescuento;  
    private Double valorDescuento;
    
    // Datos del vehículo
    private Long vehiculoId;
    private String modeloVehiculo;
    private String marcaVehiculo;
    private String categoriaVehiculo;
    private BigDecimal precioOriginal;
    private BigDecimal precioFinal; 
    private String imagenVehiculoUrl; 
}