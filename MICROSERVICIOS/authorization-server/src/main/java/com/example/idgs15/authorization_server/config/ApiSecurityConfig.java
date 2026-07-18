// package com.example.idgs15.authorization_server.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.core.annotation.Order;
// import org.springframework.http.HttpMethod;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
// import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
// import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
// import org.springframework.security.web.SecurityFilterChain;

// @Configuration
// public class ApiSecurityConfig {

//     // Filtro SOLO para rutas de API que llegan con Bearer token vía gateway
//     // (register, cotizaciones, etc.) — separado del flujo OAuth y del login por formulario.
//     @Bean
//     @Order(2)
//     public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
//         http
//                 .securityMatcher("/register", "/api/**")
//                 .csrf(AbstractHttpConfigurer::disable)
//                 .authorizeHttpRequests(auth -> auth
//                         .requestMatchers("/register").hasRole("ADMIN")
//                         .requestMatchers(HttpMethod.POST, "/api/cotizaciones").permitAll()
//                         .requestMatchers(HttpMethod.GET, "/api/cotizaciones/**").hasRole("ADMIN")
//                         .requestMatchers(HttpMethod.PUT, "/api/cotizaciones/**").hasRole("ADMIN")
//                         .anyRequest().authenticated())
//                 .oauth2ResourceServer(oauth2 -> oauth2
//                         .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

//         return http.build();
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
// package com.example.idgs15.authorization_server.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.core.annotation.Order;
// import org.springframework.http.HttpMethod;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
// import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
// import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
// import org.springframework.security.web.SecurityFilterChain;

// @Configuration
// public class ApiSecurityConfig {

//     // Filtro SOLO para rutas de API que llegan con Bearer token vía gateway
//     // (register, cotizaciones, etc.) — separado del flujo OAuth y del login por formulario.
//     @Bean
//     @Order(2)
//     public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
//         http
//                 .securityMatcher("/register", "/api/**")
//                 .csrf(AbstractHttpConfigurer::disable)
//                 .authorizeHttpRequests(auth -> auth
//                         .requestMatchers("/register").hasRole("ADMIN")
//                         .requestMatchers(HttpMethod.POST, "/api/cotizaciones").permitAll()
//                         .requestMatchers(HttpMethod.GET, "/api/cotizaciones/**").hasRole("ADMIN")
//                         .requestMatchers(HttpMethod.PUT, "/api/cotizaciones/**").hasRole("ADMIN")
//                         .anyRequest().authenticated())
//                 .oauth2ResourceServer(oauth2 -> oauth2
//                         .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

//         return http.build();
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
package com.example.idgs15.authorization_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ApiSecurityConfig {

    private static final String[] STAFF_ROLES = {"ADMIN", "SERVICIO", "VENTAS", "MARKETING"};

    @Bean
    @Order(2)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/register", "/api/**")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register").hasAnyRole(STAFF_ROLES)
                        .requestMatchers(HttpMethod.POST, "/api/cotizaciones").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/cotizaciones/**").hasAnyRole(STAFF_ROLES)
                        .requestMatchers(HttpMethod.PUT, "/api/cotizaciones/**").hasAnyRole(STAFF_ROLES)
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

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