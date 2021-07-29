package com.otp.moneyzeraapi.controller;

import com.otp.moneyzeraapi.model.Categoria;
import com.otp.moneyzeraapi.service.interfaces.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService service;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> salvar(@RequestBody Categoria categoria) {
        try {
            final Categoria categoriaSalva = service.salvar(categoria);

            return ResponseEntity.created(URI.create("/categoria/" + categoriaSalva.getId())).build();
        } catch (Exception error) {

            return ResponseEntity.badRequest().body(error.getMessage());
        }
    }

}
