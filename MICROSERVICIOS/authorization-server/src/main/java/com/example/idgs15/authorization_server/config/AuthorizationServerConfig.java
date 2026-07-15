// package com.example.idgs15.authorization_server.config;

// import java.security.KeyPair;
// import java.security.KeyPairGenerator;
// import java.security.interfaces.RSAPrivateKey;
// import java.security.interfaces.RSAPublicKey;
// import java.util.UUID;
// import java.util.stream.Collectors;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.core.annotation.Order;
// import org.springframework.http.MediaType;
// import org.springframework.security.config.Customizer;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.oauth2.core.AuthorizationGrantType;
// import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
// import org.springframework.security.oauth2.jwt.JwtDecoder;
// import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
// import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
// import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
// import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
// import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
// import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
// import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
// import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
// import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

// import com.nimbusds.jose.jwk.JWKSet;
// import com.nimbusds.jose.jwk.RSAKey;
// import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
// import com.nimbusds.jose.jwk.source.JWKSource;
// import com.nimbusds.jose.proc.SecurityContext;

// @Configuration
// public class AuthorizationServerConfig {

//     // Filtro de seguridad SOLO para los endpoints del Authorization Server (/oauth2/*, etc.)
//     @Bean
//     @Order(1)
//     public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
//         http
//                 .oauth2AuthorizationServer((authorizationServer) -> {
//                     http.securityMatcher(authorizationServer.getEndpointsMatcher());
//                     authorizationServer.oidc(Customizer.withDefaults());
//                 })
//                 .authorizeHttpRequests((authorize) -> authorize
//                         .requestMatchers("/.well-known/**", "/oauth2/jwks").permitAll()
//                         .anyRequest().authenticated())
//                 .exceptionHandling((exceptions) -> exceptions
//                         .defaultAuthenticationEntryPointFor(
//                                 new LoginUrlAuthenticationEntryPoint("/login"),
//                                 new MediaTypeRequestMatcher(MediaType.TEXT_HTML)));

//         return http.build();
//     }

//     // Cliente OAuth2 para Postman, con PKCE obligatorio (OAuth 2.1)
//     @Bean
//     public RegisteredClientRepository registeredClientRepository() {
//         RegisteredClient postmanClient = RegisteredClient.withId(UUID.randomUUID().toString())
//                 .clientId("postman-client")
//                 .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
//                 .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                 .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//                 .redirectUri("https://oauth.pstmn.io/v1/callback")
//                 .scope("read")
//                 .clientSettings(ClientSettings.builder()
//                         .requireProofKey(true)
//                         .requireAuthorizationConsent(true)
//                         .build())
//                 .build();

//         return new InMemoryRegisteredClientRepository(postmanClient);
//     }
    

//     // Configuración general del Authorization Server (issuer)
//     @Bean
//     public AuthorizationServerSettings authorizationServerSettings() {
//         return AuthorizationServerSettings.builder()
//                 .issuer("http://localhost:9000")
//                 .build();
//     }

//     // Llaves RSA para firmar los JWT
//     @Bean
//     public JWKSource<SecurityContext> jwkSource() {
//         KeyPair keyPair = generateRsaKey();
//         RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
//         RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

//         RSAKey rsaKey = new RSAKey.Builder(publicKey)
//                 .privateKey(privateKey)
//                 .keyID(UUID.randomUUID().toString())
//                 .build();

//         return new ImmutableJWKSet<>(new JWKSet(rsaKey));
//     }

//     @Bean
//     public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
//         return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
//     }

//     private static KeyPair generateRsaKey() {
//         try {
//             KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//             keyPairGenerator.initialize(2048);
//             return keyPairGenerator.generateKeyPair();
//         } catch (Exception ex) {
//             throw new IllegalStateException("Error generando el par de llaves RSA", ex);
//         }
//     }

