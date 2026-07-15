package com.example.idgs15.authorization_server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.idgs15.authorization_server.entity.AppUser;
import com.example.idgs15.authorization_server.repository.AppUserRepository;

@SpringBootApplication
public class AuthorizationServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthorizationServerApplication.class, args);
	}

	@Bean
	public CommandLineRunner seedAdmin(AppUserRepository repository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (!repository.existsByUsername("admin")) {
				AppUser admin = new AppUser();
				admin.setUsername("admin");
				admin.setPassword(passwordEncoder.encode("admin123"));
				admin.setRole("ADMIN");
				repository.save(admin);
			}
		};
	}
}