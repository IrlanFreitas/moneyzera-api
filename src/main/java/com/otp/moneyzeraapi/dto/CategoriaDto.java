package com.otp.moneyzeraapi.dto;

import com.otp.moneyzeraapi.enums.TipoCategoria;
import com.otp.moneyzeraapi.model.Categoria;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class CategoriaDto {

    @NotNull @NotEmpty
    private String nome;
    private String descricao;
    private String tipo;
    private Long usuarioId;

    public Categoria converter() {
        return Categoria.builder()
                .nome(nome)
                .descricao(descricao)
                .tipo(TipoCategoria.valueOf(tipo))
                .build();
    }
}
