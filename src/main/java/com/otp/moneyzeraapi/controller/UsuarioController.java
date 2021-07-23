package com.otp.moneyzeraapi.controller;

import com.otp.moneyzeraapi.form.UsuarioForm;
import com.otp.moneyzeraapi.model.Usuario;
import com.otp.moneyzeraapi.repository.UsuarioRepository;
import com.otp.moneyzeraapi.service.interfaces.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
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

        try {
            final Usuario usuarioCadastrado = service.cadastrar(usuario.getUsuario());

            return ResponseEntity.created(URI.create("/usuario/" + usuarioCadastrado.getId())).build();
        } catch (Exception error) {
            return ResponseEntity.badRequest().body(error.getMessage());
        }

    }
}