//     // Agrega los roles del usuario como claim custom en el JWT
//     @Bean
//     public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
//         return (context) -> {
//             if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
//                 var roles = context.getPrincipal().getAuthorities().stream()
//                         .map(GrantedAuthority::getAuthority)
//                         .collect(Collectors.toList());
//                 context.getClaims().claim("roles", roles);
//             }
//         };
//     }
    
// }


// package com.example.idgs15.authorization_server.config;

// import java.security.KeyPair;
// import java.security.KeyPairGenerator;
// import java.security.interfaces.RSAPrivateKey;
// import java.security.interfaces.RSAPublicKey;
// import java.util.List;
// import java.util.UUID;
// import java.util.stream.Collectors;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.core.annotation.Order;
// import org.springframework.http.MediaType;
// import org.springframework.security.config.Customizer;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.oauth2.core.AuthorizationGrantType;
// import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
// import org.springframework.security.oauth2.jwt.JwtDecoder;
// import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
// import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
// import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
// import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
// import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
// import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
// import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
// import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
// import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.CorsConfigurationSource;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// import com.nimbusds.jose.jwk.JWKSet;
// import com.nimbusds.jose.jwk.RSAKey;
// import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
// import com.nimbusds.jose.jwk.source.JWKSource;
// import com.nimbusds.jose.proc.SecurityContext;

// @Configuration
// public class AuthorizationServerConfig {

//     // Filtro de seguridad SOLO para los endpoints del Authorization Server (/oauth2/*, etc.)
//     @Bean
//     @Order(1)
//     public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
//         http
//                 .cors(Customizer.withDefaults())
//                 .oauth2AuthorizationServer((authorizationServer) -> {
//                     http.securityMatcher(authorizationServer.getEndpointsMatcher());
//                     authorizationServer.oidc(Customizer.withDefaults());
//                 })
//                 .authorizeHttpRequests((authorize) -> authorize
//                         .requestMatchers("/.well-known/**", "/oauth2/jwks").permitAll()
//                         .anyRequest().authenticated())
//                 .exceptionHandling((exceptions) -> exceptions
//                         .defaultAuthenticationEntryPointFor(
//                                 new LoginUrlAuthenticationEntryPoint("/login"),
//                                 new MediaTypeRequestMatcher(MediaType.TEXT_HTML)));

//         return http.build();
//     }

//     // Clientes OAuth2: Postman (pruebas) y React (SPA con PKCE)
//     @Bean
//     public RegisteredClientRepository registeredClientRepository() {
//         RegisteredClient postmanClient = RegisteredClient.withId(UUID.randomUUID().toString())
//                 .clientId("postman-client")
//                 .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
//                 .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                 .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//                 .redirectUri("http://localhost:5173/callback")
//                 .scope("read")
//                 .clientSettings(ClientSettings.builder()
//                         .requireProofKey(true)
//                         .requireAuthorizationConsent(true)
//                         .build())
//                 .build();

//         RegisteredClient reactClient = RegisteredClient.withId(UUID.randomUUID().toString())
//                 .clientId("react-client")
//                 .clientAuthenticationMethod(ClientAuthenticationMethod.NONE) // cliente público (SPA)
//                 .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                 .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//                 .redirectUri("http://localhost:3000/callback")
//                 .scope("openid")
//                 .scope("read")
//                 .clientSettings(ClientSettings.builder()
//                         .requireProofKey(true) // PKCE obligatorio
//                         .requireAuthorizationConsent(false) // sin pantalla de consentimiento
//                         .build())
//                 .build();

//         return new InMemoryRegisteredClientRepository(postmanClient, reactClient);
//     }

//     // Configuración general del Authorization Server (issuer)
//     @Bean
//     public AuthorizationServerSettings authorizationServerSettings() {
//         return AuthorizationServerSettings.builder()
//                 .issuer("http://localhost:9000")
//                 .build();
//     }

//     // Llaves RSA para firmar los JWT
//     @Bean
//     public JWKSource<SecurityContext> jwkSource() {
//         KeyPair keyPair = generateRsaKey();
//         RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
//         RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

