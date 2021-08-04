package com.otp.moneyzeraapi.controller;

import com.otp.moneyzeraapi.exception.ErroAutenticacao;
import com.otp.moneyzeraapi.form.UsuarioForm;
import com.otp.moneyzeraapi.form.UsuarioLoginForm;
import com.otp.moneyzeraapi.model.Usuario;
import com.otp.moneyzeraapi.repository.UsuarioRepository;
import com.otp.moneyzeraapi.service.interfaces.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private UsuarioService service;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Usuario>> obter() {
        return ResponseEntity.ok(repository.findAll());
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> cadastrar(@Valid @RequestBody UsuarioForm usuario) {

        // assert usuario != null : "Usuário não pode ser nulo";

        try {
            final Usuario usuarioCadastrado = service.salvar(usuario.getUsuario());

            return ResponseEntity.created(URI.create("/usuario/" + usuarioCadastrado.getId())).build();
        } catch (Exception error) {
            return ResponseEntity.badRequest().body(error.getMessage());
        }

    }

    @RequestMapping(path = "/autenticar", method = RequestMethod.POST)
    public ResponseEntity<?> autenticar(@Valid @RequestBody UsuarioLoginForm login) {

        try {
            final Usuario usuario = service.autenticar(login.getEmail(), login.getSenha());

            return ResponseEntity.ok(usuario);
        } catch (ErroAutenticacao error) {
            return ResponseEntity.badRequest().body(error.getMessage());
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> atualizar(@RequestParam("id") Long id, @RequestBody UsuarioForm usuarioForm) {
        return service.obterPorId(id).map(usuarioEncontrado -> {
            try {
                usuarioForm.setId(id);
                Usuario usuario = service.atualizar(usuarioForm.getUsuario());
                usuario.setData(usuarioEncontrado.getData());
                return ResponseEntity.ok(usuario);
            } catch (Exception error) {
                return ResponseEntity.badRequest().body(error.getMessage());
            }
        }).orElseGet(() -> new ResponseEntity("Usuário não encontrado.", HttpStatus.BAD_REQUEST));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deletar(@RequestParam("id") Long id) {
        return service.obterPorId(id).map( usuario -> {
            service.deletar(id);
            return ResponseEntity.noContent().build();
        }).orElseGet( () -> new ResponseEntity("Usuário não encontrado.", HttpStatus.BAD_REQUEST));
    }
}
