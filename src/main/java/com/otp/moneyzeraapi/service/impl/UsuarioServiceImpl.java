package com.otp.moneyzeraapi.service.impl;

import com.otp.moneyzeraapi.exception.ErroAutenticacao;
import com.otp.moneyzeraapi.exception.RegraNegocioException;
import com.otp.moneyzeraapi.model.Usuario;
import com.otp.moneyzeraapi.repository.UsuarioRepository;
import com.otp.moneyzeraapi.service.interfaces.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public Usuario autenticar(String email, String senha) {

        final Optional<Usuario> usuario = repository.findByEmail(email);

        if(usuario.isEmpty()) {
            throw new ErroAutenticacao("Usuário não encontrado.");
        }

        if(!encoder.matches(senha, usuario.get().getSenha())) {
            throw new ErroAutenticacao("Usuário ou senha inválido.");
        }

        return usuario.get();
    }

    @Override
    @Transactional
    public Usuario salvar(Usuario usuario) {

        validarEmail(usuario.getEmail());

        criptografarSenha(usuario);

        return repository.save(usuario);
    }

    private void criptografarSenha(Usuario usuario) {
        String senha = usuario.getSenha();
        final String encode = encoder.encode(senha);
        usuario.setSenha(encode);
    }

    @Override
    public void validarEmail(String email) {

        final Boolean existsUserByEmail = repository.existsByEmail(email);

        if(existsUserByEmail) throw new RegraNegocioException("Já existe usuário cadastrado com esse email");
    }

    @Override
    public Usuario atualizar(Usuario usuario) {

        if (usuario.getId() == null || usuario.getId() == 0)
            throw new IllegalArgumentException("Necessário id para atualizar o usuário");

        return repository.save(usuario);
    }

    @Override
    public void deletar(Long id) {

        if (id == null || id == 0)
            throw new IllegalArgumentException("Necessário id para atualizar o usuário");

        repository.deleteById(id);
    }

    @Override
    public Optional<Usuario> obterPorId(Long id) {

        if (id == null || id == 0)
            throw new RegraNegocioException("Necessário id para buscar usuario.");

        return repository.findById(id);
    }

    @Override
    public Optional<Usuario> obterPorEmail(String email) {

        if (email == null || email.isEmpty())
            throw new RegraNegocioException("Necessário email para buscar usuario.");

        return repository.findByEmail(email);
    }

    @Override
    public List<Usuario> obterTodos() {
        return repository.findAll();
    }
}
