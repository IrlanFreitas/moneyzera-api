package com.otp.moneyzeraapi.service.interfaces;

import com.otp.moneyzeraapi.model.Usuario;

public interface UsuarioService {

    Usuario autenticar(String email, String senha);

    Usuario salvar(Usuario usuario);

    void validarEmail(String email);

    Usuario atualizar(Usuario usuario);

    void deletar(Long id);

    Usuario buscar(Long id);

}
