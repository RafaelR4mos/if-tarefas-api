package com.br.todo.dto.task;

import com.br.todo.entity.enumeration.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskDTO {
    private Integer idTarefa;
    private String nomeTarefa;
    private String descricao;
    private TaskStatus status;
    private Timestamp dtCriacao;
}
