package com.example.royalauto.ms_comercial.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "imagen_vehiculo")
@Data
public class ImagenVehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // NN marcado
    @Column(length = 300, nullable = false)
    private String url;

    // Llave foránea (vehiculo_id), también tiene NN marcado
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    @JsonIgnore // Evita un bucle infinito al responder a React
    private Vehiculo vehiculo;
}