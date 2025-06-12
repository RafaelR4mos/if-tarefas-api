package com.br.todo.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserCreateDTO {
    @NotNull(message = "O nome do usuário é obrigatório")
    private String nomeUsuario;

    @NotNull(message = "O email do usuário é obrigatório")
    @Email(message = "O e-mail deve ser fornecido no formato válido")
    private String emailUsuario;

    @NotNull(message = "A senha do usuário é obrigatória")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{6,20}$",
            message = "A senha deve conter entre 6 e 20 caracteres, pelo menos um dígito, uma letra minúscula, uma letra maiúscula e um caractere especial.")
    private String senhaUsuario;
}

