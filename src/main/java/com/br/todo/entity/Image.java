package com.br.todo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "image")
@Table(name = "tb_imagens")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_imagem")
    @SequenceGenerator(name = "seq_id_imagem", sequenceName = "seq_id_imagem", allocationSize = 1)
    @Column(name = "id_imagem")
    private Integer idImagem;

    @Column(name = "conteudo_imagem")
    private byte[] conteudoImagem;

    @Column(name = "descricao")
    private String descricao;

    @OneToOne
    @JoinColumn(name = "id_tarefa", referencedColumnName = "id_tarefa")
    private Task tarefa;
}
