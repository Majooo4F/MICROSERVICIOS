package com.example.royalauto.ms_comercial.service;


import com.example.royalauto.ms_comercial.dto.VehiculoDTO;
import com.example.royalauto.ms_comercial.entity.Categoria;
import com.example.royalauto.ms_comercial.entity.ImagenVehiculo;
import com.example.royalauto.ms_comercial.entity.Marca;
import com.example.royalauto.ms_comercial.entity.Vehiculo;
import com.example.royalauto.ms_comercial.repository.CategoriaRepository;
import com.example.royalauto.ms_comercial.repository.MarcaRepository;
import com.example.royalauto.ms_comercial.repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final MarcaRepository marcaRepository;
    private final CategoriaRepository categoriaRepository;

    // E1HU3: Visualizar el catálogo de autos
    public List<VehiculoDTO> listarTodos() {
        return vehiculoRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public VehiculoDTO obtenerPorId(Long id) {
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con id: " + id));
        return toDTO(vehiculo);
    }

    // Consulta en lote (ej. para que ms-cotizaciones resuelva varios vehículos con una sola llamada)
    public List<VehiculoDTO> obtenerPorIds(List<Long> ids) {
        return vehiculoRepository.findAllById(ids).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // E1HU1: Crear registros de nuevos vehículos
    public VehiculoDTO crear(VehiculoDTO dto) {
        Vehiculo vehiculo = toEntity(dto, new Vehiculo());
        return toDTO(vehiculoRepository.save(vehiculo));
    }

    public VehiculoDTO actualizar(Long id, VehiculoDTO dto) {
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con id: " + id));
        vehiculo = toEntity(dto, vehiculo);
        return toDTO(vehiculoRepository.save(vehiculo));
    }

    public void eliminar(Long id) {
        if (!vehiculoRepository.existsById(id)) {
            throw new RuntimeException("Vehículo no encontrado con id: " + id);
        }
        vehiculoRepository.deleteById(id);
    }

    // E1HU2: Filtrar vehículos por marca
    public List<VehiculoDTO> filtrarPorMarca(String nombreMarca) {
        return vehiculoRepository.findByMarca_NombreIgnoreCase(nombreMarca).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<VehiculoDTO> filtrarPorCategoria(String nombreCategoria) {
        return vehiculoRepository.findByCategoria_NombreIgnoreCase(nombreCategoria).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<VehiculoDTO> listarDisponibles() {
        return vehiculoRepository.findByDisponibleTrue().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ---------- Conversión manual (sin mapper) FUSIONADA ----------

    private VehiculoDTO toDTO(Vehiculo vehiculo) {
        VehiculoDTO dto = new VehiculoDTO();
        dto.setId(vehiculo.getId());
        dto.setModelo(vehiculo.getModelo());
        dto.setAnio(vehiculo.getAnio());
        dto.setPrecio(vehiculo.getPrecio()); // Campo del pull
        dto.setDescripcion(vehiculo.getDescripcion());
        dto.setDisponible(vehiculo.getDisponible());
        dto.setMarcaId(vehiculo.getMarca().getId());
        dto.setMarcaNombre(vehiculo.getMarca().getNombre()); // Campo del pull
        dto.setCategoriaId(vehiculo.getCategoria().getId());
        dto.setCategoriaNombre(vehiculo.getCategoria().getNombre()); // Campo del pull
        
        // Lógica de imágenes del pull (lista de Strings)
        dto.setImagenes(vehiculo.getImagenes().stream()
                .map(ImagenVehiculo::getUrl)
                .collect(Collectors.toList()));

        // --- NUESTROS CAMPOS DE COMPATIBILIDAD (Para el Marketing Dashboard) ---
        // Llenamos precioOriginal con el precio base del auto
        dto.setPrecioOriginal(vehiculo.getPrecio()); 
        
        // Mapeamos los nombres de marca y categoría a nuestras variables de DTO
        if (vehiculo.getMarca() != null) {
            dto.setMarcaVehiculo(vehiculo.getMarca().getNombre());
        }
        if (vehiculo.getCategoria() != null) {
            dto.setCategoriaVehiculo(vehiculo.getCategoria().getNombre());
        }
        
        // Foto principal para la vista previa del Admin
        if (vehiculo.getImagenes() != null && !vehiculo.getImagenes().isEmpty()) {
            dto.setImagenUrl(vehiculo.getImagenes().get(0).getUrl());
        } else {
            dto.setImagenUrl("https://via.placeholder.com/400x220?text=Sin+Foto");
        }
        // ---

        return dto;
    }

    private Vehiculo toEntity(VehiculoDTO dto, Vehiculo vehiculo) {
        vehiculo.setModelo(dto.getModelo());
        vehiculo.setAnio(dto.getAnio());
        vehiculo.setPrecio(dto.getPrecio());
        vehiculo.setDescripcion(dto.getDescripcion());
        vehiculo.setDisponible(dto.getDisponible() != null ? dto.getDisponible() : true);

        Marca marca = marcaRepository.findById(dto.getMarcaId())
                .orElseThrow(() -> new RuntimeException("Marca no encontrada con id: " + dto.getMarcaId()));
        vehiculo.setMarca(marca);

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + dto.getCategoriaId()));
        vehiculo.setCategoria(categoria);

        if (dto.getImagenes() != null) {
            vehiculo.getImagenes().clear();
            dto.getImagenes().forEach(url -> {
                ImagenVehiculo img = new ImagenVehiculo();
                img.setUrl(url);
                img.setVehiculo(vehiculo);
                vehiculo.getImagenes().add(img);
            });
        }

        return vehiculo;
    }
}
