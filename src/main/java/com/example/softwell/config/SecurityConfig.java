package com.example.softwell.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

// Esse arquivo retira a necessidade de login e senha para acessar os dados da api

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Este filtro é para as rotas que precisam de autenticação
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated() // Todas as outras requisições requerem autenticação
                )
                .csrf(csrf -> csrf.disable()); // Desabilita CSRF para APIs REST

        return http.build();
    }

    // Este customizador IGNORA completamente o endpoint de humores
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/api/humores");
    }
}