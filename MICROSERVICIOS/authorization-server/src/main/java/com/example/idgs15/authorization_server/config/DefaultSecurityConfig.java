// package com.example.idgs15.authorization_server.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.core.annotation.Order;
// import org.springframework.http.HttpMethod;
// import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.crypto.factory.PasswordEncoderFactories;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

// @Configuration
// @EnableMethodSecurity
// public class DefaultSecurityConfig {

//     @Bean
//     @Order(2)
//     public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//         http
//                 .csrf(csrf -> csrf.ignoringRequestMatchers(
//                         "/h2-console/**", "/api/cotizaciones", "/api/cotizaciones/**"))
//                 .headers(headers -> headers
//                         .frameOptions(frame -> frame.sameOrigin()))
//                 .authorizeHttpRequests(auth -> auth
//                         .requestMatchers("/h2-console/**").permitAll()
//                         .requestMatchers("/register").hasRole("ADMIN")
//                         .requestMatchers(HttpMethod.POST, "/api/cotizaciones").permitAll()
//                         .requestMatchers(HttpMethod.GET, "/api/cotizaciones/**").hasRole("ADMIN")
//                         .requestMatchers(HttpMethod.PUT, "/api/cotizaciones/**").hasRole("ADMIN")
//                         .anyRequest().authenticated())
//                 .formLogin(form -> form
//                         .successHandler(new SavedRequestAwareAuthenticationSuccessHandler())
//                 )
//                 .logout(logout -> logout
//                         .logoutUrl("/logout")
//                         .logoutSuccessUrl("http://localhost:5173/login")
//                         .invalidateHttpSession(true)
//                         .deleteCookies("JSESSIONID")
//                 );

//         return http.build();
//     }

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//     }
// }

package com.example.idgs15.authorization_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

@Configuration
@EnableMethodSecurity
public class DefaultSecurityConfig {

    // Filtro para navegador: pantalla de login (formulario), logout, h2-console.
    // Ya NO maneja /register ni /api/** (eso lo cubre ApiSecurityConfig con JWT).
    @Bean
    @Order(3)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .successHandler(new SavedRequestAwareAuthenticationSuccessHandler())
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("http://localhost:5173/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}