package com.example.royalauto.ms_comercial.controller;

import com.example.royalauto.ms_comercial.dto.PromocionDTO;
import com.example.royalauto.ms_comercial.dto.VehiculoDTO;
import com.example.royalauto.ms_comercial.service.PromocionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/promociones")
@CrossOrigin(origins = "http://localhost:5173") // Permite la entrada directa desde tu frontend en React
public class PromocionController {

    @Autowired
    private PromocionService promocionService;

    // --- Trae TODAS (Para el panel de React) ---
    @GetMapping("/todas")
    public ResponseEntity<List<PromocionDTO>> getTodas() { 
        return ResponseEntity.ok(promocionService.obtenerTodas());
    }

    // --- Trae SOLO LAS ACTIVAS (Para el Index público) ---
    @GetMapping("/tarjetas")
    public ResponseEntity<List<PromocionDTO>> getTarjetas() { 
        return ResponseEntity.ok(promocionService.obtenerTarjetasPromocion());
    }

    // --- VINCULAR PROMOCIÓN (Actualizado con los campos de descuento) ---
    @PostMapping(value = "/vincular", consumes = "multipart/form-data")
    public ResponseEntity<PromocionDTO> crearPromocion(
            @RequestParam(value = "archivo", required = false) MultipartFile archivo,
            @RequestParam(value = "imagenUrl", required = false) String imagenUrlExterna,
            @RequestParam("vehiculoId") Long vehiculoId,
            @RequestParam(value = "tipoDescuento", required = false) String tipoDescuento,
            @RequestParam(value = "valorDescuento", required = false) Double valorDescuento) {
        try {
            return ResponseEntity
                    .ok(promocionService.vincularPromocionAVehiculo(archivo, imagenUrlExterna, vehiculoId, tipoDescuento, valorDescuento));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // --- Endpoint para el Switch de Activar/Desactivar ---
    @PutMapping("/toggle-activo/{id}")
    public ResponseEntity<PromocionDTO> toggleActivo(@PathVariable Long id) { // <-- Cambiado a PromocionDTO
        try {
            return ResponseEntity.ok(promocionService.toggleActivo(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- NUEVO: Endpoint para Vista Previa del Auto ---
    @GetMapping("/vehiculo/{id}")
    public ResponseEntity<VehiculoDTO> getInfoVehiculoParaPreview(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(promocionService.buscarInfoVehiculo(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/desactivar/{id}")
    public ResponseEntity<Void> desactivarPromocion(@PathVariable Long id) {
        try {
            promocionService.desactivarPromocion(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- Endpoint para borrar físicamente (DELETE) ---
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarBannerDefinitivamente(@PathVariable Long id) {
        try {
            promocionService.eliminarPromocionDefinitivamente(id);
            return ResponseEntity.noContent().build(); 
        } catch (Exception e) {
            return ResponseEntity.notFound().build(); 
        }
    }

    // --- NUEVO: Obtener catálogo completo para el menú desplegable ---
    @GetMapping("/vehiculos-listado")
    public ResponseEntity<List<VehiculoDTO>> getVehiculosListado() {
        return ResponseEntity.ok(promocionService.obtenerVehiculosParaSeleccion());
    }

    // --- NUEVO: Endpoint para Editar Promoción ---
    @PutMapping(value = "/actualizar/{id}", consumes = "multipart/form-data")
    public ResponseEntity<PromocionDTO> actualizarPromocion(
            @PathVariable Long id,
            @RequestParam(value = "tipoDescuento", required = false) String tipoDescuento,
            @RequestParam(value = "valorDescuento", required = false) Double valorDescuento) {
        try {
            return ResponseEntity.ok(promocionService.actualizarPromocion(id, tipoDescuento, valorDescuento));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}