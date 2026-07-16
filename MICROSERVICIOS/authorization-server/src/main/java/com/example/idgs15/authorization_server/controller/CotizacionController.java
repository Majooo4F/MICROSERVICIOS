package com.example.idgs15.authorization_server.controller;

import com.example.idgs15.authorization_server.dto.CotizacionRequest;
import com.example.idgs15.authorization_server.entity.Cotizacion;
import com.example.idgs15.authorization_server.repository.CotizacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cotizaciones")
@RequiredArgsConstructor
public class CotizacionController {

    private final CotizacionRepository repository;

    // Público: el cliente envía su cotización sin necesidad de login
    @PostMapping
    public ResponseEntity<String> crear(@RequestBody CotizacionRequest request) {
        Cotizacion cotizacion = new Cotizacion();
        cotizacion.setNombreCliente(request.getNombreCliente());
        cotizacion.setCorreo(request.getCorreo());
        cotizacion.setTelefono(request.getTelefono());
        cotizacion.setVehiculoId(request.getVehiculoId());
        cotizacion.setVehiculoNombre(request.getVehiculoNombre());
        cotizacion.setVehiculoMarca(request.getVehiculoMarca());
        cotizacion.setMensaje(request.getMensaje());

        repository.save(cotizacion);
        return ResponseEntity.ok("Cotización enviada correctamente");
    }

    // Protegido: solo Admin consulta todas las cotizaciones recibidas
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Cotizacion>> listar() {
        return ResponseEntity.ok(repository.findAll());
    }

    // Protegido: solo Admin consulta una cotización puntual
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Cotizacion> obtenerPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Protegido: Admin marca una cotización como atendida/cerrada
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/estado")
    public ResponseEntity<String> actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        Cotizacion cotizacion = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cotización no encontrada"));

        cotizacion.setEstado(estado);
        repository.save(cotizacion);
        return ResponseEntity.ok("Estado actualizado a " + estado);
    }
}