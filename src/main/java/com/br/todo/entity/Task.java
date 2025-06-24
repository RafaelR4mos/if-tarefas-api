package com.br.todo.entity;

import com.br.todo.entity.enumeration.TaskStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity(name = "task")
@Table(name = "tb_tarefas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_tarefa")
    @SequenceGenerator(name = "seq_id_tarefa", sequenceName = "seq_id_tarefa", allocationSize = 1)
    @Column(name = "id_tarefa")
    private Integer idTarefa;

    @Column(name = "nm_tarefa")
    private String nomeTarefa;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Column(name = "dt_criacao")
    @CreationTimestamp
    private Timestamp dtCriacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private User usuario;
}
