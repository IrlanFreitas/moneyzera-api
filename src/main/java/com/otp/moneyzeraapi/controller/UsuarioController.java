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
    private UsuarioService service;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Usuario>> obter() {
        return ResponseEntity.ok( service.obterTodos() );
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> cadastrar(@Valid @RequestBody UsuarioForm usuarioForm) {

        try {
            final Usuario usuarioCadastrado = service.salvar(usuarioForm.getUsuario());

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

                Usuario usuario =  usuarioForm.getUsuario();
                //TODO - Arrumar um jeito disso não atualizar pra null
                usuario.setData(usuarioEncontrado.getData());

                Usuario usuarioAtualizado = service.atualizar(usuario);

                return ResponseEntity.ok(usuarioAtualizado);
            } catch (Exception error) {
                return ResponseEntity.badRequest().body(error.getMessage());
            }
        }).orElseGet(() -> new ResponseEntity<>("Usuário não encontrado.", HttpStatus.BAD_REQUEST));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deletar(@RequestParam("id") Long id) {
        return service.obterPorId(id).map( usuario -> {
            service.deletar(id);
            return ResponseEntity.ok().build();
        }).orElseGet( () -> new ResponseEntity<>("Usuário não encontrado.", HttpStatus.BAD_REQUEST));
    }

}
