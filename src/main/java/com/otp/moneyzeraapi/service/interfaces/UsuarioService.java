package com.otp.moneyzeraapi.service.interfaces;

import com.otp.moneyzeraapi.model.Usuario;

public interface UsuarioService {

    Usuario autenticar(String email, String senha);

    Usuario cadastrar(Usuario usuario);

    void validarEmail(String email);

}
