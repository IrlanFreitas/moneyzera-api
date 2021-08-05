package com.otp.moneyzeraapi.form;

import com.otp.moneyzeraapi.exception.RegraNegocioException;
import com.otp.moneyzeraapi.model.Conta;
import com.otp.moneyzeraapi.model.Usuario;
import com.otp.moneyzeraapi.service.interfaces.UsuarioService;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class ContaForm {

    private Long id;
    private String nome;
    private String descricao;
    @NotNull
    private Long usuarioId;

    public Conta converter(UsuarioService usuarioService) {
        return Conta.builder()
                .id(id)
                .nome(nome)
                .descricao(descricao)
                .usuario(obterUsuario(usuarioService))
                .build();
    }

    private Usuario obterUsuario(UsuarioService usuarioService) {
        return usuarioService
                .obterPorId(usuarioId)
                .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado para o id informado."));
    }

}
