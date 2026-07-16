package com.example.royalauto.ms_comercial.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "citas_servicios", schema = "comercial")
@Data
public class CitaServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Datos Personales del Cliente (Campos manuales)
    @Column(name = "nombre_cliente", nullable = false, length = 150)
    private String nombreCliente;

    @Column(nullable = false, length = 100)
    private String correo;

    @Column(nullable = false, length = 20)
    private String telefono;

    // Datos del Vehículo
    @Column(nullable = false, length = 50)
    private String marca;

    @Column(nullable = false, length = 80)
    private String modelo;

    @Column(nullable = false)
    private Integer anio;

    // Fecha de la Cita (Solo fecha, sin rangos)
    @Column(name = "fecha_cita", nullable = false)
    private LocalDate fechaCita;

    @Column(length = 500)
    private String mensaje;

    // Relación con el Servicio solicitado
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "servicio_id", nullable = false)
    private Servicio servicio;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @Column(length = 30, nullable = false)
    private String estado; // PENDIENTE, ATENDIDO

    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDateTime.now();
        this.estado = "PENDIENTE"; // Por defecto al registrarse
    }
}