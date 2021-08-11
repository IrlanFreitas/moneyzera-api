package com.otp.moneyzeraapi.controller;

import com.otp.moneyzeraapi.enums.StatusTransacao;
import com.otp.moneyzeraapi.form.TransacaoForm;
import com.otp.moneyzeraapi.model.Conta;
import com.otp.moneyzeraapi.model.Transacao;
import com.otp.moneyzeraapi.model.Usuario;
import com.otp.moneyzeraapi.service.interfaces.CategoriaService;
import com.otp.moneyzeraapi.service.interfaces.ContaService;
import com.otp.moneyzeraapi.service.interfaces.TransacaoService;
import com.otp.moneyzeraapi.service.interfaces.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/transacao")
public class TransacaoController {

    @Autowired
    private TransacaoService service;

    @Autowired
    private ContaService contaService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private UsuarioService usuarioService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> salvar(@RequestBody TransacaoForm transacaoForm) {
        try {

            final Transacao transacao = service.salvar(transacaoForm.converter(contaService, categoriaService));

            return ResponseEntity.created(URI.create("/transacao/" + transacao.getId())).build();
        } catch (Exception error) {
            return ResponseEntity.badRequest().body(error.getMessage());
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> atualizar(@PathVariable("id") @NotNull Long id, @RequestBody TransacaoForm transacaoForm) {

        return service.buscarPorId(id).map(transacaoEncontrada -> {
            try {
                Transacao transacao = transacaoForm.converter(contaService, categoriaService);
                transacao.setId(transacaoEncontrada.getId());
                service.atualizar(transacao);
                return ResponseEntity.ok(transacao);
            } catch (Exception error) {
                return ResponseEntity.badRequest().body(error.getMessage());
            }
        }).orElseGet(() -> new ResponseEntity<>("Transação não encontrada.", HttpStatus.BAD_REQUEST));
    }

    @RequestMapping(path = "/{id}/atualiza-status", method = RequestMethod.PUT)
    public ResponseEntity<?> atualizarStatus(@PathVariable("id") @NotNull Long id, @RequestBody String status) {
        return service.buscarPorId(id).map( transacaoEncontrada -> {
            final StatusTransacao statusTransacao = StatusTransacao.valueOf(status);

            if (statusTransacao == null) return ResponseEntity.badRequest().body("Status inválido");

            try {
                transacaoEncontrada.setStatus(statusTransacao);
                service.atualizar(transacaoEncontrada);
                return ResponseEntity.ok().build();
            } catch (Exception error) {
                return ResponseEntity.badRequest().body(error.getMessage());
            }

        }).orElseGet( () ->  new ResponseEntity<>("Transação não encontrada", HttpStatus.NO_CONTENT));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deletar(@PathVariable("id") Long id) {
        //* * Como eu achei que seria suficiente
//        try {
//            service.deletar(id);
//            return ResponseEntity.ok().build();
//        } catch (Exception error) {
//            return ResponseEntity.badRequest().body(error.getMessage());
//        }

        //* * Como o curso fez
        return service.buscarPorId(id).map(transacaoEncontrada -> {
            try {
                service.deletar(transacaoEncontrada.getId());
                return ResponseEntity.ok().build();
            } catch (Exception error) {
                return ResponseEntity.badRequest().body(error.getMessage());
            }
        }).orElseGet(() -> new ResponseEntity<>("Transação não encontrado.", HttpStatus.BAD_REQUEST));
    }

    @RequestMapping(method = RequestMethod.GET)
    //TODO Necessário testar com o RequestParam recebendo os dados em forma de objeto
//    public ResponseEntity<?> buscar(@RequestParam TransacaoForm transacaoForm) {
    public ResponseEntity<?> buscar(@RequestParam(value = "descricao", required = false) String descricao,
                                    @RequestParam(value = "data", required = false) LocalDate data,
                                    @RequestParam(value = "usuario", required = false) Long usuarioId) {
        try {

            final Transacao transacao = Transacao.builder().descricao(descricao).data(data).contaOrigem(
                    Conta.builder().usuario(
                            Usuario.builder().id(usuarioId).build()).build()).build();

            //* * Essa forma de fazer casa com a ideia de passar como objeto
//            final List<Transacao> transacoes = service.buscar(transacaoForm.converter(contaService, categoriaService));
//            final List<Transacao> transacoes = service.buscar(transacao);

            final Optional<Usuario> usuario = usuarioService.obterPorId(usuarioId);

            if (usuario.isEmpty())
                return ResponseEntity.noContent().build();

            return ResponseEntity.ok(service.buscar(transacao));

        } catch (Exception error) {
            return ResponseEntity.badRequest().body(error.getMessage());
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> buscarPorId(@PathVariable("id") Long id) {
        try {
            final Optional<Transacao> transacao = service.buscarPorId(id);

            if (transacao.isPresent())
                return ResponseEntity.ok(transacao.get());

            return ResponseEntity.noContent().build();

        } catch (Exception error) {
            return ResponseEntity.badRequest().body(error.getMessage());
        }
    }

}
