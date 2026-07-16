package com.example.royalauto.ms_comercial.service;

import com.example.royalauto.ms_comercial.dto.PromocionDTO;
import com.example.royalauto.ms_comercial.dto.VehiculoDTO;
import com.example.royalauto.ms_comercial.entity.Promocion;
import com.example.royalauto.ms_comercial.entity.Vehiculo;
import com.example.royalauto.ms_comercial.repository.PromocionRepository;
import com.example.royalauto.ms_comercial.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PromocionService {

    @Autowired
    private PromocionRepository promocionRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;
    
    private final String UPLOAD_DIR = "uploads/";

    // Modificado: Convierte las promociones activas a DTO
    public List<PromocionDTO> obtenerTarjetasPromocion() {
        return promocionRepository.findByActivoTrue()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Modificado: Recibe los campos de descuento y retorna el DTO empaquetado
    public PromocionDTO vincularPromocionAVehiculo(MultipartFile archivo, String imagenUrlExterna, Long vehiculoId, String tipoDescuento, Double valorDescuento) throws IOException {
        
        // 1. Buscamos el auto en la BD para obtener su precio original
        Vehiculo autoAsignado = vehiculoRepository.findById(vehiculoId)
                .orElseThrow(() -> new IllegalArgumentException("No existe un vehículo con el ID: " + vehiculoId));

        Promocion promocion = new Promocion();

        // Lógica de imágenes
        if (archivo != null && !archivo.isEmpty()) {
            String nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();
            Path rutaCompleta = Paths.get(UPLOAD_DIR + nombreArchivo);
            Files.createDirectories(rutaCompleta.getParent());
            Files.write(rutaCompleta, archivo.getBytes());
            promocion.setImagenUrl("http://localhost:8082/api/imagenes/" + nombreArchivo);
        } else if (imagenUrlExterna != null && !imagenUrlExterna.trim().isEmpty()) {
            promocion.setImagenUrl(imagenUrlExterna);
        } else {
            if (autoAsignado.getImagenes() != null && !autoAsignado.getImagenes().isEmpty()) {
                promocion.setImagenUrl(autoAsignado.getImagenes().get(0).getUrl());
            } else {
                promocion.setImagenUrl("https://via.placeholder.com/400x220?text=Auto+Sin+Foto");
            }
        }
        
        // Asignamos la relación con el vehículo e información del descuento
        promocion.setVehiculo(autoAsignado);
        promocion.setTipoDescuento(tipoDescuento != null ? tipoDescuento.toUpperCase() : "NINGUNO");
        promocion.setValorDescuento(valorDescuento != null ? valorDescuento : 0.0);
        
        // 2. NUEVA LÓGICA: CALCULAR EL PRECIO Y GUARDARLO EN LA ENTIDAD
        BigDecimal precioOriginal = autoAsignado.getPrecio();
        BigDecimal precioFinal = precioOriginal;

        if (precioOriginal != null && valorDescuento != null) {
            if ("PORCENTAJE".equals(promocion.getTipoDescuento())) {
                double porcentaje = valorDescuento / 100.0;
                BigDecimal rebaja = precioOriginal.multiply(BigDecimal.valueOf(porcentaje));
                precioFinal = precioOriginal.subtract(rebaja);
            } else if ("FIJO".equals(promocion.getTipoDescuento())) {
                precioFinal = precioOriginal.subtract(BigDecimal.valueOf(valorDescuento));
            }
        }
        
        // Guardamos el resultado en la propiedad que va a la BD
        promocion.setPrecioPromocion(precioFinal);
        promocion.setActivo(true);
        
        // Al persistir, se guardará el registro con el precio ya calculado fijo
        Promocion guardada = promocionRepository.save(promocion);
        return convertirADTO(guardada);
    }

    public void desactivarPromocion(Long id) {
        Promocion promocion = promocionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promoción no encontrada con ID: " + id));
        promocion.setActivo(false);
        promocionRepository.save(promocion);
    }

    public void eliminarPromocionDefinitivamente(Long id) {
        Promocion promocion = promocionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promoción no encontrada con ID: " + id));

        if (promocion.getImagenUrl() != null && promocion.getImagenUrl().contains("/api/imagenes/")) {
            try {
                String nombreArchivo = promocion.getImagenUrl().substring(promocion.getImagenUrl().lastIndexOf("/") + 1);
                Path rutaArchivo = Paths.get(UPLOAD_DIR + nombreArchivo);
                Files.deleteIfExists(rutaArchivo); 
            } catch (IOException e) {
                System.err.println("Error al borrar el archivo físico: " + e.getMessage());
            }
        }

        promocionRepository.delete(promocion);
    }

    // Método exclusivo para la previsualización en el Frontend
    public VehiculoDTO buscarInfoVehiculo(Long id) {
        Vehiculo v = vehiculoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado"));
        
        VehiculoDTO dto = new VehiculoDTO();
        dto.setId(v.getId());
        dto.setModelo(v.getModelo());
        dto.setAnio(v.getAnio());
        dto.setDescripcion(v.getDescripcion());
        dto.setPrecioOriginal(v.getPrecio());
        
        if (v.getMarca() != null) dto.setMarcaVehiculo(v.getMarca().getNombre());
        if (v.getCategoria() != null) dto.setCategoriaVehiculo(v.getCategoria().getNombre());
        
        if (v.getImagenes() != null && !v.getImagenes().isEmpty()) {
            dto.setImagenUrl(v.getImagenes().get(0).getUrl());
        } else {
            dto.setImagenUrl("https://via.placeholder.com/400x220?text=Sin+Foto");
        }
        return dto;
    }

    // Modificado: Convierte todas las promociones del panel administrativo a DTO
    public List<PromocionDTO> obtenerTodas() {
        return promocionRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Modificado: Ejecuta el toggle invirtiendo el estado y retorna DTO
    public PromocionDTO toggleActivo(Long id) {
        Promocion promocion = promocionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promoción no encontrada con ID: " + id));
        
        promocion.setActivo(!promocion.isActivo()); 
        Promocion actualizada = promocionRepository.save(promocion);
        return convertirADTO(actualizada);
    }

    // --- MÉTODO AUXILIAR PARA COMPONER LOS DATOS DE PROMOCIÓN, AUTO E IMÁGENES ---
    private PromocionDTO convertirADTO(Promocion promo) {
        PromocionDTO dto = new PromocionDTO();
        dto.setId(promo.getId());
        dto.setImagenUrlPromo(promo.getImagenUrl());
        dto.setActivo(promo.isActivo());
        dto.setTipoDescuento(promo.getTipoDescuento());
        dto.setValorDescuento(promo.getValorDescuento());
        
        // Obtenemos directamente el precio calculado desde la tabla promociones
        dto.setPrecioFinal(promo.getPrecioPromocion());
        
        if (promo.getVehiculo() != null) {
            Vehiculo v = promo.getVehiculo();
            dto.setVehiculoId(v.getId());
            dto.setModeloVehiculo(v.getModelo());
            dto.setPrecioOriginal(v.getPrecio());
            
            if (v.getMarca() != null) {
                dto.setMarcaVehiculo(v.getMarca().getNombre());
            }
            if (v.getCategoria() != null) {
                dto.setCategoriaVehiculo(v.getCategoria().getNombre());
            }
            
            if (v.getImagenes() != null && !v.getImagenes().isEmpty()) {
                dto.setImagenVehiculoUrl(v.getImagenes().get(0).getUrl());
            } else {
                dto.setImagenVehiculoUrl("https://via.placeholder.com/400x220?text=Sin+Foto+De+Vehiculo");
            }
        }
        
        return dto;
    }

    // Trae una lista limpia de todos los autos para el menú desplegable del Admin
    public List<VehiculoDTO> obtenerVehiculosParaSeleccion() {
        return vehiculoRepository.findAll()
                .stream()
                .map(v -> {
                    VehiculoDTO dto = new VehiculoDTO();
                    dto.setId(v.getId());
                    dto.setModelo(v.getModelo());
                    dto.setAnio(v.getAnio());
                    dto.setPrecioOriginal(v.getPrecio());
                    dto.setDescripcion(v.getDescripcion());
                    
                    if (v.getMarca() != null) dto.setMarcaVehiculo(v.getMarca().getNombre());
                    if (v.getCategoria() != null) dto.setCategoriaVehiculo(v.getCategoria().getNombre());
                    
                    if (v.getImagenes() != null && !v.getImagenes().isEmpty()) {
                        dto.setImagenUrl(v.getImagenes().get(0).getUrl());
                    } else {
                        dto.setImagenUrl("https://via.placeholder.com/400x220?text=Sin+Foto");
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }
    
    // --- NUEVO MÉTODO PARA ACTUALIZAR ---
    public PromocionDTO actualizarPromocion(Long id, String tipoDescuento, Double valorDescuento) {
        Promocion promocion = promocionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promoción no encontrada con ID: " + id));

        promocion.setTipoDescuento(tipoDescuento != null ? tipoDescuento.toUpperCase() : "NINGUNO");
        promocion.setValorDescuento(valorDescuento != null ? valorDescuento : 0.0);

        // Recalcular el precio final basándonos en el precio original del vehículo
        Vehiculo v = promocion.getVehiculo();
        if (v != null && v.getPrecio() != null) {
            BigDecimal precioOriginal = v.getPrecio();
            BigDecimal precioFinal = precioOriginal;

            if ("PORCENTAJE".equals(promocion.getTipoDescuento())) {
                double porcentaje = promocion.getValorDescuento() / 100.0;
                BigDecimal rebaja = precioOriginal.multiply(BigDecimal.valueOf(porcentaje));
                precioFinal = precioOriginal.subtract(rebaja);
            } else if ("FIJO".equals(promocion.getTipoDescuento())) {
                precioFinal = precioOriginal.subtract(BigDecimal.valueOf(promocion.getValorDescuento()));
            }
            promocion.setPrecioPromocion(precioFinal);
        }

        Promocion actualizada = promocionRepository.save(promocion);
        return convertirADTO(actualizada);
    }
}