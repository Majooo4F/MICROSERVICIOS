package com.example.royalauto.ms_comercial.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "promociones")
public class Promocion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "imagen_url", nullable = false, length = 500)
    private String imagenUrl;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;
    
    private boolean activo = true;

    @Column(name = "tipo_descuento", length = 25)
    private String tipoDescuento;
    
    @Column(name = "valor_descuento")
    private Double valorDescuento;

    @Column(name = "precio_promocion", precision = 12, scale = 2)
    private BigDecimal precioPromocion;
}