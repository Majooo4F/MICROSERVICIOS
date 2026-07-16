package com.example.royalauto.ms_comercial.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "vehiculo")
@Data
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer anio;

    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false)
    private Boolean disponible;

    @Column(length = 100, nullable = false)
    private String modelo;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal precio;

    // --- RELACIONES ACTUALIZADAS ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marca_id", nullable = false)
    private Marca marca;

    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ImagenVehiculo> imagenes;
}