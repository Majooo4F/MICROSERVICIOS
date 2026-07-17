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
            // APAGAMOS EL CORS DE SPRING SECURITY para que no interfiera con el filtro maestro de arriba
            .cors(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/oauth2/**", "/login", "/.well-known/**").permitAll()
                .requestMatchers("/register").hasRole("ADMIN")

                // ========================================================
                // RUTAS DE TU COMPAÑERO (SERVICIOS Y CITAS)
                // ========================================================
                // Permitir acceso a los endpoints de servicios
                .requestMatchers(HttpMethod.GET, "/api/public/servicios/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/public/citas-servicios").permitAll()
                // CRUD de administración de servicios es solo para ADMIN
                .requestMatchers(HttpMethod.POST, "/api/public/servicios").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/public/servicios/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/public/servicios/**").hasRole("ADMIN")
                // Solo el ADMIN puede ver el listado de agendas de los clientes
                .requestMatchers(HttpMethod.GET, "/api/public/citas-servicios").hasRole("ADMIN")

                // ========================================================
                // TUS RUTAS (COTIZACIONES, VEHÍCULOS, NOTICIAS, PROMOCIONES)
                // ========================================================
                // Cotizaciones: crear es público, consultar/actualizar es solo Admin
                .requestMatchers(HttpMethod.POST, "/api/cotizaciones").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/cotizaciones/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/cotizaciones/**").hasRole("ADMIN")

                // Catálogo de vehículos
                .requestMatchers(HttpMethod.GET, "/api/vehiculos/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/vehiculos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/vehiculos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/vehiculos/**").hasRole("ADMIN")

                // Cotizaciones de vehículos: solicitar es público, consultar/actualizar es solo Admin
                .requestMatchers(HttpMethod.POST, "/api/public/cotizaciones-vehiculos").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/public/cotizaciones-vehiculos").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/public/cotizaciones-vehiculos/**").hasRole("ADMIN")

                // NUESTRAS RUTAS: Públicas para leer, protegidas para escribir
                .requestMatchers(HttpMethod.GET, "/api/noticias/**", "/api/promociones/**", "/api/imagenes/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/noticias/**", "/api/promociones/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/noticias/**", "/api/promociones/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/noticias/**", "/api/promociones/**").hasRole("ADMIN")

                // Otras entidades genéricas
                .requestMatchers(HttpMethod.GET, "/api/entity-a/**", "/api/entity-b/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/entity-a/**", "/api/entity-b/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/entity-a/**", "/api/entity-b/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/entity-a/**", "/api/entity-b/**").hasRole("ADMIN")

                // Mis rutas mark
                //Público cualquiera lo puede ver
                .requestMatchers(HttpMethod.GET, "/api/comercial/contacto").permitAll()
                // Solo Admin puede actualizar los datos de contacto
                .requestMatchers(HttpMethod.PUT, "/api/comercial/contacto").hasRole("ADMIN")
                

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