package com.br.todo.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    private String idUsuario;
    private String nomeUsuario;

    @JsonProperty("emailUsuario")
    private String username;
    private String senhaUsuario;
}
