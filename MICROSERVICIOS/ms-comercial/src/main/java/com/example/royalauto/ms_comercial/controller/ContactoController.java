package com.example.royalauto.ms_comercial.controller;


import com.example.royalauto.ms_comercial.dto.ContactoDTO;
import com.example.royalauto.ms_comercial.service.ContactoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comercial/contacto")
@RequiredArgsConstructor
public class ContactoController {
    
    private final ContactoService contactoService;
    
    @GetMapping
    public ResponseEntity<ContactoDTO> getContacto() {
        ContactoDTO contacto = contactoService.getContacto();
        if (contacto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(contacto);
    }
    
    @PutMapping
    public ResponseEntity<ContactoDTO> updateContacto(@RequestBody ContactoDTO contactoDTO) {
        ContactoDTO updated = contactoService.updateContacto(contactoDTO);
        return ResponseEntity.ok(updated);
    }
}