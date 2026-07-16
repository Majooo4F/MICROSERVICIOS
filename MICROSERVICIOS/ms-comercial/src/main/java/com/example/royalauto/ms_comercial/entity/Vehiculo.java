package com.example.royalauto.ms_comercial.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehiculo", schema = "comercial")
@Data
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String modelo;

    @Column(nullable = false)
    private Integer anio;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;

    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false)
    private Boolean disponible = true;

    // Relación DENTRO del mismo microservicio -> sí se usa FK real
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "marca_id", nullable = false)
    private Marca marca;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImagenVehiculo> imagenes = new ArrayList<>();
}

