package com.example.royalauto.ms_comercial.controller;

import lombok.RequiredArgsConstructor;
import com.example.royalauto.ms_comercial.dto.CitaRequestDTO;
import com.example.royalauto.ms_comercial.service.ServicioAutomotrizService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/citas-servicios")
@RequiredArgsConstructor
public class CitaServicioController {

    private final ServicioAutomotrizService servicioService;

    // POST público para que cualquier cliente agende desde la web
    @PostMapping
    public ResponseEntity<String> registrarCita(@RequestBody CitaRequestDTO dto) {
        servicioService.agendarCita(dto);
        return ResponseEntity.ok("Cita de servicio agendada correctamente. Nos comunicaremos contigo a la brevedad.");
    }

    // GET privado para que el administrador consulte las citas desde el dashboard
    @GetMapping
    public ResponseEntity<List<CitaRequestDTO>> listarCitasAdmin() {
        return ResponseEntity.ok(servicioService.obtenerTodasLasCitas());
    }

    // PUT para actualizar el estado de un servicio
    @PutMapping("/{id}/estado")
    public ResponseEntity<String> actualizarEstado(
            @PathVariable("id") Long id, 
            @RequestParam("estado") String estado) {
        
        servicioService.actualizarEstadoCita(id, estado.toUpperCase());
        return ResponseEntity.ok("El estado de la cita ha sido actualizado a " + estado.toUpperCase());
    }
}