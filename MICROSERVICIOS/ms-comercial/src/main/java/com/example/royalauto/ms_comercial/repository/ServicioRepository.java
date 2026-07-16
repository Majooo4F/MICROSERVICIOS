package com.example.royalauto.ms_comercial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.royalauto.ms_comercial.entity.Servicio;

public interface ServicioRepository extends JpaRepository<Servicio, Long> {
    
}