//         RSAKey rsaKey = new RSAKey.Builder(publicKey)
//                 .privateKey(privateKey)
//                 .keyID(UUID.randomUUID().toString())
//                 .build();

//         return new ImmutableJWKSet<>(new JWKSet(rsaKey));
//     }

//     @Bean
//     public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
//         return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
//     }

//     private static KeyPair generateRsaKey() {
//         try {
//             KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//             keyPairGenerator.initialize(2048);
//             return keyPairGenerator.generateKeyPair();
//         } catch (Exception ex) {
//             throw new IllegalStateException("Error generando el par de llaves RSA", ex);
//         }
//     }

//     // Agrega los roles del usuario como claim custom en el JWT
//     @Bean
//     public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
//         return (context) -> {
//             if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
//                 var roles = context.getPrincipal().getAuthorities().stream()
//                         .map(GrantedAuthority::getAuthority)
//                         .collect(Collectors.toList());
//                 context.getClaims().claim("roles", roles);
//             }
//         };
//     }

//     // Configuración CORS para permitir llamadas desde el frontend React
//     @Bean
//     public CorsConfigurationSource corsConfigurationSource() {
//         CorsConfiguration config = new CorsConfiguration();
//         config.setAllowedOrigins(List.of("http://localhost:3000"));
//         config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//         config.setAllowedHeaders(List.of("*"));
//         config.setAllowCredentials(true);

//         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//         source.registerCorsConfiguration("/**", config);
//         return source;
//     }
// }


// package com.example.idgs15.authorization_server.config;

// import java.security.KeyPair;
// import java.security.KeyPairGenerator;
// import java.security.interfaces.RSAPrivateKey;
// import java.security.interfaces.RSAPublicKey;
// import java.util.List;
// import java.util.UUID;
// import java.util.stream.Collectors;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.core.annotation.Order;
// import org.springframework.http.MediaType;
// import org.springframework.security.config.Customizer;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.oauth2.core.AuthorizationGrantType;
// import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
// import org.springframework.security.oauth2.jwt.JwtDecoder;
// import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
// import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
// import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
// import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
// import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
// import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
// import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
// import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
// import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.CorsConfigurationSource;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// import com.nimbusds.jose.jwk.JWKSet;
// import com.nimbusds.jose.jwk.RSAKey;
// import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
// import com.nimbusds.jose.jwk.source.JWKSource;
// import com.nimbusds.jose.proc.SecurityContext;

// @Configuration
// public class AuthorizationServerConfig {

//     @Bean
//     @Order(1)
//     public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {

//         OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

//         http
//                 .cors(Customizer.withDefaults())
//                 .getConfigurer(
//                         org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer.class)
//                 .oidc(Customizer.withDefaults());

//         http.exceptionHandling(exceptions -> exceptions
//                 .defaultAuthenticationEntryPointFor(
//                         new LoginUrlAuthenticationEntryPoint("/login"),
//                         new MediaTypeRequestMatcher(MediaType.TEXT_HTML)));

//         return http.build();
//     }

//     @Bean
//     public RegisteredClientRepository registeredClientRepository() {

//         RegisteredClient reactClient = RegisteredClient.withId(UUID.randomUUID().toString())
//                 .clientId("react-client")
//                 .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
//                 .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                 .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//                 .redirectUri("http://localhost:5173/callback")
//                 .scope("openid")
//                 .scope("read")
//                 .clientSettings(ClientSettings.builder()
//                         .requireProofKey(true)
//                         .requireAuthorizationConsent(false)
//                         .build())
//                 .build();

//         RegisteredClient postmanClient = RegisteredClient.withId(UUID.randomUUID().toString())
//                 .clientId("postman-client")
//                 .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
//                 .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                 .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//                 .redirectUri("http://localhost:5173/callback")
//                 .scope("openid")
//                 .scope("read")
//                 .clientSettings(ClientSettings.builder()
//                         .requireProofKey(true)
//                         .requireAuthorizationConsent(true)
//                         .build())
//                 .build();

