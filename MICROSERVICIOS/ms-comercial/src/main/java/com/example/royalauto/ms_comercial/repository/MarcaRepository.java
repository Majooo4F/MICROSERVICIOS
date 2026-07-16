package com.example.royalauto.ms_comercial.repository;


import com.example.royalauto.ms_comercial.entity.Marca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MarcaRepository extends JpaRepository<Marca, Long> {
    Optional<Marca> findByNombreIgnoreCase(String nombre);
}

