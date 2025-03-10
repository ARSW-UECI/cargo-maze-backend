package com.cargomaze.cargo_maze.config;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import jakarta.servlet.http.HttpServletResponse;



@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(req -> req
                .requestMatchers(HttpMethod.OPTIONS).permitAll()
                .requestMatchers("/stompendpoint/**", "/error","/").permitAll()
                .requestMatchers("/cargoMaze/**").authenticated() // Requiere autenticación
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(authenticationEntryPoint()) // Manejar errores de autenticación
                .accessDeniedHandler(accessDeniedHandler())          // Manejar accesos denegados
            );

        return http.build();
    }


    // Personaliza el manejo de errores de autenticación
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            System.err.println("Error de autenticación: " + authException.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Autenticación requerida");
        };
    }

    // Personaliza el manejo de accesos denegados
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            System.err.println("Acceso denegado: " + accessDeniedException.getMessage());
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso no autorizado");
        };
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true); // Permitir cookies
        configuration.setAllowedOriginPatterns(Arrays.asList(
                "http://localhost:4200/*",
                "https://ashy-flower-01560480f.4.azurestaticapps.net/*",
                "https://proyectoarsw.duckdns.org/*",
                "https://login.microsoftonline.com/*",
                "http://localhost:4200",
                "https://ashy-flower-01560480f.4.azurestaticapps.net"

        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Métodos permitidos
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type", "*")); // Headers permitidos
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Disposition")); // Headers expuestos

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}



    
