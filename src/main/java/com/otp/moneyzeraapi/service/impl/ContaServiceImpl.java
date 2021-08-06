package com.otp.moneyzeraapi.service.impl;

import com.otp.moneyzeraapi.exception.RegraNegocioException;
import com.otp.moneyzeraapi.model.Conta;
import com.otp.moneyzeraapi.repository.ContaRepository;
import com.otp.moneyzeraapi.service.interfaces.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ContaServiceImpl implements ContaService {

    @Autowired
    private ContaRepository repository;

    @Override
    @Transactional
    public Conta salvar(Conta conta) {
        conta.setSaldo(new BigDecimal(0));
        return repository.save(conta);
    }

    @Override
    @Transactional
    public Conta atualizar(Conta conta) {
        if (conta.getId() == null || conta.getId() == 0) throw new RegraNegocioException("Necessário id para atualizar dados da conta");

        return repository.save(conta);
    }

    @Override
    @Transactional
    public Conta depositar(Conta conta, BigDecimal valor) {

        validar(valor);

        conta.setSaldo( conta.getSaldo().add(valor) );

        return conta;
    }

    @Override
    @Transactional
    public Conta retirar(Conta conta, BigDecimal valor) {

        validar(valor);

        conta.setSaldo( conta.getSaldo().subtract(valor) );

        return conta;
    }

    @Override
    @Transactional
    public void transferir(Conta contaOrigem, Conta contaDestino, BigDecimal valor) {

        validar(valor);
        retirar(contaOrigem, valor);
        depositar(contaDestino, valor);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (id == null || id == 0) throw new RegraNegocioException("Necessário id para deletar a conta");

        repository.deleteById(id);
    }

    @Override
    public List<Conta> buscarPorUsuarioId(Long id) {

        if (id == null || id == 0) throw new RegraNegocioException("Necessário id para obter usuario e as contas");

        return repository.findByUsuario_Id(id);
    }

    @Override
    public Optional<Conta> obterPorId(Long id) {

        if (id == null || id == 0) throw new RegraNegocioException("Necessário id para obter a conta");

        return repository.findById(id);
    }

    @Override
    public List<Conta> obter() {
        return repository.findAll();
    }

    public void validar(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) throw new RegraNegocioException("O valor não pode ser negativo ou nulo");
    }

}
