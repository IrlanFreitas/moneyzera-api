package com.otp.moneyzeraapi.dto;

import com.otp.moneyzeraapi.model.Usuario;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class UsuarioDto {

    @NotNull @NotEmpty
    private String nome;
    @NotNull @NotEmpty
    private String email;
    @NotNull @NotEmpty
    private String senha;

    public Usuario getUsuario() {
        return Usuario.builder()
                .nome(this.nome)
                .email(this.email)
                .senha(this.senha)
                .build();
    }
}
