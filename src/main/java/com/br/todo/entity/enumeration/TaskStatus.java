package com.br.todo.entity.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskStatus {
    P("PENDENTE"),
    F("FINALIZADA");

    private final String status;
}
