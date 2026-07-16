package com.example.royalauto.ms_comercial.service;

import lombok.RequiredArgsConstructor;
import com.example.royalauto.ms_comercial.dto.CitaRequestDTO;
import com.example.royalauto.ms_comercial.dto.ServicioDTO;
import com.example.royalauto.ms_comercial.entity.CitaServicio;
import com.example.royalauto.ms_comercial.entity.Servicio;
import com.example.royalauto.ms_comercial.repository.CitaServicioRepository;
import com.example.royalauto.ms_comercial.repository.ServicioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServicioAutomotrizService {

    private final ServicioRepository servicioRepository;
    private final CitaServicioRepository citaRepository;

    public List<ServicioDTO> obtenerCatalogoServicios() {
        return servicioRepository.findAll().stream().map(servicio -> {
            ServicioDTO dto = new ServicioDTO();
            dto.setId(servicio.getId());
            dto.setNombre(servicio.getNombre());
            dto.setDescripcion(servicio.getDescripcion());
            dto.setPrecioBase(servicio.getPrecioBase());
            dto.setImagenUrl(servicio.getImagenUrl());
            return dto;
        }).collect(Collectors.toList());
    }

    public void guardarServicio(ServicioDTO dto) {
        Servicio servicio = new Servicio();
        servicio.setNombre(dto.getNombre());
        servicio.setDescripcion(dto.getDescripcion());
        servicio.setPrecioBase(dto.getPrecioBase());
        servicio.setImagenUrl(dto.getImagenUrl());
        
        servicioRepository.save(servicio);
    }

    // Obtener un servicio por su ID
    public ServicioDTO obtainServicioPorId(Long id) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + id));
        
        ServicioDTO dto = new ServicioDTO();
        dto.setId(servicio.getId());
        dto.setNombre(servicio.getNombre());
        dto.setDescripcion(servicio.getDescripcion());
        dto.setPrecioBase(servicio.getPrecioBase());
        dto.setImagenUrl(servicio.getImagenUrl());
        return dto;
    }

    // Actualizar un servicio existente
    public void actualizarServicio(Long id, ServicioDTO dto) {
        Servicio servicioExistente = servicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + id));

        servicioExistente.setNombre(dto.getNombre());
        servicioExistente.setDescripcion(dto.getDescripcion());
        servicioExistente.setPrecioBase(dto.getPrecioBase());
        servicioExistente.setImagenUrl(dto.getImagenUrl());
        
        servicioRepository.save(servicioExistente);
    }

    // Eliminar un servicio por su ID
    public void eliminarServicio(Long id) {
        Servicio servicioExistente = servicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + id));

        servicioRepository.delete(servicioExistente);
    }

    // 1. Guardar la cita del formulario público
    public void agendarCita(CitaRequestDTO dto) {
        // Validamos usando el id tipo Long obtenido del DTO
        Servicio servicio = servicioRepository.findById(dto.getServicioId())
                .orElseThrow(() -> new RuntimeException("El servicio seleccionado no es válido o no existe."));

        CitaServicio cita = new CitaServicio();
        cita.setNombreCliente(dto.getNombreCliente());
        cita.setCorreo(dto.getCorreo());
        cita.setTelefono(dto.getTelefono());
        cita.setMarca(dto.getMarca());
        cita.setModelo(dto.getModelo());
        cita.setAnio(dto.getAnio());
        cita.setFechaCita(dto.getFechaCita());
        cita.setMensaje(dto.getMensaje());
        cita.setServicio(servicio);

        citaRepository.save(cita);
    }

    // 2. Obtener todas las citas registradas (Para el panel del Administrador)
    public List<CitaRequestDTO> obtenerTodasLasCitas() {
        return citaRepository.findAll().stream().map(cita -> {
            CitaRequestDTO dto = new CitaRequestDTO();
            dto.setId(cita.getId());
            dto.setNombreCliente(cita.getNombreCliente());
            dto.setCorreo(cita.getCorreo());
            dto.setTelefono(cita.getTelefono());
            dto.setMarca(cita.getMarca());
            dto.setModelo(cita.getModelo());
            dto.setAnio(cita.getAnio());
            dto.setFechaCita(cita.getFechaCita());
            dto.setMensaje(cita.getMensaje());
            dto.setServicioId(cita.getServicio().getId()); // Retorna el Long de la relación
            dto.setNombreServicio(cita.getServicio().getNombre()); 
            dto.setEstado(cita.getEstado());
            dto.setFechaRegistro(cita.getFechaRegistro());
            return dto;
        }).collect(Collectors.toList());
    }
}