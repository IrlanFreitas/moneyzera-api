package com.otp.moneyzeraapi.form;

import com.otp.moneyzeraapi.enums.StatusTransacao;
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
public class TransacaoForm {

    private Long id;
    private String descricao;
    private LocalDate data;
    private Long contaOrigemId;
    private Long contaDestinoId;
    private Long categoriaId;
    private BigDecimal valor;
    private String status;

    public Transacao converter(ContaService contaService,
                               CategoriaService categoriaService) {

        return Transacao.builder()
                .id(id)
                .descricao(descricao)
                .data(data)
                .contaOrigem(obterConta(contaService, contaOrigemId))
                .contaDestino(obterConta(contaService, contaDestinoId))
                .categoria(obterCategoria(categoriaService, categoriaId))
                .status(StatusTransacao.valueOf(status))
                .valor(valor)
                .build();


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
