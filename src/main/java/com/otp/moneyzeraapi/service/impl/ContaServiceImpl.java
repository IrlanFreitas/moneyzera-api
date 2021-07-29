package com.otp.moneyzeraapi.service.impl;

import com.otp.moneyzeraapi.exception.RegraNegocioException;
import com.otp.moneyzeraapi.model.Conta;
import com.otp.moneyzeraapi.repository.ContaRepository;
import com.otp.moneyzeraapi.service.interfaces.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContaServiceImpl implements ContaService {

    @Autowired
    private ContaRepository repository;

    @Override
    public Conta salvar(Conta conta) {
        return repository.save(conta);
    }

    @Override
    public Conta atualizar(Conta conta) {
        if (conta.getId() == null || conta.getId() == 0) throw new RegraNegocioException("Necessário id para atualizar dados da conta");

        return repository.save(conta);
    }

    @Override
    public void deletar(Long id) {
        if (id == null || id == 0) throw new RegraNegocioException("Necessário id para deletar a conta");

        repository.deleteById(id);
    }

    @Override
    public List<Conta> buscarPorUsuarioId(Long id) {

        if (id == null || id == 0) throw new RegraNegocioException("Necessário id para deletar a conta");

        return repository.findByUsuario_Id(id);
    }
}
