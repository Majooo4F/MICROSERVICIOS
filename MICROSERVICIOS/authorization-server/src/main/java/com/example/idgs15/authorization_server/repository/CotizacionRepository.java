package com.example.idgs15.authorization_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.idgs15.authorization_server.entity.Cotizacion;

public interface CotizacionRepository extends JpaRepository<Cotizacion, Long> {
}