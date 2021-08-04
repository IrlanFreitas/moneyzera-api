package com.otp.moneyzeraapi.service.impl;

import com.otp.moneyzeraapi.exception.RegraNegocioException;
import com.otp.moneyzeraapi.model.Categoria;
import com.otp.moneyzeraapi.repository.CategoriaRepository;
import com.otp.moneyzeraapi.service.interfaces.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepository repository;

    @Override
    public Categoria salvar(Categoria categoria) {
        return repository.save(categoria);
    }

    @Override
    public Categoria atualizar(Categoria categoria) {

        if (categoria.getId() == null || categoria.getId() == 0) throw new RegraNegocioException("Necessário id para atualizar categoria");

        return repository.save(categoria);
    }

    @Override
    public void deletar(Long id) {
        if (id == null || id == 0) throw new RegraNegocioException("Necessário id para remover categoria");

        repository.deleteById(id);
    }

    @Override
    public List<Categoria> listar() {
        return repository.findAll();
    }

    @Override
    public Optional<Categoria> obterPorId(Long id) {

        if (id == null || id == 0) throw new RegraNegocioException("Necessário id para buscar categoria");

        return repository.findById(id);
    }

//    @Override
//    public List<Categoria> buscarPorUsuarioId(Long id) {
//        return null;
//    }

}
