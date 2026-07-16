package com.example.royalauto.ms_comercial.controller;

import com.example.royalauto.ms_comercial.dto.CategoriaDTO;
import com.example.royalauto.ms_comercial.dto.MarcaDTO;
import com.example.royalauto.ms_comercial.dto.VehiculoDTO;
import com.example.royalauto.ms_comercial.service.CategoriaService;
import com.example.royalauto.ms_comercial.service.MarcaService;
import com.example.royalauto.ms_comercial.service.VehiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
@RequiredArgsConstructor
public class VehiculoController {

    private final VehiculoService vehiculoService;
    private final MarcaService marcaService;
    private final CategoriaService categoriaService;

    // E1HU3: Visualizar el catálogo de autos (nombre, modelo, año, precio, imágenes)
    @GetMapping
    public ResponseEntity<List<VehiculoDTO>> listarTodos() {
        return ResponseEntity.ok(vehiculoService.listarTodos());
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<VehiculoDTO>> listarDisponibles() {
        return ResponseEntity.ok(vehiculoService.listarDisponibles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehiculoDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(vehiculoService.obtenerPorId(id));
    }

    // E1HU1: Crear registros de nuevos vehículos (CRUD)
    @PostMapping
    public ResponseEntity<VehiculoDTO> crear(@RequestBody VehiculoDTO dto) {
        VehiculoDTO creado = vehiculoService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehiculoDTO> actualizar(@PathVariable Long id, @RequestBody VehiculoDTO dto) {
        return ResponseEntity.ok(vehiculoService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        vehiculoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // E1HU2: Filtrar vehículos por características (marca / categoría)
    @GetMapping("/filtrar/marca")
    public ResponseEntity<List<VehiculoDTO>> filtrarPorMarca(@RequestParam String nombre) {
        return ResponseEntity.ok(vehiculoService.filtrarPorMarca(nombre));
    }

    @GetMapping("/filtrar/categoria")
    public ResponseEntity<List<VehiculoDTO>> filtrarPorCategoria(@RequestParam String nombre) {
        return ResponseEntity.ok(vehiculoService.filtrarPorCategoria(nombre));
    }

    // ---------- Marca (sin controller propio, se administra desde aquí) ----------

    @GetMapping("/marcas")
    public ResponseEntity<List<MarcaDTO>> listarMarcas() {
        return ResponseEntity.ok(marcaService.listarTodas());
    }

    @PostMapping("/marcas")
    public ResponseEntity<MarcaDTO> crearMarca(@RequestBody MarcaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(marcaService.crear(dto));
    }

    @DeleteMapping("/marcas/{id}")
    public ResponseEntity<Void> eliminarMarca(@PathVariable Long id) {
        marcaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ---------- Categoria (sin controller propio, se administra desde aquí) ----------

    @GetMapping("/categorias")
    public ResponseEntity<List<CategoriaDTO>> listarCategorias() {
        return ResponseEntity.ok(categoriaService.listarTodas());
    }

    @PostMapping("/categorias")
    public ResponseEntity<CategoriaDTO> crearCategoria(@RequestBody CategoriaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.crear(dto));
    }

    @DeleteMapping("/categorias/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
