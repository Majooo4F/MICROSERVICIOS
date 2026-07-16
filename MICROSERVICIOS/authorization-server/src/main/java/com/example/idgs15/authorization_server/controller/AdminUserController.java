package com.example.idgs15.authorization_server.controller;

import com.example.idgs15.authorization_server.entity.Users;
import com.example.idgs15.authorization_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserRepository userRepository;

    @PreAuthorize("hasAuthority('SCOPE_admin') or hasRole('ADMIN')")
    @PutMapping("/{id}/role")
    public ResponseEntity<?> changeRole(@PathVariable Long id, @RequestParam String role) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!role.equals("USER") && !role.equals("ADMIN")) {
            return ResponseEntity.badRequest().body("Rol inválido");
        }

        user.setRole(role);
        userRepository.save(user);
        return ResponseEntity.ok("Rol actualizado a " + role);
    }
}