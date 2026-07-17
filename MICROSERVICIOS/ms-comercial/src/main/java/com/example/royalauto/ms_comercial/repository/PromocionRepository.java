package com.example.royalauto.ms_comercial.repository;

import com.example.royalauto.ms_comercial.entity.Promocion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromocionRepository extends JpaRepository<Promocion, Long> {
    List<Promocion> findByActivoTrue();

    Optional<Promocion> findByVehiculo_IdAndActivoTrue(Long vehiculoId);
}