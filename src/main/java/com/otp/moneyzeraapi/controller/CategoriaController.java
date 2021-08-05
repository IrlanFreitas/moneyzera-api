package com.otp.moneyzeraapi.controller;

import com.otp.moneyzeraapi.exception.RegraNegocioException;
import com.otp.moneyzeraapi.model.Categoria;
import com.otp.moneyzeraapi.service.interfaces.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.Optional;

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

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> atualizar(@PathVariable("id") @NotNull Long id, @RequestBody Categoria categoriaForm) {

        try {
            final Optional<Categoria> categoria = service.obterPorId(id);

            if (categoria.isPresent()) {
                categoriaForm.setId(id);
                final Categoria categoriaAtualizada = service.atualizar(categoriaForm);
                return ResponseEntity.ok(categoriaAtualizada);
            }

            return ResponseEntity.noContent().build();

        } catch (Exception error) {
            return ResponseEntity.badRequest().body(error.getMessage());
        }

    }

}
