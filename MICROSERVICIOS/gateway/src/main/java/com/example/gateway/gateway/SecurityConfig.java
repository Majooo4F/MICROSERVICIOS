package com.example.gateway.gateway;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                // Lectura: ambos roles pueden
                .requestMatchers(HttpMethod.GET, "/api/entity-a/**", "/api/entity-b/**").hasAnyRole("USER", "ADMIN")
                // Escritura: solo ADMIN
                .requestMatchers(HttpMethod.POST, "/api/entity-a/**", "/api/entity-b/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/entity-a/**", "/api/entity-b/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/entity-a/**", "/api/entity-b/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            );

        return http.build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles"); // el nombre del claim que agregamos
        grantedAuthoritiesConverter.setAuthorityPrefix(""); // el claim ya trae "ROLE_", no dupliques el prefijo

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}