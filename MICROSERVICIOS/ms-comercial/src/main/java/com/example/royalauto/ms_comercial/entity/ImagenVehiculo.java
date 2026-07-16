package com.example.royalauto.ms_comercial.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "imagen_vehiculo", schema = "comercial")
@Data
public class ImagenVehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 300)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;
}

