package com.example.softwell.Security;

import com.example.softwell.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults; // ✅ 1. Importe esta classe
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, CustomUserDetailsService userDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Rotas Públicas (login e registro)
                        .requestMatchers("/softwell/auth/**").permitAll()

                        // Rotas que podem ser acessadas por qualquer um (mesmo sem login)
                        .requestMatchers("/api/humores/**").permitAll()
                        .requestMatchers("/act/**").permitAll()

                        // ✅ REGRA CORRETA: Rota de análise SÓ PARA ADMIN
                        .requestMatchers("/api/psychosocial/analysis/**").hasRole("ADMIN")

                        // Rotas que exigem apenas autenticação (qualquer usuário logado)
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ✅✅✅ 2. ADICIONE ESTE BEAN PARA REMOVER O PREFIXO "ROLE_" ✅✅✅
    /**
     * Este Bean remove o prefixo padrão "ROLE_" que o Spring Security espera.
     * Ao retornar uma string vazia, a verificação hasRole("ADMIN") buscará
     * pela permissão "ADMIN" exatamente como está no banco de dados,
     * em vez de procurar por "ROLE_ADMIN".
     */
    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); // O argumento é uma string vazia
    }
}



//package com.example.softwell.Security;
//
//import com.example.softwell.service.CustomUserDetailsService;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.ProviderManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    private final JwtAuthenticationFilter jwtAuthenticationFilter;
//    private final CustomUserDetailsService userDetailsService;
//
//    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, CustomUserDetailsService userDetailsService) {
//        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
//        this.userDetailsService = userDetailsService;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        // 1. Rotas Públicas (login e registro)
//                        .requestMatchers("/softwell/auth/**").permitAll()
//                        .requestMatchers("/api/humores/**").permitAll()
//                        .requestMatchers("/act/**").permitAll()
//
//                        // 2. Rota para ADMIN: Todas as análises psicossociais
//                        // Qualquer URL começando com /api/psychosocial/analysis/ só pode ser acessada por um ADMIN.
//                        .requestMatchers("/api/psychosocial/analysis/**").hasRole("ADMIN")
//
//                        // 3. Rota para Usuário Autenticado: Envio do formulário
//                        // A URL /api/psychosocial/submit pode ser acessada por qualquer usuário logado.
//                        .requestMatchers("/api/psychosocial/submit").authenticated()
//
//                        // 4. Regra Geral: Qualquer outra requisição exige autenticação
//                        .anyRequest().authenticated()
//                )
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//
////    @Bean
////    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
////        http
////                .csrf(csrf -> csrf.disable()) // Desabilita CSRF para API REST
////                .authorizeHttpRequests(auth -> auth
////                        .requestMatchers("/softwell/auth/**").permitAll() // Permite acesso público a endpoints de autenticação
////                        .requestMatchers("/api/humores/**").permitAll()
////
////                        // **NOVO:** Permite acesso a todas as rotas do Controller de Atividades (ex: /act/report, /act/activity)
////                        .requestMatchers("/act/**").permitAll()
////
////                        .requestMatchers("/api/psychosocial/**").permitAll()
////                        .anyRequest().authenticated() // Exige autenticação para todas as outras requisições
////                )
////                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Não usa sessão, é stateless
////                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Adiciona nosso filtro JWT
////
////        return http.build();
////    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService);
//        authProvider.setPasswordEncoder(passwordEncoder());
//        return new ProviderManager(authProvider);
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//}