//         return new InMemoryRegisteredClientRepository(
//                 reactClient,
//                 postmanClient);
//     }

//     @Bean
//     public AuthorizationServerSettings authorizationServerSettings() {
//         return AuthorizationServerSettings.builder()
//                 .issuer("http://localhost:9000")
//                 .build();
//     }

//     @Bean
//     public JWKSource<SecurityContext> jwkSource() {

//         KeyPair keyPair = generateRsaKey();

//         RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
//         RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

//         RSAKey rsaKey = new RSAKey.Builder(publicKey)
//                 .privateKey(privateKey)
//                 .keyID(UUID.randomUUID().toString())
//                 .build();

//         JWKSet jwkSet = new JWKSet(rsaKey);

//         return new ImmutableJWKSet<>(jwkSet);
//     }

//     @Bean
//     public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
//         return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
//     }

//     private static KeyPair generateRsaKey() {
//         try {
//             KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
//             generator.initialize(2048);
//             return generator.generateKeyPair();
//         } catch (Exception ex) {
//             throw new IllegalStateException(ex);
//         }
//     }

//     @Bean
//     public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
//         return context -> {

//             if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {

//                 List<String> roles = context.getPrincipal()
//                         .getAuthorities()
//                         .stream()
//                         .map(GrantedAuthority::getAuthority)
//                         .collect(Collectors.toList());

//                 context.getClaims().claim("roles", roles);
//             }
//         };
//     }

//     @Bean
//     public CorsConfigurationSource corsConfigurationSource() {

//         CorsConfiguration config = new CorsConfiguration();

//         config.setAllowedOrigins(List.of(
//                 "http://localhost:5173",
//                 "http://localhost:3000"));

//         config.setAllowedMethods(List.of(
//                 "GET",
//                 "POST",
//                 "PUT",
//                 "DELETE",
//                 "OPTIONS"));

//         config.setAllowedHeaders(List.of("*"));
//         config.setAllowCredentials(true);

//         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//         source.registerCorsConfiguration("/**", config);

//         return source;
//     }

// }

package com.example.idgs15.authorization_server.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
public class AuthorizationServerConfig {

    // Filtro de seguridad SOLO para los endpoints del Authorization Server (/oauth2/*, etc.)
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .oauth2AuthorizationServer((authorizationServer) -> {
                    http.securityMatcher(authorizationServer.getEndpointsMatcher());
                    authorizationServer.oidc(Customizer.withDefaults());
                })
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/.well-known/**", "/oauth2/jwks").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)));

        return http.build();
    }

    // Clientes OAuth2: React (SPA con PKCE) y Postman (pruebas)
    @Bean
    public RegisteredClientRepository registeredClientRepository() {

        RegisteredClient reactClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("react-client")
                .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://localhost:5173/callback")
                .scope("openid")
                .scope("read")
                .clientSettings(ClientSettings.builder()
                        .requireProofKey(true)
                        .requireAuthorizationConsent(false)
                        .build())
                .build();

        RegisteredClient postmanClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("postman-client")
                .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("https://oauth.pstmn.io/v1/callback")
                .scope("openid")
                .scope("read")
                .clientSettings(ClientSettings.builder()
                        .requireProofKey(true)
                        .requireAuthorizationConsent(true)
                        .build())
                .build();

        return new InMemoryRegisteredClientRepository(reactClient, postmanClient);
    }

    // Configuración general del Authorization Server (issuer)
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer("http://localhost:9000")
                .build();
    }

    // Llaves RSA para firmar los JWT
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();

        return new ImmutableJWKSet<>(new JWKSet(rsaKey));
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    private static KeyPair generateRsaKey() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException("Error generando el par de llaves RSA", ex);
        }
    }

    // Agrega los roles del usuario como claim custom en el JWT
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
        return (context) -> {
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                var roles = context.getPrincipal().getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList());
                context.getClaims().claim("roles", roles);
            }
        };
    }

    // Configuración CORS para permitir llamadas desde el frontend
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}