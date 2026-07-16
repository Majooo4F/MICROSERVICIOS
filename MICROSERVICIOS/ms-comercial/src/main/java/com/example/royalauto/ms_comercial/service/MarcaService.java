package com.example.royalauto.ms_comercial.service;

import com.example.royalauto.ms_comercial.dto.MarcaDTO;
import com.example.royalauto.ms_comercial.entity.Marca;
import com.example.royalauto.ms_comercial.repository.MarcaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MarcaService {

    private final MarcaRepository marcaRepository;

    public List<MarcaDTO> listarTodas() {
        return marcaRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public MarcaDTO crear(MarcaDTO dto) {
        Marca marca = new Marca();
        marca.setNombre(dto.getNombre());
        marca.setLogo(dto.getLogo());
        marca.setDescripcion(dto.getDescripcion());
        return toDTO(marcaRepository.save(marca));
    }

    public void eliminar(Long id) {
        marcaRepository.deleteById(id);
    }

    private MarcaDTO toDTO(Marca marca) {
        MarcaDTO dto = new MarcaDTO();
        dto.setId(marca.getId());
        dto.setNombre(marca.getNombre());
        dto.setLogo(marca.getLogo());
        dto.setDescripcion(marca.getDescripcion());
        return dto;
    }
}