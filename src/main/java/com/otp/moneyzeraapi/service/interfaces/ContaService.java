package com.otp.moneyzeraapi.service.interfaces;

import com.otp.moneyzeraapi.model.Conta;

import java.util.List;

public interface ContaService {

    Conta salvar(Conta conta);

    Conta atualizar(Conta conta);

    void deletar(Long id);

    List<Conta> buscarPorUsuarioId(Long id);
}
