package com.br.todo.dto.task;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskUpdateDTO {
    @Size(min = 3, max = 100, message = "O nome da tarefa pode ter de 3 a 100 caracteres apenas.")
    private String nomeTask;

    @Size(max = 255, message = "A descrição pode ter no máximo 255 caracteres.")
    private String descricao;
}
