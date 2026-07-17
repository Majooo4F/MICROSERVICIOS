package com.example.royalauto.ms_comercial.controller;

import lombok.RequiredArgsConstructor;
import com.example.royalauto.ms_comercial.dto.ServicioDTO;
import com.example.royalauto.ms_comercial.service.ServicioAutomotrizService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/servicios")
@RequiredArgsConstructor
public class ServicioController {

    private final ServicioAutomotrizService servicioService;

    // listar los servicios
    @GetMapping
    public ResponseEntity<List<ServicioDTO>> listarServicios() {
        List<ServicioDTO> servicios = servicioService.obtenerCatalogoServicios();
        return ResponseEntity.ok(servicios);
    }

    // registrar un nuevo servicio
    @org.springframework.web.bind.annotation.PostMapping
    public org.springframework.http.ResponseEntity<String> crearServicio(@org.springframework.web.bind.annotation.RequestBody ServicioDTO dto) {
    servicioService.guardarServicio(dto);
    return ResponseEntity.ok().build();
    }

    // obtener un servicio por su ID
    @GetMapping("/{id}")
    public org.springframework.http.ResponseEntity<ServicioDTO> obtenerPorId(@PathVariable Long id) {
        return org.springframework.http.ResponseEntity.ok(servicioService.obtainServicioPorId(id));
    }

    // actualizar un servicio existente
    @PutMapping("/{id}")
    public org.springframework.http.ResponseEntity<String> actualizar(@PathVariable Long id, @RequestBody ServicioDTO dto) {
        servicioService.actualizarServicio(id, dto);
        return org.springframework.http.ResponseEntity.ok("Servicio actualizado correctamente");
    }

    // 3. DELETE: http://localhost:8082/api/public/servicios/{id}
    @DeleteMapping("/{id}")
    public org.springframework.http.ResponseEntity<String> eliminar(@PathVariable Long id) {
        servicioService.eliminarServicio(id);
        return org.springframework.http.ResponseEntity.ok("Servicio eliminado correctamente");
    }
}