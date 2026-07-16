package com.example.idgs15.authorization_server.dto;

import lombok.Data;

@Data
public class CotizacionRequest {
    private String nombreCliente;
    private String correo;
    private String telefono;
    private Long vehiculoId;
    private String vehiculoNombre;
    private String vehiculoMarca;
    private String mensaje;
}