package com.example.gateway.gateway;

import java.util.List;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class SecurityConfig {

    private static final String[] STAFF_ROLES = {"ADMIN", "SERVICIO", "VENTAS", "MARKETING"};

    @Bean
    public FilterRegistrationBean<CorsFilter> customCorsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Apagamos el CORS de Spring Security: el filtro maestro de arriba ya lo maneja
            .cors(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/oauth2/**", "/login", "/.well-known/**").permitAll()

                // /register: cualquier miembro del staff puede dar de alta a otro
                .requestMatchers("/register").hasAnyRole(STAFF_ROLES)

                // ========================================================
                // RUTAS DE SERVICIOS Y CITAS
                // ========================================================
                .requestMatchers(HttpMethod.GET, "/api/public/servicios/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/public/citas-servicios").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/public/servicios").hasAnyRole(STAFF_ROLES)
                .requestMatchers(HttpMethod.PUT, "/api/public/servicios/**").hasAnyRole(STAFF_ROLES)
                .requestMatchers(HttpMethod.DELETE, "/api/public/servicios/**").hasAnyRole(STAFF_ROLES)
                .requestMatchers(HttpMethod.GET, "/api/public/citas-servicios").hasAnyRole(STAFF_ROLES)

                // ========================================================
                // COTIZACIONES Y VEHÍCULOS
                // ========================================================
                .requestMatchers(HttpMethod.POST, "/api/cotizaciones").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/cotizaciones/**").hasAnyRole(STAFF_ROLES)
                .requestMatchers(HttpMethod.PUT, "/api/cotizaciones/**").hasAnyRole(STAFF_ROLES)

                .requestMatchers(HttpMethod.GET, "/api/vehiculos/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/vehiculos/**").hasAnyRole(STAFF_ROLES)
                .requestMatchers(HttpMethod.PUT, "/api/vehiculos/**").hasAnyRole(STAFF_ROLES)
                .requestMatchers(HttpMethod.DELETE, "/api/vehiculos/**").hasAnyRole(STAFF_ROLES)

                // Cotizaciones de vehículos (variante /api/public/...)
                .requestMatchers(HttpMethod.POST, "/api/public/cotizaciones-vehiculos").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/public/cotizaciones-vehiculos").hasAnyRole(STAFF_ROLES)
                .requestMatchers(HttpMethod.PUT, "/api/public/cotizaciones-vehiculos/**").hasAnyRole(STAFF_ROLES)

                // ========================================================
                // NOTICIAS, PROMOCIONES, IMÁGENES
                // ========================================================
                .requestMatchers(HttpMethod.GET, "/api/noticias/**", "/api/promociones/**", "/api/imagenes/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/noticias/**", "/api/promociones/**").hasAnyRole(STAFF_ROLES)
                .requestMatchers(HttpMethod.PUT, "/api/noticias/**", "/api/promociones/**").hasAnyRole(STAFF_ROLES)
                .requestMatchers(HttpMethod.DELETE, "/api/noticias/**", "/api/promociones/**").hasAnyRole(STAFF_ROLES)

                // ========================================================
                // CONTACTO
                // ========================================================
                .requestMatchers(HttpMethod.GET, "/api/comercial/contacto").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/comercial/contacto").hasAnyRole(STAFF_ROLES)

                // ========================================================
                // ENTIDADES GENÉRICAS
                // ========================================================
                .requestMatchers(HttpMethod.GET, "/api/entity-a/**", "/api/entity-b/**").hasAnyRole(STAFF_ROLES)
                .requestMatchers(HttpMethod.POST, "/api/entity-a/**", "/api/entity-b/**").hasAnyRole(STAFF_ROLES)
                .requestMatchers(HttpMethod.PUT, "/api/entity-a/**", "/api/entity-b/**").hasAnyRole(STAFF_ROLES)
                .requestMatchers(HttpMethod.DELETE, "/api/entity-a/**", "/api/entity-b/**").hasAnyRole(STAFF_ROLES)

                // Todo lo demás requiere sesión válida
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            );

        return http.build();
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