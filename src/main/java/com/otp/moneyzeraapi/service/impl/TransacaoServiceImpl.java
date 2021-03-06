package com.otp.moneyzeraapi.service.impl;

import com.otp.moneyzeraapi.enums.StatusTransacao;
import com.otp.moneyzeraapi.enums.TipoCategoria;
import com.otp.moneyzeraapi.exception.RegraNegocioException;
import com.otp.moneyzeraapi.model.Transacao;
import com.otp.moneyzeraapi.repository.TransacaoRepository;
import com.otp.moneyzeraapi.service.interfaces.ContaService;
import com.otp.moneyzeraapi.service.interfaces.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TransacaoServiceImpl implements TransacaoService {

    @Autowired
    private TransacaoRepository repository;

    @Autowired
    private ContaService contaService;

    @Override
    @Transactional
    public Transacao salvar(Transacao transacao) {

        validar(transacao);
        transacao.setId(null);

        movimentar(transacao);

        return repository.save(transacao);
    }

    @Override
    @Transactional
    public Transacao atualizar(Transacao transacao) {

        if (transacao.getId() == null || transacao.getId() == 0)
            throw new IllegalArgumentException("Necessário id para atualizar a transação");

        validar(transacao);

        final Optional<Transacao> transacaoEncontrada = buscarPorId(transacao.getId());

        if (transacaoEncontrada.isEmpty()) throw new RegraNegocioException("Transação não encontrada.");

        reverterMovimentacao(transacaoEncontrada.get());

        movimentar(transacao);

        return repository.save(transacao);
    }

    @Override
    @Transactional
    public void deletar(Long id) {

        if (id == null || id == 0)
            throw new IllegalArgumentException("Necessário id para atualizar a transação");

        final Optional<Transacao> transacao = buscarPorId(id);

        transacao.ifPresent(this::reverterMovimentacao);

        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Transacao> buscar(Transacao transacaoFiltro) {

        // ** Query By Example Spring
        //TODO Melhorar a implementação, com certeza não vai pegar pela data do jeito certo.
        Example<Transacao> consulta = Example.of(transacaoFiltro,
                ExampleMatcher.matching()
                        .withIgnoreCase()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));


        return repository.findAll(consulta);
    }

    @Override
    @Transactional
    public void atualizarStatus(Transacao transacao, StatusTransacao status) {
        //TODO Os status devem alterar o total da conta
        transacao.setStatus(status);
        atualizar(transacao);
    }

    //TODO Verificar se há como melhorar isso aqui
    @Override
    public void validar(Transacao transacao) {

        if (transacao.getDescricao() == null || transacao.getDescricao().trim().equals("")) {
            throw new RegraNegocioException("Informe uma descrição válida.");
        }

        if (transacao.getData() == null) {
            throw new RegraNegocioException("Informe uma data válida.");
        }

        if (transacao.getContaOrigem().getId() == null) {
            throw new RegraNegocioException("Informe uma conta já cadastrada.");
        }

        if (transacao.getContaOrigem().getUsuario().getId() == null) {
            throw new RegraNegocioException("Informe um usuário já cadastrado.");
        }

        if (transacao.getCategoria().getTipo().equals(TipoCategoria.TRANSFERENCIA)) {

            if ( transacao.getContaDestino().getId() == null) {
                throw new RegraNegocioException("Informe uma conta destino já cadastrada.");
            }

            if (transacao.getContaDestino().getUsuario().getId() == null) {
                throw new RegraNegocioException("Informe um usuário na conta destino já cadastrado.");
            }
        }

        if (transacao.getValor() == null || transacao.getValor().compareTo(BigDecimal.ZERO) < 1) {
            throw new RegraNegocioException("Informe um valor válido.");
        }

        if (transacao.getCategoria() == null) {
            throw new RegraNegocioException("Informe uma categoria.");
        }
    }

    @Override
    public Optional<Transacao> buscarPorId(Long id) {

        if (id == null || id == 0)
            throw new IllegalArgumentException("Necessário id para buscar a transação");

        return repository.findById(id);
    }

    @Transactional
    private void movimentar(Transacao transacao) {
        switch (transacao.getCategoria().getTipo()) {
            case RECEITA:
                contaService.depositar(transacao.getContaOrigem(), transacao.getValor());
                break;
            case DESPESA:
                contaService.retirar(transacao.getContaOrigem(), transacao.getValor());
                break;
            case TRANSFERENCIA:
                contaService.transferir(transacao.getContaOrigem(), transacao.getContaDestino(), transacao.getValor());
                break;
        }
    }

    @Transactional
    private void reverterMovimentacao(Transacao transacao) {
        switch (transacao.getCategoria().getTipo()) {
            case RECEITA:
                contaService.retirar(transacao.getContaOrigem(), transacao.getValor());
                break;
            case DESPESA:
                contaService.depositar(transacao.getContaOrigem(), transacao.getValor());
                break;
            case TRANSFERENCIA:
                contaService.transferir(transacao.getContaDestino(), transacao.getContaOrigem(), transacao.getValor());
                break;
        }
    }
}
