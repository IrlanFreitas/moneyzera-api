package com.otp.moneyzeraapi.service.impl;

import com.otp.moneyzeraapi.exception.RegraNegocioException;
import com.otp.moneyzeraapi.model.Usuario;
import com.otp.moneyzeraapi.repository.UsuarioRepository;
import com.otp.moneyzeraapi.service.interfaces.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public Usuario autenticar(String email, String senha) {
        return null;
    }

    @Override
    public Usuario cadastrar(Usuario usuario) {

        validarEmail(usuario.getEmail());

        final Usuario usuarioComId = repository.save(usuario);

        return usuarioComId;
    }

    @Override
    public void validarEmail(String email) {

        final Boolean existsUserByEmail = repository.existsByEmail(email);

        if(existsUserByEmail) throw new RegraNegocioException("Já existe usuário cadastrado com esse email");
    }
}
