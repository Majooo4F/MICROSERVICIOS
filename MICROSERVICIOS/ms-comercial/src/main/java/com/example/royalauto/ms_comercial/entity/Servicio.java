package com.example.royalauto.ms_comercial.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "servicios", schema = "comercial")
@Data
public class Servicio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 150)
    private String nombre;
    
    @Column(length = 500)
    private String descripcion;
    
    @Column(name = "precio_base", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioBase;
    
    @Column(name = "imagen_url", length = 300)
    private String imagenUrl; // Aquí guardas la ruta de la imagen o el nombre del ícono
}