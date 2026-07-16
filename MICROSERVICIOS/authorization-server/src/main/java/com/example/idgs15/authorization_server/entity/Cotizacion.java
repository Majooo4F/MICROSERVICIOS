package com.example.idgs15.authorization_server.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "cotizacion", schema = "comercial")
public class Cotizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreCliente;
    private String correo;
    private String telefono;

    private Long vehiculoId;       // referencia al vehículo real en ms_comercial
    private String vehiculoNombre; // snapshot del modelo al momento de cotizar
    private String vehiculoMarca;  // snapshot de la marca

    private String mensaje;

    private LocalDateTime fechaSolicitud = LocalDateTime.now();
    private String estado = "PENDIENTE"; // PENDIENTE, ATENDIDA, CERRADA
}