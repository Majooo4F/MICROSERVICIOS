package com.example.idgs15.authorization_server.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
}