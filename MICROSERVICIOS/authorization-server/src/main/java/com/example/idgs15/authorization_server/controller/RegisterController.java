package com.example.idgs15.authorization_server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.idgs15.authorization_server.dto.RegisterRequest;
import com.example.idgs15.authorization_server.entity.AppUser;
import com.example.idgs15.authorization_server.repository.AppUserRepository;

@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private AppUserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        if (repository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("El usuario ya existe");
        }

        AppUser newUser = new AppUser();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole("USER"); // por seguridad, el registro público siempre da rol USER, nunca ADMIN

        repository.save(newUser);

        return ResponseEntity.ok("Usuario registrado correctamente");
    }
}