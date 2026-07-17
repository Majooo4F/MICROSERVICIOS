package com.example.royalauto.ms_comercial.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ServicioDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precioBase;
    private String imagenUrl;
}
