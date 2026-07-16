package com.example.royalauto.ms_comercial.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "marca")
@Data
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // NN (Not Null) y UQ (Unique) están marcados
    @Column(length = 100, nullable = false, unique = true)
    private String nombre;

    @Column(length = 300)
    private String descripcion;

    @Column(length = 300)
    private String logo;
}