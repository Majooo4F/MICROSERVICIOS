package com.example.royalauto.ms_cotizaciones.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CotizacionVehiculoDTO {
    private Long id;
    private String nombreCliente;
    private String correo;
    private String telefono;
    private String mensaje;

    private String formaPago; // CONTADO, FINANCIADO
    private BigDecimal enganche; // Solo aplica si formaPago = FINANCIADO
    private Integer plazoMeses; // Solo aplica si formaPago = FINANCIADO

    private Long vehiculoId; // id del vehículo seleccionado del catálogo (vive en ms-comercial)

    // Datos del vehículo (obtenidos vía Feign a ms-comercial) para mostrarle al admin sin que busque el ID
    private String vehiculoMarca;
    private String vehiculoModelo;
    private Integer vehiculoAnio;
    private BigDecimal vehiculoPrecio;

    private String estado;
    private LocalDateTime fechaRegistro;
}
