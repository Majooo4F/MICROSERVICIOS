package com.example.idgs15.authorization_server.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String nombre;
    private String username;
    private String password;
    private String correo;
    private String telefono;
    private String role; // "USER" o "ADMIN", lo decide el Admin al crear
}