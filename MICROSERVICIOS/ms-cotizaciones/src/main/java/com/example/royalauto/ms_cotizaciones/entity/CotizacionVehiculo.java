package com.example.royalauto.ms_cotizaciones.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cotizaciones_vehiculos", schema = "cotizaciones")
@Data
public class CotizacionVehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Datos de contacto del cliente
    @Column(name = "nombre_cliente", nullable = false, length = 150)
    private String nombreCliente;

    @Column(nullable = false, length = 100)
    private String correo;

    @Column(nullable = false, length = 20)
    private String telefono;

    @Column(length = 500)
    private String mensaje;

    // Datos de financiamiento
    @Column(name = "forma_pago", nullable = false, length = 20)
    private String formaPago = "CONTADO"; // CONTADO, FINANCIADO

    @Column(precision = 12, scale = 2)
    private BigDecimal enganche; // Solo aplica si formaPago = FINANCIADO

    @Column(name = "plazo_meses")
    private Integer plazoMeses; // Solo aplica si formaPago = FINANCIADO

    // Vehículo del catálogo por el que se pregunta.
    // No hay FK real: Vehiculo vive en ms-comercial (otro microservicio/otra BD),
    // así que solo guardamos su id y consultamos los datos por Feign cuando se necesitan.
    @Column(name = "vehiculo_id", nullable = false)
    private Long vehiculoId;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @Column(length = 30, nullable = false)
    private String estado; // PENDIENTE, CONTACTADO

    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDateTime.now();
        this.estado = "PENDIENTE";
    }
}
