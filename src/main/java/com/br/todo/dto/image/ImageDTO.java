package com.br.todo.dto.image;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageDTO {
    private Integer idImagem;
    private byte[] conteudoImagem;
    private String descricao;
}
