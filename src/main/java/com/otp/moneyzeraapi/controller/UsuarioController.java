package com.otp.moneyzeraapi.controller;

import com.otp.moneyzeraapi.dto.TokenDto;
import com.otp.moneyzeraapi.exception.ErroAutenticacao;
import com.otp.moneyzeraapi.dto.UsuarioDto;
import com.otp.moneyzeraapi.dto.UsuarioLoginDto;
import com.otp.moneyzeraapi.model.Usuario;
import com.otp.moneyzeraapi.service.interfaces.JwtService;
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

    @Autowired
    private JwtService jwtService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Usuario>> obter() {
        return ResponseEntity.ok( service.obterTodos() );
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> cadastrar(@Valid @RequestBody UsuarioDto usuarioForm) {

        try {
            final Usuario usuarioCadastrado = service.salvar(usuarioForm.getUsuario());

            return ResponseEntity.created(URI.create("/usuario/" + usuarioCadastrado.getId())).build();
        } catch (Exception error) {
            return ResponseEntity.badRequest().body(error.getMessage());
        }

    }

    @RequestMapping(path = "/autenticar", method = RequestMethod.POST)
    public ResponseEntity<?> autenticar(@Valid @RequestBody UsuarioLoginDto login) {

        try {
            final Usuario usuario = service.autenticar(login.getEmail(), login.getSenha());
            final String token = jwtService.gerarToken(usuario);

            final TokenDto tokenDto =  new TokenDto(token);

            return ResponseEntity.ok(tokenDto);
        } catch (ErroAutenticacao error) {
            return ResponseEntity.badRequest().body(error.getMessage());
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> atualizar(@RequestParam("id") Long id, @RequestBody UsuarioDto usuarioForm) {
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
