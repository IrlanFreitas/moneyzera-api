package com.otp.moneyzeraapi.controller;

import com.otp.moneyzeraapi.form.ContaForm;
import com.otp.moneyzeraapi.model.Conta;
import com.otp.moneyzeraapi.model.Usuario;
import com.otp.moneyzeraapi.service.interfaces.ContaService;
import com.otp.moneyzeraapi.service.interfaces.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/conta")
public class ContaController {

    @Autowired
    private ContaService service;

    @Autowired
    private UsuarioService usuarioService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> salvar(@Valid @RequestBody ContaForm contaForm) {
        try {
            final Conta conta = service.salvar(contaForm.converter(usuarioService));

            return ResponseEntity.created(URI.create("/conta/"+conta.getId())).build();
        } catch (Exception error) {
            return ResponseEntity.badRequest().body(error.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> buscar() {
        try {
            return ResponseEntity.ok(service.obter());
        } catch (Exception error) {
            return ResponseEntity.badRequest().body(error.getMessage());
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> buscarPorId(@NotNull @PathVariable("id") Long id) {
        try {

            final Optional<Conta> conta = service.obterPorId(id);

            if (conta.isPresent())
                return ResponseEntity.ok(conta.get());

            return ResponseEntity.noContent().build();
        } catch (Exception error) {
            return ResponseEntity.badRequest().body(error.getMessage());
        }
    }

    @RequestMapping(path = "/{id}/saldo-usuario", method = RequestMethod.GET)
    public ResponseEntity<?> buscarSaldoPorUsuarioId(@NotNull @PathVariable("id") Long id){
        //TODO Onde as buscas forem aninhandas é necessário buscar o primeiro item, sempre
        //TODO ou melhor deixar que o erro informe isso?
        try {

            final Optional<Usuario> usuario = usuarioService.obterPorId(id);

            // TODO Deve ser feito em todo lugar que precisa pesquisar algo antes?
            if (usuario.isEmpty()) return ResponseEntity.notFound().build();

            return ResponseEntity.ok(service.obterSaldoUsuario(id));
        } catch (Exception error) {
            return ResponseEntity.badRequest().body(error.getMessage());
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> atualizar(@PathVariable("id") Long id, @Valid @RequestBody ContaForm contaForm ) {
        return service.obterPorId(id).map( contaEncontrada -> {
            try {

                Conta conta = service.atualizar(contaForm.converter(usuarioService));
                conta.setId(id);

                return ResponseEntity.ok(conta);
            } catch (Exception error) {
                return ResponseEntity.badRequest().body(error.getMessage());
            }
        }).orElseGet( () -> new ResponseEntity<>("Conta não encontrado", HttpStatus.BAD_REQUEST));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deletar(@PathVariable("id") @NotNull Long id) {
        try {
            //TODO Feito de outro jeito
            service.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception error) {
            return ResponseEntity.badRequest().body(error.getMessage());
        }
    }
}
