package com.otp.moneyzeraapi.service.interfaces;

import com.otp.moneyzeraapi.enums.StatusTransacao;
import com.otp.moneyzeraapi.model.Transacao;

import java.util.List;
import java.util.Optional;

public interface TransacaoService {

    Transacao salvar(Transacao transacao);

    Transacao atualizar(Transacao transacao);

    void deletar(Long id);

    List<Transacao> buscar(Transacao transacaoFiltro);

    void atualizarStatus(Transacao transacao, StatusTransacao status);

    void validar(Transacao transacao);

    Optional<Transacao> buscarPorId(Long id);
}
