package com.example.royalauto.ms_cotizaciones.client;

import com.example.royalauto.ms_cotizaciones.dto.VehiculoInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "ms-comercial")
public interface VehiculoClient {

    @GetMapping("/api/vehiculos/{id}")
    VehiculoInfoDTO obtenerPorId(@PathVariable("id") Long id);

    // Consulta en lote: resuelve varios vehículos con una sola llamada,
    // en vez de una llamada HTTP por cada cotización al listar
    @PostMapping("/api/vehiculos/by-ids")
    List<VehiculoInfoDTO> obtenerPorIds(@RequestBody List<Long> ids);
}
