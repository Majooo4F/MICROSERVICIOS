package com.example.royalauto.ms_comercial.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "categoria")
@Data
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // NN (Not Null) y UQ (Unique) están marcados
    @Column(length = 100, nullable = false, unique = true)
    private String nombre;
}