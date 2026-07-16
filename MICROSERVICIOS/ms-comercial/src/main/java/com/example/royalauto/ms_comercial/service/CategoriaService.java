package com.example.royalauto.ms_comercial.service;


import com.example.royalauto.ms_comercial.dto.CategoriaDTO;
import com.example.royalauto.ms_comercial.entity.Categoria;
import com.example.royalauto.ms_comercial.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public List<CategoriaDTO> listarTodas() {
        return categoriaRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public CategoriaDTO crear(CategoriaDTO dto) {
        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombre());
        return toDTO(categoriaRepository.save(categoria));
    }

    public void eliminar(Long id) {
        categoriaRepository.deleteById(id);
    }

    private CategoriaDTO toDTO(Categoria categoria) {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setId(categoria.getId());
        dto.setNombre(categoria.getNombre());
        return dto;
    }
}

