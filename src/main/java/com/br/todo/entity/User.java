package com.br.todo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity(name = "user")
@Table(name = "tb_usuarios")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_usuario")
    private String idUsuario;

    @Column(name = "nm_usuario")
    private String nomeUsuario;

    @Column(name = "email_usuario")
    @JsonProperty("emailUsuario")
    private String username;

    @JsonIgnore
    @Column(name = "senha_usuario")
    private String senhaUsuario;

    @OneToMany(mappedBy = "usuario", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Task> tarefas = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return senhaUsuario;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
