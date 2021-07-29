package com.otp.moneyzeraapi.controller;

import com.otp.moneyzeraapi.model.Transacao;
import com.otp.moneyzeraapi.service.interfaces.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/transacao")
public class TransacaoController {

    @Autowired
    private TransacaoService service;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> salvar(Transacao transacao) {
        try {

            final Transacao transacaoSalva = service.salvar(transacao);

            return ResponseEntity.created(URI.create("/transacao/" + transacaoSalva.getId())).build();
        } catch (Exception error) {
            return ResponseEntity.badRequest().body(error.getMessage());
        }
    }

    @RequestMapping
    public ResponseEntity<?> atualizar(Transacao transacao) {
        try {
            final Transacao transacaoAtualizada = service.atualizar(transacao);

            return ResponseEntity.ok(transacaoAtualizada);
        } catch (Exception error) {
            return ResponseEntity.badRequest().body(error.getMessage());
        }
    }

}
