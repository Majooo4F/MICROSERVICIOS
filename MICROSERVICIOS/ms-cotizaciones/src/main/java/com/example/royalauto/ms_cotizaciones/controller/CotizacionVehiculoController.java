package com.example.royalauto.ms_cotizaciones.controller;

import lombok.RequiredArgsConstructor;
import com.example.royalauto.ms_cotizaciones.dto.CotizacionVehiculoDTO;
import com.example.royalauto.ms_cotizaciones.service.CotizacionVehiculoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/cotizaciones-vehiculos")
@RequiredArgsConstructor
public class CotizacionVehiculoController {

    private final CotizacionVehiculoService cotizacionService;

    // POST público para que cualquier cliente pida cotización desde la web
    @PostMapping
    public ResponseEntity<String> solicitarCotizacion(@RequestBody CotizacionVehiculoDTO dto) {
        cotizacionService.solicitarCotizacion(dto);
        return ResponseEntity.ok("Solicitud de cotización registrada correctamente. Nos comunicaremos contigo a la brevedad.");
    }

    // GET privado para que el administrador consulte las cotizaciones desde el dashboard
    @GetMapping
    public ResponseEntity<List<CotizacionVehiculoDTO>> listarCotizacionesAdmin() {
        return ResponseEntity.ok(cotizacionService.obtenerTodas());
    }

    // PUT para actualizar el estado de una cotización
    @PutMapping("/{id}/estado")
    public ResponseEntity<String> actualizarEstado(
            @PathVariable("id") Long id,
            @RequestParam("estado") String estado) {

        cotizacionService.actualizarEstado(id, estado.toUpperCase());
        return ResponseEntity.ok("El estado de la cotización ha sido actualizado a " + estado.toUpperCase());
    }
}
