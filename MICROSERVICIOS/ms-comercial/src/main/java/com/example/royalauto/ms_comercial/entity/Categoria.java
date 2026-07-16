package com.example.royalauto.ms_comercial.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "categoria", schema = "comercial")
@Data
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String nombre;
}

