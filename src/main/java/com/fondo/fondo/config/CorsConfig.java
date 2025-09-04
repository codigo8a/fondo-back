package com.fondo.fondo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        
        // Permitir orígenes específicos (ajusta según tu frontend)
        corsConfiguration.setAllowedOriginPatterns(Arrays.asList("*"));
        
        // Permitir métodos HTTP
        corsConfiguration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"
        ));
        
        // Permitir headers
        corsConfiguration.setAllowedHeaders(Arrays.asList(
            "Origin", "Content-Type", "Accept", "Authorization", 
            "Access-Control-Request-Method", "Access-Control-Request-Headers"
        ));
        
        // Permitir credenciales
        corsConfiguration.setAllowCredentials(true);
        
        // Configurar para todas las rutas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        
        return new CorsFilter(source);
    }
}