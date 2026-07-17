// package com.example.gateway.gateway;

// import java.util.List;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.HttpMethod;
// import org.springframework.security.config.Customizer;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
// import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
// import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.CorsConfigurationSource;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// @Configuration
// public class SecurityConfig {

//     @Bean
//     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//         http
//             .cors(Customizer.withDefaults())
//             .csrf(AbstractHttpConfigurer::disable)
//             .authorizeHttpRequests(auth -> auth
//                 // Rutas públicas de autenticación (login, oauth2)
//                 .requestMatchers("/oauth2/**", "/login", "/.well-known/**").permitAll()

//                 // /register ya NO es público: solo Admin da de alta nuevos usuarios
//                 .requestMatchers("/register").authenticated()

//                 // Cotizaciones: crear es público, consultar/actualizar es solo Admin
//                 .requestMatchers(HttpMethod.POST, "/api/cotizaciones").permitAll()
//                 .requestMatchers(HttpMethod.GET, "/api/cotizaciones/**").hasRole("ADMIN")
//                 .requestMatchers(HttpMethod.PUT, "/api/cotizaciones/**").hasRole("ADMIN")

//                 // Catálogo (vehículos, marcas, categorías): lectura pública, escritura solo Admin
//                 .requestMatchers(HttpMethod.GET, "/api/vehiculos/**").permitAll()
//                 .requestMatchers(HttpMethod.POST, "/api/vehiculos/**").hasRole("ADMIN")
//                 .requestMatchers(HttpMethod.PUT, "/api/vehiculos/**").hasRole("ADMIN")
//                 .requestMatchers(HttpMethod.DELETE, "/api/vehiculos/**").hasRole("ADMIN")

//                 // Otras entidades genéricas (si las usas)
//                 .requestMatchers(HttpMethod.GET, "/api/entity-a/**", "/api/entity-b/**").hasAnyRole("USER", "ADMIN")
//                 .requestMatchers(HttpMethod.POST, "/api/entity-a/**", "/api/entity-b/**").hasRole("ADMIN")
//                 .requestMatchers(HttpMethod.PUT, "/api/entity-a/**", "/api/entity-b/**").hasRole("ADMIN")
//                 .requestMatchers(HttpMethod.DELETE, "/api/entity-a/**", "/api/entity-b/**").hasRole("ADMIN")

//                 // Todo lo demás requiere sesión válida
//                 .anyRequest().authenticated()
//             )
//             .oauth2ResourceServer(oauth2 -> oauth2
//                 .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
//             );

//         return http.build();
//     }

//     @Bean
//     public CorsConfigurationSource corsConfigurationSource() {
//         CorsConfiguration config = new CorsConfiguration();
//         config.setAllowedOrigins(List.of("http://localhost:5173"));
//         config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//         config.setAllowedHeaders(List.of("*"));
//         config.setAllowCredentials(true);

//         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//         source.registerCorsConfiguration("/**", config);
//         return source;
//     }

//     private JwtAuthenticationConverter jwtAuthenticationConverter() {
//         JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
//         grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
//         grantedAuthoritiesConverter.setAuthorityPrefix("");

//         JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
//         jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
//         return jwtAuthenticationConverter;
//     }
// }


package com.example.gateway.gateway;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    private static final String[] STAFF_ROLES = {"ADMIN", "SERVICIO", "VENTAS", "MARKETING"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                // Rutas públicas de autenticación (login, oauth2)
                .requestMatchers("/oauth2/**", "/login", "/.well-known/**").permitAll()

                // /register: cualquier miembro del staff puede dar de alta a otro
                .requestMatchers("/register").hasAnyRole(STAFF_ROLES)

                // Cotizaciones: crear es público, consultar/actualizar es staff
                .requestMatchers(HttpMethod.POST, "/api/cotizaciones").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/cotizaciones/**").hasAnyRole(STAFF_ROLES)
                .requestMatchers(HttpMethod.PUT, "/api/cotizaciones/**").hasAnyRole(STAFF_ROLES)

                // Catálogo: lectura pública, escritura para cualquier staff
                .requestMatchers(HttpMethod.GET, "/api/vehiculos/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/vehiculos/**").hasAnyRole(STAFF_ROLES)
                .requestMatchers(HttpMethod.PUT, "/api/vehiculos/**").hasAnyRole(STAFF_ROLES)
                .requestMatchers(HttpMethod.DELETE, "/api/vehiculos/**").hasAnyRole(STAFF_ROLES)

                // Otras entidades genéricas (si las usas)
                .requestMatchers(HttpMethod.GET, "/api/entity-a/**", "/api/entity-b/**").hasAnyRole(STAFF_ROLES)
                .requestMatchers(HttpMethod.POST, "/api/entity-a/**", "/api/entity-b/**").hasAnyRole(STAFF_ROLES)
                .requestMatchers(HttpMethod.PUT, "/api/entity-a/**", "/api/entity-b/**").hasAnyRole(STAFF_ROLES)
                .requestMatchers(HttpMethod.DELETE, "/api/entity-a/**", "/api/entity-b/**").hasAnyRole(STAFF_ROLES)

                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}