package com.otp.moneyzeraapi.dto;

import com.otp.moneyzeraapi.enums.StatusTransacao;
import com.otp.moneyzeraapi.enums.TipoCategoria;
import com.otp.moneyzeraapi.exception.RegraNegocioException;
import com.otp.moneyzeraapi.model.Categoria;
import com.otp.moneyzeraapi.model.Conta;
import com.otp.moneyzeraapi.model.Transacao;
import com.otp.moneyzeraapi.service.interfaces.CategoriaService;
import com.otp.moneyzeraapi.service.interfaces.ContaService;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class TransacaoDto {

    private String nome;
    private String descricao;
    private LocalDate data;
    private Long contaOrigemId;
    private Long contaDestinoId;
    private Long categoriaId;
    private BigDecimal valor;
    private String status;

    //TODO Regra lógica de conversão, podem ficar aqui ?
    public Transacao converter(ContaService contaService,
                               CategoriaService categoriaService) {

        final Transacao.TransacaoBuilder builder = Transacao.builder()
                .nome(nome)
                .descricao(descricao)
                .data(data)
                .contaOrigem(obterConta(contaService, contaOrigemId))
                .status(StatusTransacao.valueOf(status))
                .valor(valor);

        final Categoria categoria = obterCategoria(categoriaService, categoriaId);
        builder.categoria(categoria);

        if(categoria.getTipo().equals(TipoCategoria.TRANSFERENCIA)) {
            builder.contaDestino(obterConta(contaService, contaDestinoId));
        }

        return builder.build();
    }

    private Conta obterConta(ContaService contaService, Long id) {
        return contaService.obterPorId(id)
                .orElseThrow(
                        () -> new RegraNegocioException("Conta não encontrada para o id informado."));
    }

    private Categoria obterCategoria(CategoriaService categoriaService, Long id) {

        return categoriaService.obterPorId(id)
                .orElseThrow(
                        () -> new RegraNegocioException("Categoria não encontrada para o id informado."));
    }
}
