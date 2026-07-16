package com.example.idgs15.authorization_server;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.idgs15.authorization_server.entity.Users;
import com.example.idgs15.authorization_server.repository.UserRepository;

@SpringBootApplication
public class AuthorizationServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthorizationServerApplication.class, args);
	}

	@Bean
	public CommandLineRunner seedAdmin(UserRepository repository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (!repository.existsByUsername("admin")) {
				Users admin = new Users();
				admin.setNombre("Administrador");
				admin.setTelefono("0000000000");
				admin.setCorreo("admin@example.com");
				admin.setUsername("admin");
				admin.setPassword(passwordEncoder.encode("admin123"));
				admin.setRole("ADMIN");
				repository.save(admin);
			}
		};
	}
}