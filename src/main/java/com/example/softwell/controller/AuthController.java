package com.example.softwell.controller;

import com.example.softwell.Security.JwtAuthenticationFilter;
import com.example.softwell.Security.JwtTokenUtil;
import com.example.softwell.model.User;
import com.example.softwell.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/softwell/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthController(AuthenticationManager authenticationManager, JwtAuthenticationFilter jwtAuthenticationFilter, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials){
        String username = credentials.get("username");
        String password = credentials.get("password");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User newUser){
        if(userRepository.findByUsername(newUser.getUsername()).isPresent()){
            return ResponseEntity.badRequest().body("Username ja existe");
        }
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        userRepository.save(newUser);
        return ResponseEntity.ok("Usuario registrado com sucesso");
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(userRepository.findAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<?> adminEndpoint(Authentication authentication) {
        String username = authentication.getName();
        User user = (User) authentication.getPrincipal();// converter para User
        String id = user.getId(); //extrair o id
        List<String> roles = authentication.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .toList();

        return ResponseEntity.ok("Acesso de "+roles+" para "+username+" com id "+id);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestBody Map<String, String> body) {
        String id = body.get("id");
        userRepository.deleteById(id);
    }


}
