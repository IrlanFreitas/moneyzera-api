package com.otp.moneyzeraapi.service.interfaces;

import com.otp.moneyzeraapi.model.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaService {

    Categoria salvar(Categoria categoria);

    Categoria atualizar(Categoria categoria);

    void deletar(Long id);

    List<Categoria> listar();

//    List<Categoria> BuscarPorUsuarioId(Long id);

    Optional<Categoria> obterPorId(Long id);
}
