package com.example.royalauto.ms_comercial.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "marca", catalog = "comercial")
@Data
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String nombre;

    @Column(length = 300)
    private String logo;

    @Column(length = 300)
    private String descripcion;
}
