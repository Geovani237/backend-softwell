package com.example.softwell.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority; // ✅ 1. Importe esta classe
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List; // ✅ 2. Importe a classe List
import java.util.stream.Collectors; // ✅ 3. Importe a classe Collectors

@Data
@Document(collection = "usuarios")
public class User implements UserDetails {

    @Id
    private String id;

    private String cpf;
    private String username;
    private String password;

    // ✅ 4. Adiciona o campo para armazenar as permissões (roles) do usuário.
    // O MongoDB irá mapear este campo para a lista de strings no seu documento.
    private List<String> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // ✅ 5. CORREÇÃO VITAL:
        // Este método agora lê a lista de 'roles' (ex: ["ROLE_ADMIN", "ROLE_USER"])
        // e a converte para o formato que o Spring Security entende.
        if (this.roles == null) {
            return List.of(); // Retorna lista vazia se o campo 'roles' for nulo
        }
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    // O restante da classe permanece o mesmo, pois já está correto.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}


//package com.example.softwell.model;
//
//import lombok.Data;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//import java.util.Collections;
//
//@Data
//@Document(collection = "usuarios")
//public class User implements UserDetails {
//
//    @Id
//    private String id;
//
//
//    private String cpf;
//    private String username;
//    private String password;
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        // Por enquanto, vamos retornar uma coleção vazia
//        // Você pode implementar roles de usuários aqui
//        return Collections.emptyList();
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//
//
//}
