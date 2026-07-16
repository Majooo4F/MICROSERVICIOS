package com.example.royalauto.ms_comercial.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class VehiculoDTO {

    private Long id;
    private String modelo;
    private Integer anio;
    private BigDecimal precio;
    private String descripcion;
    private Boolean disponible;

    // Solo se envía el id + nombre (no hay FK real hacia afuera del microservicio)
    private Long marcaId;
    private String marcaNombre;

    private Long categoriaId;
    private String categoriaNombre;

    private List<String> imagenes;
}
