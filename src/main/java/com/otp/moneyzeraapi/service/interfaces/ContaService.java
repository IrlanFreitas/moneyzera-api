package com.otp.moneyzeraapi.service.interfaces;

import com.otp.moneyzeraapi.model.Conta;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ContaService {

    Conta salvar(Conta conta);

    Conta atualizar(Conta conta);

    Conta depositar(Conta conta, BigDecimal valor);

    Conta retirar(Conta conta, BigDecimal valor);

    void deletar(Long id);

    List<Conta> buscarPorUsuarioId(Long id);

    Optional<Conta> obterPorId(Long id);

    List<Conta> obter();

    void transferir(Conta contaOrigem, Conta contaDestino, BigDecimal valor);
}
