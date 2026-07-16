package com.example.royalauto.ms_comercial.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CitaRequestDTO {
    private Long id;
    private String nombreCliente;
    private String correo;
    private String telefono;
    private String marca;
    private String modelo;
    private Integer anio;
    private LocalDate fechaCita; // Formato YYYY-MM-DD
    private String mensaje;
    private Long servicioId; // id del servicio seleccionado
    private String nombreServicio; // Para mostrarle el nombre al admin sin que busque el ID
    private String estado;
    private LocalDateTime fechaRegistro;
}    
