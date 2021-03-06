package com.otp.moneyzeraapi.service.interfaces;

import com.otp.moneyzeraapi.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    Usuario autenticar(String email, String senha);

    Usuario salvar(Usuario usuario);

    void validarEmail(String email);

    Usuario atualizar(Usuario usuario);

    void deletar(Long id);

    Optional<Usuario> obterPorId(Long id);

    Optional<Usuario> obterPorEmail(String email);

    List<Usuario> obterTodos();

}
